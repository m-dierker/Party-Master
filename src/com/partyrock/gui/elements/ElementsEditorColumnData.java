package com.partyrock.gui.elements;

/**
 * Data for an editable column (so we can record things like "editable")
 * @author Matthew
 * 
 */
public class ElementsEditorColumnData {
	private boolean editable;

	public ElementsEditorColumnData(boolean editable) {
		this.editable = editable;
	}

	public boolean isEditable() {
		return editable;
	}
}
