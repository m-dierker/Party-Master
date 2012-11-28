package com.partyrock.gui;

import java.util.ArrayList;

import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import com.partyrock.element.ElementController;

/**
 * Will update the list of elements in a ElementTableRenderer
 * @author Matthew
 * 
 */
public class ElementUpdater {
	/**
	 * Updates the elements in the table
	 */
	public static void updateElements(ElementTableRenderer obj, Table table) {
		ArrayList<ElementController> elements = new ArrayList<ElementController>(obj.getMaster().getElements());

		// Add every existing element that is in the current elements list to
		// the table
		for (TableItem item : table.getItems()) {
			ElementController controller = (ElementController) item.getData();
			if (elements.contains(controller)) {
				elements.remove(controller);
				obj.addElementAsRow(controller);
			}
			item.dispose();
		}

		// Add the remaining (new) elements at the bottom
		for (int a = 0; a < elements.size(); a++) {
			obj.addElementAsRow(elements.get(a));
		}

		packTable(table);
	}

	/**
	 * Packs the table's columns to make them the correct size
	 * @param table the table to pack
	 */
	public static void packTable(Table table) {
		for (int i = 0; i < table.getColumnCount(); i++) {
			table.getColumn(i).pack();
		}
	}
}
