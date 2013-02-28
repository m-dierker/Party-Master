package com.partyrock.element.led;

import org.eclipse.swt.graphics.GC;

import com.partyrock.element.ElementSimulator;

public class LEDPanelSimulator extends ElementSimulator {

	private LEDPanelController controller;
	private final int SPACE_FROM_TEXT = 5;
	private final int DIAMETER = 5;
	private final int LED_SPACE = 2;

	public LEDPanelSimulator(LEDPanelController controller) {
		super(controller);
		this.controller = controller;
	}

	@Override
	public void renderElement(GC gc) {
		int y = getY() + getTextBounds(gc).y + SPACE_FROM_TEXT;
		for (int r = 0; r < controller.getPanelHeight(); r++) {
			for (int c = 0; c < controller.getPanelWidth(); c++) {
				gc.setBackground(controller.getColor(r, c));
				gc.fillOval(getX() + c * (DIAMETER + LED_SPACE), y + r * (DIAMETER + LED_SPACE), DIAMETER, DIAMETER);
			}
		}
	}

	@Override
	public int getNormalWidth() {
		return (int) Math.max(getTextBounds().x, (DIAMETER + LED_SPACE) * controller.getPanelWidth());
	}

	@Override
	public int getNormalHeight() {
		return getTextBounds().y + SPACE_FROM_TEXT + (DIAMETER + LED_SPACE) * controller.getPanelHeight();
	}

}
