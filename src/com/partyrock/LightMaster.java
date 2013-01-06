package com.partyrock;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.partyrock.comm.uc.Microcontroller;
import com.partyrock.element.ElementController;
import com.partyrock.gui.LightWindowManager;
import com.partyrock.settings.PersistentSettings;
import com.partyrock.settings.SectionSettings;

/**
 * LightMaster is, as the name implies, the master class. Everything starts
 * here.
 * @author Matthew
 * 
 */
public class LightMaster {
	private LightWindowManager windowManager;
	private ArrayList<ElementController> elements;
	private ArrayList<Microcontroller> controllers;
	private PersistentSettings location;

	public LightMaster(String... args) {

		elements = new ArrayList<ElementController>();
		controllers = new ArrayList<Microcontroller>();

		// Construct the GUI. This will make the main window.
		windowManager = new LightWindowManager(this);

		windowManager.loop();
	}

	public static void main(String... args) {
		new LightMaster(args);
	}

	/**
	 * Returns the list of elements (lights, lasers, LEDs, etc.)
	 */
	public ArrayList<ElementController> getElements() {
		return elements;
	}

	/**
	 * Returns the list of microcontrollers
	 */
	public ArrayList<Microcontroller> getControllers() {
		return controllers;
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
	}

	/**
	 * Adds a list of microcontrollers to the existing microcontroller list
	 * @param controllerList The list of controllers to add.
	 */
	public void addController(Microcontroller... controllerList) {
		for (Microcontroller controller : controllerList) {
			controllers.add(controller);
		}
	}

	public PersistentSettings getLocation() {
		return location;
	}

	/**
	 * Set the location to be this file and save it
	 * @param f The new file to save to
	 */
	public void saveLocationToFile(File f) {
		location = new PersistentSettings(f);
		saveLocationFile();
	}

	/**
	 * Save the location file
	 */
	public void saveLocationFile() {
		updateElementsInSettings();
		try {
			location.save();
		} catch (IOException e) {
			System.err.println("Error writing location file!");
			e.printStackTrace();
		}
	}

	/**
	 * Have all elements save their data in their respective SectionSettings
	 * objects
	 */
	public void updateElementsInSettings() {
		SectionSettings elementSettings = location.getSettingsForSection("elements");
		for (int a = 0; a < elements.size(); a++) {
			ElementController element = elements.get(a);

			// Add the element to the list of all elements
			elementSettings.put("element" + a, element.getInternalID());

			// Save the element's data
			element.saveData(location.getSettingsForSection(element.getInternalID()));
		}
	}
}
