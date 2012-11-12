package com.partyrock.gui;

import com.partyrock.LightMaster;

/**
 * LightWindowManager manages *all* GUI things. Constructing a
 * LightWindowManager automatically makes a LightWindow.
 * @author Matthew
 * 
 */
public class LightWindowManager {
	private LightMaster master;

	public LightWindowManager(LightMaster master) {
		this.master = master;
	}
}
