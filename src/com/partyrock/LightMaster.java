package com.partyrock;

import java.io.File;
import java.util.ArrayList;

import com.partyrock.anim.LightAnimationManager;
import com.partyrock.comm.uc.Microcontroller;
import com.partyrock.element.ElementController;
import com.partyrock.gui.LightWindowManager;
import com.partyrock.location.LightLocationManager;
import com.partyrock.show.LightShowManager;

/**
 * LightMaster is, as the name implies, the master class. Everything starts here.
 * 
 * @author Matthew
 * 
 */
public class LightMaster {
    private LightWindowManager windowManager;
    private ArrayList<ElementController> elements;
    private ArrayList<Microcontroller> controllers;
    private LightShowManager showManager;
    private LightLocationManager locationManager;
    private LightAnimationManager animationManager;

    public LightMaster(String... args) {

        elements = new ArrayList<ElementController>();
        controllers = new ArrayList<Microcontroller>();

        // Construct the animation manager to manage the list of animations for
        // someone to select from
        animationManager = new LightAnimationManager(this);

        // Load the show manager to manage music, animations, etc.
        showManager = new LightShowManager(this);

        // Construct the location manager to manage the show file
        locationManager = new LightLocationManager(this);

        // Construct the GUI. This will make the main window.
        windowManager = new LightWindowManager(this);

        // Temporary development measure - Try to load the location.loc file if
        // it exists. This can be commented out for normal operation.
        // Must be done after everything else is loaded, but before the loop
        locationManager.loadNormalLocFile();
        showManager.loadMusic(new File("music/i_knew_you_were_trouble.mp3"));

        // This starts the GUI loop, so nothing should be called after this
        windowManager.loop();
    }

    public static void main(String... args) {
        new LightMaster(args);

        // If an MP3 was playing, for some reason the system doesn't automatically die (something to do with an apple
        // audio thread that doesn't die)
        // Therefore, we have to forcibly exit
        // This is annoying but it works
        System.exit(0);
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
     * 
     * @param elementList The list of elements to add
     */
    public void addElement(ElementController... elementList) {
        // Add all elements to the element list
        for (ElementController element : elementList) {
            elements.add(element);
        }
    }

    /**
     * Removes an element from the list
     * 
     * @param victim The element to remove
     */
    public void removeElement(ElementController victim) {
        elements.remove(victim);
        getLocationManager().removeElement(victim);
        getLocationManager().unsavedChanges();
    }

    /**
     * Adds a list of microcontrollers to the existing microcontroller list
     * 
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

    public LightShowManager getShowManager() {
        return showManager;
    }

    public LightLocationManager getLocationManager() {
        return locationManager;
    }

    public LightAnimationManager getAnimationManager() {
        return animationManager;
    }

    /**
     * Called when the main window is closed, and the system is going down
     */
    public void onDispose() {
        // Pause the music if it's playing
        showManager.shutdown();

        // Attempt to save the location file (probably by prompting the user)
        locationManager.attemptToSave();
    }
}
