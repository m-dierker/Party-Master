package com.partyrock.gui.elements;

/**
 * Something that displays elements and should be notified when elements are
 * updated
 * @author Matthew
 * 
 */
public interface ElementDisplay {
	public void updateElements();

	public boolean isDisposed();
}
