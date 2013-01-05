package com.partyrock.element.blink;

import java.awt.Color;

import com.partyrock.element.ElementExecutor;
import com.partyrock.tools.net.NetManager;

public class BlinkExecutor extends ElementExecutor {
	private BlinkController controller;

	public BlinkExecutor(BlinkController controller) {
		this.controller = controller;
	}

	public void on() {
		cmdBlink("on");
	}

	public void off() {
		cmdBlink("off");
	}

	public void red() {
		cmdBlink("red");
	}

	public void green() {
		cmdBlink("green");
	}

	public void blue() {
		cmdBlink("blue");
	}

	public void setColor(Color c) {
		setColor(c.getRed(), c.getGreen(), c.getBlue());
	}

	public void setColor(int r, int g, int b) {
		cmdBlink("rgb?r=" + r + "&g=" + g + "&b=" + b);
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
