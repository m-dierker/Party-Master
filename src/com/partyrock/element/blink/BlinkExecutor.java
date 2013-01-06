package com.partyrock.element.blink;

import com.partyrock.element.ElementExecutor;
import com.partyrock.tools.net.NetManager;

public class BlinkExecutor extends ElementExecutor {
	private BlinkController controller;

	public BlinkExecutor(BlinkController controller) {
		this.controller = controller;
	}

	public void cmdBlink(String params) {
		NetManager.get(getURL() + params);
	}

	/**
	 * For a Blink, the URL is the ID (ex: http://rasppi.dierkers.com)
	 */
	private String getURL() {
		String id = controller.getID();
		if (!id.endsWith("/")) {
			id += "/";
		}
		return id;
	}

}
