package com.partyrock.element.lights;

import com.partyrock.element.ElementExecutor;

public class LightExecutor extends ElementExecutor {
	private LightController controller;

	public LightExecutor(LightController controller) {
		this.controller = controller;
	}
}
