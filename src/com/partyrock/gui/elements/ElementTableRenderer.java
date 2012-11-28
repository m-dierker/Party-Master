package com.partyrock.gui.elements;

import com.partyrock.LightMaster;
import com.partyrock.element.ElementController;

/**
 * Something that lists all elements in a table (so we can easily update it)
 * @author Matthew
 * 
 */
public interface ElementTableRenderer {
	/**
	 * Returns the light master
	 */
	public LightMaster getMaster();

	/**
	 * Adds an element to the table
	 * @param element the row to add
	 */
	public void addElementAsRow(ElementController element);
}
