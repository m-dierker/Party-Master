package com.partyrock.element.blink;

import com.partyrock.element.ElementExecutor;
import com.partyrock.tools.net.NetManager;

public class BlinkExecutor extends ElementExecutor {
	private BlinkController controller;

	public BlinkExecutor(BlinkController controller) {
		this.controller = controller;
	}

	/**
	 * Send a command to the Blink with certain parameters
	 * @param params the URL to hit, something like /red, /off, or /rgb?r=255&g=0&b=128
	 */
	public void cmdBlink(String params) {
		System.out.println("Hitting " + getURL() + params);

		// Since this is a dev element, we just skip it if the URL starts with NULL to prevent an exception
		if (getURL().toUpperCase().startsWith("NULL")) {
			System.out.println("The blink URL starts with NULL, not hitting the URL");
		} else {
			NetManager.get(getURL() + params);
		}
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
