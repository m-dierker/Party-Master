package com.partyrock.gui.elements;

import com.partyrock.LightMaster;
import com.partyrock.element.ElementController;

/**
 * Something that lists elements in a table. This class exists so we can update
 * elements when a user edits an element
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
