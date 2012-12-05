package com.partyrock.gui.uc;

import com.partyrock.LightMaster;
import com.partyrock.comm.uc.Microcontroller;

/**
 * Something that lists uC in a table. This class exists so we can update
 * uCs when a user edits one
 * @author Matthew
 * 
 */
public interface UCTableRenderer {
	/**
	 * Returns the light master
	 */
	public LightMaster getMaster();

	/**
	 * Adds a uC to the table
	 * @param controller the row to add
	 */
	public void addControllerAsRow(Microcontroller controller);
}
