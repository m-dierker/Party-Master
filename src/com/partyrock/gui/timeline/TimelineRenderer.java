package com.partyrock.gui.timeline;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.widgets.ScrollBar;

import com.partyrock.LightMaster;
import com.partyrock.config.PartyConstants;
import com.partyrock.gui.LightWindow;

/**
 * Renders the timeline at the bottom of the screen and such
 * 
 * @author Matthew
 * 
 */
public class TimelineRenderer {
    private LightMaster master;
    private LightWindow main;
    private Image image;
    private int lastPPS = -1;

    public TimelineRenderer(LightWindow window) {
        this.main = window;
        master = window.getMaster();
    }

    public void renderTimeline(GC gcOrig, Rectangle timeline) {

        // Clipping automatically only displays what's on the screen, so just draw everything every time and the
        // clipping will take care of it not rendering

        int musicWidth = (int) (PartyConstants.PIXELS_PER_SECOND * master.getShowManager().getMusicDuration());
        int totalWidth = musicWidth + PartyConstants.ELEMENT_NAME_COLUMN_SIZE;

        int xOffset = getXOffset(timeline);

        if (xOffset == -1) {
            // No music has been loaded
            // Draw the background
            gcOrig.setBackground(main.getDisplay().getSystemColor(SWT.COLOR_BLACK));
            gcOrig.fillRectangle(timeline);

            String text = "Party Master - Developed by Party Rock Illinois @ University of Illinois Urbana-Champaign - Engineering Open House 2013";
            Point textBounds = gcOrig.textExtent(text);
            gcOrig.setForeground(main.getDisplay().getSystemColor(SWT.COLOR_CYAN));
            gcOrig.drawText(text, timeline.width / 2 - textBounds.x / 2, timeline.height / 2 - textBounds.y / 2, true);
            return;
        }

        Transform transform = new Transform(main.getDisplay());
        transform.translate(xOffset, 0);
        gcOrig.setTransform(transform);

        if (lastPPS != PartyConstants.PIXELS_PER_SECOND) {
            image = new Image(main.getDisplay(), totalWidth, timeline.height);
            GC gc = new GC(image);

            lastPPS = PartyConstants.PIXELS_PER_SECOND;

            // Draw the background before we transform anything
            gc.setBackground(main.getDisplay().getSystemColor(SWT.COLOR_BLACK));
            gc.fillRectangle(timeline.x, timeline.y, totalWidth, timeline.height);

            gc.setForeground(main.getDisplay().getSystemColor(SWT.COLOR_CYAN));
            gc.setBackground(main.getDisplay().getSystemColor(SWT.COLOR_CYAN));
            // draw the base timeline
            gc.fillRectangle(PartyConstants.ELEMENT_NAME_COLUMN_SIZE, timeline.height / 2 - 1, musicWidth, 2);

            int maxUsedX = -1;
            // Render timeline lines
            for (int a = 0; a < master.getShowManager().getMusicDuration(); a++) {
                Point textBounds = gc.textExtent("" + a);

                int x = a * PartyConstants.PIXELS_PER_SECOND + PartyConstants.ELEMENT_NAME_COLUMN_SIZE;

                // Render a tick
                gc.drawLine(x, timeline.height / 2 - timeline.height / 6, x, timeline.height / 2 + timeline.height / 6);

                // See if we should render the text
                int minX = x - textBounds.x / 2;

                // Check if the X we want to use is open based on the last maximum X value, and a margin
                if (minX <= maxUsedX + 3) {
                    continue;
                }

                // Render text
                gc.drawText("" + a, x - textBounds.x / 2, timeline.height / 2 + timeline.height / 6, true);

                maxUsedX = x + textBounds.x / 2;
            }
        }

        gcOrig.drawImage(image, 0, 0);

        main.getMusicRenderer().renderMusic(gcOrig, timeline, true);
    }

    /**
     * Returns the xOffset applied to the timeline to account for scrolling in the table.
     * 
     * @param timeline
     *            The bounds of the the timeline
     * @return the *NEGATIVE* xOffset (something like -100)
     */
    public int getXOffset(Rectangle timeline) {
        ScrollBar bar = main.getMainTable().getHorizontalBar();

        // The total width due to music
        int musicWidth = (int) (PartyConstants.PIXELS_PER_SECOND * master.getShowManager().getMusicDuration());

        // The total width in pixels
        int totalWidth = musicWidth + PartyConstants.ELEMENT_NAME_COLUMN_SIZE;

        /*
         * For some awkward reason, bar.getSelection()'s actual max doesn't equal bar.getMaximum(). This stupid hack
         * takes the normal bar maximum on a mac (100) an changes it to the actual seen value (90)
         */
        int barMaximum = 90;

        if (bar.isEnabled() && master.getShowManager().getMusicDuration() != -1) {
            double percentage = 1.0 * bar.getSelection() / barMaximum;
            return (int) (-1 * (totalWidth - timeline.width) * percentage);
        } else {
            return -1;
        }
    }
}
