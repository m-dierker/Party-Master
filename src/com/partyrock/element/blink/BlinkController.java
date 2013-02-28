package com.partyrock.element.blink;

import org.eclipse.swt.graphics.Color;

import com.partyrock.LightMaster;
import com.partyrock.element.ElementController;
import com.partyrock.element.ElementType;

/**
 * Overall controller for a blink(1) USB LED (specifically with the Django api)
 * More info: https://github.com/mvd7793/blink1-api
 * 
 * @author Matthew
 * 
 */
public class BlinkController extends ElementController {

	private BlinkExecutor executor;
	private BlinkSimulator simulator;
	private Color color;

	public BlinkController(LightMaster master, String internalID, String name, String id) {
		super(master, internalID, name, id);

		executor = new BlinkExecutor(this);
		simulator = new BlinkSimulator(this);

		color = new Color(master.getWindowManager().getDisplay(), 0, 0, 0);

	}

	public void setColor(Color c) {
		setColor(c.getRed(), c.getGreen(), c.getBlue());
		color = c;
	}

	public void setColor(int r, int g, int b) {
		executor.cmdBlink("rgb?r=" + r + "&g=" + g + "&b=" + b);
		color = new Color(master.getWindowManager().getDisplay(), r, g, b);
	}

	public ElementType getType() {
		return ElementType.BLINK;
	}

	public BlinkExecutor getExecutor() {
		return executor;
	}

	public BlinkSimulator getSimulator() {
		return simulator;
	}

	public String toString() {
		return "Blink - " + super.toString();
	}

	public Color getColor() {
		return color;
	}
}
