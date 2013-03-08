package com.partyrock.element.lights;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;

import com.partyrock.element.ElementSimulator;

public class LightSimulator extends ElementSimulator {
    private LightController controller;
    private static final int SPACE_FROM_TEXT = 5;
    private static final int WIDTH = 30;
    private static final int HEIGHT = 10;
    private Color onColor;
    private Color offColor;

    public LightSimulator(LightController controller) {
        super(controller);
        this.controller = controller;
        onColor = controller.getMaster().getWindowManager().getDisplay().getSystemColor(SWT.COLOR_RED);
        offColor = new Color(onColor.getDevice(), onColor.getRed() / 2, onColor.getGreen() / 2, onColor.getBlue() / 2);
    }

    @Override
    public int getNormalWidth() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getNormalHeight() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void renderElement(GC gc) {
        gc.setBackground(controller.isOn() ? onColor : offColor);
        gc.fillRectangle(getX(), getY() + getTextBounds().y + SPACE_FROM_TEXT, WIDTH, HEIGHT);
    }
}
