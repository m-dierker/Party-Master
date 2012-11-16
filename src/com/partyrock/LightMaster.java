package com.partyrock;

import java.util.ArrayList;

import com.partyrock.element.ElementController;
import com.partyrock.gui.LightWindowManager;

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
		for (ElementController element : elementList) {
			elements.add(element);
		}
		windowManager.updateElements();
	}
}
