package com.partyrock.gui.music;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;

import com.partyrock.LightMaster;
import com.partyrock.config.PartyConstants;
import com.partyrock.gui.LightWindow;

public class MusicRenderer {
    private LightWindow main;
    private LightMaster master;

    public MusicRenderer(LightWindow window) {
        this.main = window;
        this.master = window.getMaster();
    }

    /**
     * Renders a music bar on the given GC based on where the music is. Assumes that it's given a GC that has the first
     * ELEMENT_NAME_COLUMN_SIZE pixels for the element name, and the next pixels are all for the show
     * 
     */
    public void renderMusic(GC gc, Rectangle bounds, boolean triangles) {
        gc.setForeground(main.getDisplay().getSystemColor(SWT.COLOR_GREEN));
        gc.setBackground(main.getDisplay().getSystemColor(SWT.COLOR_GREEN));
        double currentTime = master.getShowManager().getCurrentTime();

        if (currentTime == -1) {
            return;
        }

        drawMarker(gc,
                (int) (PartyConstants.ELEMENT_NAME_COLUMN_SIZE + (currentTime * PartyConstants.PIXELS_PER_SECOND)),
                bounds.height, triangles);
    }

    /**
     * Draws a marker where the music is. If triangles is set to true, it will draw triangles at the top and bottom.
     * There's reason not to do this on things like tables where you have to worry about clipping (and I don't feel like
     * doing that right now)
     * 
     * @param gc The GC to use for rendering
     * @param x The X to render at
     * @param height The height of where we're rendering
     * @param triangles Whether to draw the triangles
     */
    public void drawMarker(GC gc, int x, int height, boolean triangles) {
        gc.drawLine(x, 0, x, height);

        if (triangles) {
            int triangleWidth = 10;
            int[] topTriangle = { x - triangleWidth / 2, 0, x + triangleWidth / 2, 0, x, triangleWidth / 2 };
            gc.fillPolygon(topTriangle);

            int[] botTriangle = { x - triangleWidth / 2, height, x + triangleWidth / 2, height, x,
                    height - triangleWidth / 2 };
            gc.fillPolygon(botTriangle);
        }
    }
}
