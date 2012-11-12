package com.partyrock.gui;

import org.eclipse.swt.widgets.Shell;

import com.partyrock.LightMaster;

/**
 * The main GUI window
 * @author Matthew
 * 
 */
public class LightWindow {
	private LightMaster master;
	private LightWindowManager manager;
	private Shell shell;

	public LightWindow(LightMaster master, LightWindowManager manager) {
		this.master = master;
		this.manager = manager;

		this.shell = new Shell(manager.getDisplay());

	}
}
