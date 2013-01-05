package com.partyrock.element.blink;

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

	public BlinkController(LightMaster master, String name, String id) {
		super(master, name, id);

		executor = new BlinkExecutor(this);
//		renderer = new BlinkRenderer(this);
//		simulator = new BlinkSimulator(this);

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
