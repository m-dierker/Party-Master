package com.partyrock.comm.uc;

import com.partyrock.comm.Communicator;
import com.partyrock.settings.SectionSettings;

/**
 * Wrapper for a Microcontroller (uC) - This could include a remote arduino, or a local arduino, which ideally can be
 * handled the same on this end
 * 
 * @author Matthew
 * 
 */
public abstract class Microcontroller {
    private String name;
    private String internalID;

    public Microcontroller(String name, String internalID) {
        this.name = name;
        this.internalID = internalID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the communicator to talk to the microcontroller
     */
    public abstract Communicator getCommunicator();

    /**
     * Gets the microcontroller type
     */
    public abstract MicrocontrollerType getType();

    /**
     * What port should be used in the microcontroller editor
     */
    public abstract String getPort();

    /**
     * The port was updated, so we should establish a new connection there
     * 
     * @param port The port to connect to
     */
    public abstract void establishNewConnectionToPort(String port);

    public void sendMsg(String msg) {
        getCommunicator().sendMsg(msg);
    }

    public String getInternalID() {
        return internalID;
    }

    public void saveData(SectionSettings settings) {
        settings.put("mc_name", name);
        saveChildData(settings);
    }

    /**
     * Saves child data, including class information and such
     * 
     * @param settings
     */
    public abstract void saveChildData(SectionSettings settings);
}
