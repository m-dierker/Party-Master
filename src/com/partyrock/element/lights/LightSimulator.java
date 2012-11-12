package com.partyrock.element.lights;

import com.partyrock.element.ElementSimulator;

public class LightSimulator extends ElementSimulator {
	private LightController controller;

	public LightSimulator(LightController controller) {
		this.controller = controller;
	}
}
