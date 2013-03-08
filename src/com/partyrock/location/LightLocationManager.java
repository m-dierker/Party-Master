package com.partyrock.location;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import com.partyrock.LightMaster;
import com.partyrock.comm.uc.LocalArduino;
import com.partyrock.comm.uc.Microcontroller;
import com.partyrock.element.ElementController;
import com.partyrock.element.ElementSimulator;
import com.partyrock.element.blink.BlinkController;
import com.partyrock.element.lasers.LaserController;
import com.partyrock.element.led.LEDPanelController;
import com.partyrock.element.lights.LightController;
import com.partyrock.settings.PersistentSettings;
import com.partyrock.settings.SectionSettings;
import com.partyrock.settings.SettingsUpdateListener;
import com.partyrock.tools.PartyToolkit;

/**
 * Manages importing and saving the location file
 * 
 * @author Matthew
 * 
 */
public class LightLocationManager implements SettingsUpdateListener {
    private LightMaster master;
    private PersistentSettings location;
    private boolean unsavedChanges = false;

    public LightLocationManager(LightMaster master) {
        this.master = master;
    }

    /**
     * Returns the location file, or null if it doesn't exist
     */
    public PersistentSettings getLocation() {
        return location;
    }

    /**
     * Set the location to be this file and save it
     * 
     * @param f The new file to save to
     */
    public void saveLocationToFile(File f) {
        location = new PersistentSettings(f);
        location.addSettingsUpdateListener(this);
        saveLocationFile();
    }

    /**
     * Save the location file
     */
    public void saveLocationFile() {
        // Make sure that the settings file contains the latest data
        updateElementsInSettings();
        updateControllersInSettings();

        try {
            location.save();
            unsavedChanges = false;
        } catch (IOException e) {
            System.err.println("Error writing location file!");
            e.printStackTrace();
        }
    }

    public void updateControllersInSettings() {
        SectionSettings controllerSettings = location.getSettingsForSection("microcontrollers");
        controllerSettings.clear();
        ArrayList<Microcontroller> controllers = master.getControllers();

        for (int a = 0; a < controllers.size(); a++) {
            Microcontroller controller = controllers.get(a);
            controllerSettings.put("controller" + a, controller.getInternalID());
            controller.saveData(location.getSettingsForSection(controller.getInternalID()));
        }

    }

    /**
     * Have all elements save their data in their respective SectionSettings objects
     */
    public void updateElementsInSettings() {
        SectionSettings elementSettings = location.getSettingsForSection("elements");
        elementSettings.clear();
        ArrayList<ElementController> elements = master.getElements();
        for (int a = 0; a < elements.size(); a++) {
            ElementController element = elements.get(a);

            // Add the element to the list of all elements
            // This is sketchy and makes me worried
            elementSettings.put("element" + a, element.getInternalID());

            // Update the element's data in settings
            updateElementInSettings(element);
        }
    }

    /**
     * Updates a single ElementController's settings. This is mostly separated because saving the data triggers unsaved
     * changes. That said, this could possibly trigger some weirdness where the element is saved but isn't in the
     * element list (this should only happen if the program crashes)
     * 
     * @param element The element to save
     */
    public void updateElementInSettings(ElementController element) {
        // Save the element's data
        element.saveData(location.getSettingsForSection(element.getInternalID()));
    }

    /**
     * Loads a location file from a given file, and loads the elements inside the LightMaster
     * 
     * @param f the file to load from
     */
    public void loadLocation(File f) {
        attemptToSave();

        location = new PersistentSettings(f);
        location.addSettingsUpdateListener(this);

        // Load microcontrollers
        SectionSettings controllers = location.getSettingsForSection("microcontrollers");
        if (controllers != null) {
            Set<String> controllerNames = controllers.keySet();

            if (controllerNames != null) {
                for (String controllerName : controllerNames) {
                    addControllerFromSettings(location.getSettingsForSection(controllers.get(controllerName)));
                }
            }
        }

        // Load the special elements section
        SectionSettings elements = location.getSettingsForSection("elements");
        Set<String> elementNames = elements.keySet();

        for (String element : elementNames) {
            addElementFromSettings(location.getSettingsForSection(elements.get(element)));
        }

        // Update the element list in any GUI windows that are open
        master.getWindowManager().updateElements();
    }

    public void addControllerFromSettings(SectionSettings settings) {
        String className = settings.get("mc_class");
        if (className.equals("LocalArduino")) {
            LocalArduino ard = new LocalArduino(settings.get("mc_name"), settings.getSectionName(),
                    settings.get("ard_port"));
            master.addController(ard);
        }

    }

