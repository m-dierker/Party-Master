package com.partyrock.gui.uc;

import java.util.ArrayList;

import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import com.partyrock.comm.uc.Microcontroller;

/**
 * Will update the list of microcontrollers in an UCTableRenderer when a
 * uC has been added or updated.
 * @author Matthew
 * 
 */
public class UCUpdater {
	/**
	 * Updates the microcontrollers in the table
	 */
	public static void updateElements(UCTableRenderer obj, Table table) {
		ArrayList<Microcontroller> controllers = new ArrayList<Microcontroller>(obj.getMaster().getControllers());

		// Add every existing uC that is in the current uC list to
		// the table
		for (TableItem item : table.getItems()) {
			Microcontroller controller = (Microcontroller) item.getData();
			if (controllers.contains(controller)) {
				controllers.remove(controller);
				obj.addControllerAsRow(controller);
			}
			item.dispose();
		}

		// Add the remaining (new) uCs at the bottom
		for (int a = 0; a < controllers.size(); a++) {
			obj.addControllerAsRow(controllers.get(a));
		}
	}
}
