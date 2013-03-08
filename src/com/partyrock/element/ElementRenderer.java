package com.partyrock.element;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Pattern;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

/**
 * Renders the name in the ElemenetTable, and can be overriden to customize rendering for children
 * 
 * @author Matthew
 * 
 */
public class ElementRenderer {
    private ElementController controller;
    private Color foregroundColor;
    private Color backgroundColor;
    private Color textColor;

    public ElementRenderer(ElementController controller) {
        this.controller = controller;

        Display display = controller.getMaster().getWindowManager().getDisplay();

        // You can override these in subclasses to just change colors
        foregroundColor = new Color(display, 230, 0, 0);
        backgroundColor = new Color(display, 148, 0, 0);
        textColor = display.getSystemColor(SWT.COLOR_WHITE);
    }

    /**
     * Renders the name portion of the table. Should be called on EraseItem. We do this so we can customize rendering of
     * the name differently for each Element
     * 
     * @param gc The GC to draw with
     * @param rect The rectangle representing the bounds of the naming box
     */
    public void renderName(GC gc, Rectangle rect) {
        String name = controller.getName();
        Point nameSize = gc.textExtent(name);

        // Hello double buffering

        Pattern pattern = new Pattern(gc.getDevice(), 0, 0, rect.width, rect.height, foregroundColor, backgroundColor);
        gc.setBackgroundPattern(pattern);
        gc.fillRoundRectangle(0, 3, rect.width, rect.height - 6, 15, 15);
        //
        gc.setForeground(textColor);
        //
        int xOffset = (rect.width - nameSize.x) / 2;
        if (xOffset < 0) {
            xOffset = 0;
        }

        int yOffset = (rect.height - nameSize.y) / 2;
        if (yOffset < 0) {
            yOffset = 0;
        }

        gc.drawString(name, xOffset, yOffset + 3, true);
    }
}
