package com.partyrock.gui.elements;

import org.eclipse.swt.widgets.TableItem;

/**
 * A notification wrapper from an ElementsDoubleClickListener when a table
 * element is changed
 * @author Matthew
 * 
 */
public interface ElementsTableEditor {
	/**
	 * Alert that a table item has changed text. Note that this method *will*
	 * need to actually update the text with item.setText(column, text);
	 * @param item The item changed
	 * @param column The column changed
	 * @param text The new text for the column
	 */
	public void updateItemWithText(TableItem item, int column, String text);
}
