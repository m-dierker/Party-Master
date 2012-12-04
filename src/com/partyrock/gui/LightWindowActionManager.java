package com.partyrock.gui;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * Handles clicks for a LightWindow
 * @author Matthew
 * 
 */
public class LightWindowActionManager implements Listener {

	private LightWindow window;

	public LightWindowActionManager(LightWindow window) {
		this.window = window;
	}

	@Override
	public void handleEvent(Event e) {
		Object data = e.widget.getData();

		if (!(data instanceof GUIAction)) {
			System.out.println("A GUI action was not set for item: " + e.item);
			return;
		}

		GUIAction action = (GUIAction) data;

		// When adding processing for new actions, add them below here. Don't
		// forget the break statement!

		switch (action) {
		case EDIT_ELEMENTS:
			window.showElementsEditor();
			break;
		case EDIT_UC:
			window.showUCEditor();
			break;
		}
	}
}
