package com.partyrock;

import java.util.ArrayList;

import com.partyrock.element.ElementController;
import com.partyrock.element.lights.LightController;
import com.partyrock.gui.LightWindowManager;
import com.partyrock.gui.dialog.InputDialog;

/**
 * LightMaster is, as the name implies, the master class. Everything starts
 * here.
 * @author Matthew
 * 
 */
public class LightMaster {
	private LightWindowManager windowManager;
	private ArrayList<ElementController> elements;

	public LightMaster(String... args) {

		elements = new ArrayList<ElementController>();

		// Construct the GUI. This will make the main window.
		windowManager = new LightWindowManager(this);

		System.out.println(windowManager);

		windowManager.loop();
	}

	public static void main(String... args) {
		new LightMaster(args);
	}

	public ArrayList<ElementController> getElements() {
		return elements;
	}

	/**
	 * Adds an element to the list
	 * @param elementList The list of elements to add
	 */
	public void addElement(ElementController... elementList) {
		// Add all elements to the element list
		for (ElementController element : elementList) {
			elements.add(element);
		}

		// Update the elements in the window manager
		windowManager.updateElements();
	}

	/**
	 * Shows a window to add new element(s)
	 */
	public void addNewElement() {
		InputDialog dialog = new InputDialog(windowManager.getMain().getShell(), "How many lights would you like to add?", "Add Lights");
		String amountString = dialog.open();

		int amount = 0;
		try {
			amount = Integer.parseInt(amountString);
		} catch (Exception e) {
			System.out.println("Invalid amount specified");
			return;
		}

		for (int a = 0; a < amount; a++) {
			LightController controller = new LightController(this, "Strand " + (elements.size() + 1));
			addElement(controller);
		}

		System.out.println(elements);

	}
}
