package com.partyrock.gui.select;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;

import com.partyrock.LightMaster;
import com.partyrock.config.PartyConstants;
import com.partyrock.gui.LightWindow;

public class SelectionRenderer {
    @SuppressWarnings("unused")
    private LightMaster master;
    private LightWindow main;
    private Color bgColor;

    public SelectionRenderer(LightWindow window) {
        this.main = window;
        master = main.getMaster();
        bgColor = new Color(main.getDisplay(), 220, 220, 220);
    }

    public void renderSelection(GC gc, Rectangle bounds, Selection selection, boolean triangles) {

        // If we get an empty selection, just return
        if (selection == null) {
            return;
        }

        gc.setBackground(bgColor);
        gc.setAlpha(50);
        int startX = (int) (selection.start * PartyConstants.PIXELS_PER_SECOND)
                + PartyConstants.ELEMENT_NAME_COLUMN_SIZE;
        int durationPixels = (int) (selection.duration * PartyConstants.PIXELS_PER_SECOND);
        int endX = startX + durationPixels;
        gc.fillRectangle(startX, 0, durationPixels, bounds.height);

        // Left mark
        gc.setForeground(main.getDisplay().getSystemColor(SWT.COLOR_RED));
        gc.setBackground(main.getDisplay().getSystemColor(SWT.COLOR_RED));
        MarkRenderer.drawMarker(gc, startX, bounds.height, triangles);

        // Right mark
        MarkRenderer.drawMarker(gc, endX, bounds.height, triangles);

    }
}
