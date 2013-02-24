package com.partyrock;

import java.util.ArrayList;

import com.partyrock.comm.uc.Microcontroller;
import com.partyrock.element.ElementController;
import com.partyrock.gui.LightWindowManager;
import com.partyrock.location.LightLocationManager;
import com.partyrock.music.LightMusicManager;
import com.partyrock.show.ShowInfo;

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
	private ShowInfo show;
	private LightMusicManager musicManager;
	private LightLocationManager locationManager;

	public LightMaster(String... args) {

		elements = new ArrayList<ElementController>();
		controllers = new ArrayList<Microcontroller>();

		// Construct the music manager to manage the show's music
		musicManager = new LightMusicManager(this);

		// Construct the location manager to manage the show file
		locationManager = new LightLocationManager(this);

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

	public LightWindowManager getWindowManager() {
		return windowManager;
	}

	public ShowInfo getShow() {
		return show;
	}

	public LightMusicManager getMusicManager() {
		return musicManager;
	}

	public LightLocationManager getLocationManager() {
		return locationManager;
	}
}
