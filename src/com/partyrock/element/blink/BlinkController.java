package com.partyrock.element.blink;

import org.eclipse.swt.graphics.Color;

import com.partyrock.LightMaster;
import com.partyrock.element.ElementController;

/**
 * Overall controller for a blink(1) USB LED (specifically with the Django api)
 * More info: https://github.com/mvd7793/blink1-api
 * 
 * @author Matthew
 * 
 */
public class BlinkController extends ElementController {

	private BlinkExecutor executor;
	private BlinkRenderer renderer;
	private BlinkSimulator simulator;

	public BlinkController(LightMaster master, String internalID, String name, String id) {
		super(master, internalID, name, id);

		executor = new BlinkExecutor(this);
//		renderer = new BlinkRenderer(this);
//		simulator = new BlinkSimulator(this);

	}

	public void on() {
		executor.cmdBlink("on");
	}

	public void off() {
		executor.cmdBlink("off");
	}

	public void red() {
		executor.cmdBlink("red");
	}

	public void green() {
		executor.cmdBlink("green");
	}

	public void blue() {
		executor.cmdBlink("blue");
	}

	public void setColor(Color c) {
		setColor(c.getRed(), c.getGreen(), c.getBlue());
	}

	public void setColor(int r, int g, int b) {
		executor.cmdBlink("rgb?r=" + r + "&g=" + g + "&b=" + b);
	}

	public String getTypeName() {
		return "Blink";
	}

	public BlinkRenderer getRenderer() {
		return renderer;
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
}
