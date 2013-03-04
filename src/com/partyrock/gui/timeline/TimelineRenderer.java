package com.partyrock.gui.timeline;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.widgets.ScrollBar;

import com.partyrock.LightMaster;
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

    public TimelineRenderer(LightWindow window) {
        this.main = window;
    }

    public void renderTimeline(GC gc, Rectangle timeline) {
        gc.setBackground(main.getDisplay().getSystemColor(SWT.COLOR_BLACK));
        gc.fillRectangle(timeline);

        ScrollBar bar = main.getMainTable().getHorizontalBar();
        int xOffset = 0;
        if (bar.isVisible()) {
            xOffset = bar.getSelection() * -1;
        }

        Transform transform = new Transform(main.getDisplay());
        transform.translate(xOffset, 0);
        gc.setTransform(transform);

        gc.setForeground(main.getDisplay().getSystemColor(SWT.COLOR_CYAN));
        gc.drawLine(0, timeline.height / 2, timeline.width, timeline.height / 2);
        gc.drawText("50", 50, timeline.height / 2 + 5, true);
    }
}
