package com.partyrock;

import com.partyrock.gui.LightWindowManager;

/**
 * LightMaster is, as the name implies, the master class. Everything starts
 * here.
 * @author Matthew
 * 
 */
public class LightMaster {
	private LightWindowManager windowManager;

	public LightMaster(String... args) {

		// Construct the GUI. This will make the main window.
		windowManager = new LightWindowManager(this);

	}

	public static void main(String... args) {
		new LightMaster(args);
	}
}
