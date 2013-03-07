package com.partyrock.element.lasers;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;

import com.partyrock.element.ElementSimulator;

public class LaserSimulator extends ElementSimulator {

    private LaserController controller;
    private final int graphWidth = 60;
    private final int graphHeight = 30;
    private final int SPACE_FROM_TEXT = 5;

    public LaserSimulator(LaserController controller) {
        super(controller);
        this.controller = controller;
    }

    @Override
    public void renderElement(GC gc) {
        gc.setForeground(controller.getMaster().getWindowManager().getDisplay().getSystemColor(SWT.COLOR_GRAY));

        // Draw bottom line
        gc.drawLine(getX(), getY() + SPACE_FROM_TEXT + graphHeight, getX() + graphWidth, getY() + SPACE_FROM_TEXT
                + graphHeight);

        // Draw vertical line
        gc.drawLine(getX() + graphWidth / 2, getY() + SPACE_FROM_TEXT, getX() + graphWidth / 2, getY()
                + SPACE_FROM_TEXT + graphHeight);

        gc.setBackground(controller.getMaster().getWindowManager().getDisplay().getSystemColor(SWT.COLOR_GREEN));
        gc.fillOval(getX() + (controller.getX() / (180 / graphWidth)) - 2, getY() + graphHeight + SPACE_FROM_TEXT
                - (controller.getY() / (90 / graphHeight)) - 2, 5, 5);
    }

    @Override
    public int getNormalWidth() {
        return (int) Math.max(getTextBounds().x, graphWidth);
    }

    @Override
    public int getNormalHeight() {
        return getTextBounds().y + graphHeight;
    }

}
