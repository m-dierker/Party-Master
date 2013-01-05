package com.partyrock.gui.elements;

import java.util.ArrayList;

import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import com.partyrock.element.ElementController;

/**
 * Will update the list of elements in an ElementTableRenderer when an element
 * has been added or updated.
 * @author Matthew
 * 
 */
public class ElementUpdater {
	/**
	 * Updates the elements in a given renderer. Basically, this will take every
	 * existing element and add it back in. It will then add in any new elements
	 */
	public static void updateElements(ElementTableRenderer obj, Table table) {
		ArrayList<ElementController> elements = new ArrayList<ElementController>(obj.getMaster().getElements());

		System.out.println(elements);
		System.out.println(table.getItems().length);
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

		// I don't understand this. It's necessary for LightWindow's main
		// window, but not for the Editor (where it shrinks things too much)
//		packTable(table);
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
