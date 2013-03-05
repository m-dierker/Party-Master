package com.partyrock.gui.select;

import org.eclipse.swt.graphics.GC;

public class MarkRenderer {
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
    public static void drawMarker(GC gc, int x, int height, boolean triangles) {
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
