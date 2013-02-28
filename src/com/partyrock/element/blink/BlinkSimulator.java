package com.partyrock.element.blink;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;

import com.partyrock.element.ElementSimulator;

public class BlinkSimulator extends ElementSimulator {

	private BlinkController controller;
	private final int DIAMETER = 20;
	private final int SPACE_FROM_TEXT = 5;

	public BlinkSimulator(BlinkController controller) {
		super(controller);
		this.controller = controller;
	}

	@Override
	public void renderElement(GC gc) {
		int y = this.getY() + getTextBounds(gc).y + SPACE_FROM_TEXT;
		Color color = controller.getColor();
		gc.setBackground(color);
		gc.fillOval(getX() + getWidth() / 2 - DIAMETER / 2, y, DIAMETER, DIAMETER);
	}

	@Override
	public int getNormalWidth() {
		return (int) Math.max(getTextBounds().x, DIAMETER);
	}

	@Override
	public int getNormalHeight() {
		return getTextBounds().y + SPACE_FROM_TEXT + DIAMETER;
	}

}
