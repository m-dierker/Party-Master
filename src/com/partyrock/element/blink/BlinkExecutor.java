package com.partyrock.element.blink;

import com.partyrock.element.ElementExecutor;
import com.partyrock.tools.net.NetManager;

public class BlinkExecutor extends ElementExecutor {
	private BlinkController controller;

	public BlinkExecutor(BlinkController controller) {
		this.controller = controller;
	}

	public void cmdBlink(String params) {
		System.out.println("Hitting " + getURL() + params);
		NetManager.get(getURL() + params);
	}

	/**
	 * For a Blink, the URL is the ID (ex: http://rasppi.dierkers.com) - we need
	 * to make sure the slash is consistent, so this will always return a
	 * trailing slash
	 */
	private String getURL() {
		String id = controller.getID();
		if (!id.endsWith("/")) {
			id += "/";
		}
		return id;
	}

}
