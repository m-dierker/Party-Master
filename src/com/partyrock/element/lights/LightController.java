package com.partyrock.element.lights;

import com.partyrock.LightMaster;
import com.partyrock.element.ElementController;

/**
 * Overall controller for a light strand
 * @author Matthew
 * 
 */
public class LightController extends ElementController {

	private LightExecutor executor;
	private LightRenderer renderer;
	private LightSimulator simulator;

	public LightController(LightMaster master, String name) {
		super(master, name);

		executor = new LightExecutor(this);
		renderer = new LightRenderer(this);
		simulator = new LightSimulator(this);

	}

	public LightRenderer getRenderer() {
		return renderer;
	}

	public LightExecutor getExecutor() {
		return executor;
	}

	public LightSimulator getSimulator() {
		return simulator;
	}
}
