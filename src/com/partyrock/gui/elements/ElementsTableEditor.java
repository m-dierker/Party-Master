package com.partyrock.gui.elements;

import org.eclipse.swt.widgets.TableItem;

import com.partyrock.LightMaster;

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
	 * Just after this method is called, the LightLocationManager will be informed if there are changes
	 * @param item The item changed
	 * @param column The column changed
	 * @param text The new text for the column
	 */
	public void updateItemWithText(TableItem item, int column, String text);

	public LightMaster getMaster();
}