    /**
     * If a location file exists, will prompt the user if it should be saved if it is not currently updated
     */
    public void attemptToSave() {
        if (location != null && unsavedChanges) {
            boolean save = PartyToolkit.openQuestion(master.getWindowManager().getMain().getShell(),
                    "There are unsaved changes to location file " + location.getFile().getName()
                            + ". Would you like to save the changes?", "Save Location File?");
            if (save) {
                saveLocationFile();
            }
        }
    }

    /**
     * Imports an element from the settings file
     * 
     * @param settings The specific details about an element
     */
    public void addElementFromSettings(SectionSettings settings) {
        String elementName = settings.getSectionName();
        // For an element, the first two characters describe the element type
        String elementType = elementName.substring(0, 2);

        ElementController controller = null;
        String name = settings.get("name");
        String id = settings.get("id");
        String internalID = elementName;

        // Load your element type here
        if (elementType.equals("bl")) {
            controller = new BlinkController(master, internalID, name, id);
        } else if (elementType.equals("lp")) {
            int width = Integer.parseInt(settings.get("panel_width"));
            int height = Integer.parseInt(settings.get("panel_height"));
            controller = new LEDPanelController(master, internalID, name, id, width, height);
        } else if (elementType.equals("lz")) {
            controller = new LaserController(master, internalID, name, id);
        } else if (elementType.equals("li")) {
            controller = new LightController(master, internalID, name, id);
        }

        // Load the microcontroller if it exists
        String mcName;
        if ((mcName = settings.get("mc")) != null) {
            Microcontroller mc = findMicrocontrollerByName(mcName);
            if (mc != null) {
                controller.getExecutor().setMicrocontroller(mc);
            } else {
                System.err.println("Warning: Cannot find a microcontroller with name " + mcName);
            }
        }

        applySimulatorSettingsToElement(controller, settings);

        if (controller != null) {
            master.addElement(controller);
        } else {
            System.err.println("Controller not constructed while loading location: " + internalID);
        }
    }

    /**
     * Applies simulator settings to an element if possible. We just call this even if it might not work, so we have to
     * handle cases like a null strings and such.
     * 
     * @param controller The controller to add the settings to
     * @param settings The container possibly containing the simulator settings
     */
    public void applySimulatorSettingsToElement(ElementController controller, SectionSettings settings) {
        if (controller == null) {
            return;
        }

        ElementSimulator simulator = controller.getSimulator();

        // X
        String val;
        if ((val = settings.get("simulator_x")) != null) {
            simulator.setX(Integer.parseInt(val));
        }

        // Y
        if ((val = settings.get("simulator_y")) != null) {
            simulator.setY(Integer.parseInt(val));
        }

        // Collapsed
        if ((val = settings.get("simulator_collapsed")) != null) {
            simulator.setCollapsed(val.equals("true"));
        }
    }

    public Microcontroller findMicrocontrollerByName(String name) {
        for (Microcontroller mc : master.getControllers()) {
            if (mc.getName().equals(name)) {
                return mc;
            }
        }

        return null;
    }

    /**
     * Loads location.loc if it exists This is for debugging purposes so we don't create new elements every time when
     * not messing with new elements
     */
    public void loadNormalLocFile() {
        File normalLoc = new File("location.loc");
        if (normalLoc.exists()) {
            this.loadLocation(normalLoc);
        } else {
            System.out
                    .println("Note: Trying to load the normal location.loc file, but it doesn't exist. Continuing to run normally...");
        }

    }

    /**
     * When the settings have changed, mark that we have unsaved changes
     */
    @Override
    public void onSettingsChange() {
        unsavedChanges = true;
    }

    /**
     * This is called in various places to indicate that something changed
     */
    public void unsavedChanges() {
        unsavedChanges = true;
    }

    /**
     * Removes the element's section settings
     * 
     * @param victim
     */
    public void removeElement(ElementController victim) {
        if (location == null) {
            return;
        }

        location.getSettingsForSection(victim.getInternalID()).clear();
    }

    public ElementController getElementByInternalID(String id) {
        // This is slow, could use HashMap to fix later if I have to but it's just saving/loading so it shouldn't matter
        for (ElementController element : master.getElements()) {
            if (element.getInternalID().equals(id)) {
                return element;
            }
        }

        return null;
    }
}
