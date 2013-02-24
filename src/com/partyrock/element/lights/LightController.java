package com.partyrock.element.lights;

import com.partyrock.LightMaster;
import com.partyrock.element.ElementController;
import com.partyrock.element.ElementType;

/**
 * Overall controller for a light strand
 * @author Matthew
 * 
 */
public class LightController extends ElementController {

	private LightExecutor executor;
	private LightSimulator simulator;

	public LightController(LightMaster master, String internalID, String name, String id) {
		super(master, internalID, name, id);

		executor = new LightExecutor(this);
		simulator = new LightSimulator(this);

	}

	public ElementType getType() {
		return ElementType.LIGHTS;
	}

	public LightExecutor getExecutor() {
		return executor;
	}

	public LightSimulator getSimulator() {
		return simulator;
	}

	public String toString() {
		return "Strand - " + super.toString();
	}
}
