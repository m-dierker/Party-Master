package com.partyrock.comm.uc;

import com.partyrock.comm.Communicator;

/**
 * Wrapper for a Microcontroller (uC) - This could include a remote arduino, or
 * a local arduino, which ideally can be handled the same on this end
 * @author Matthew
 * 
 */
public abstract class Microcontroller {
	private String name;

	public Microcontroller(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
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
	 * @param port The port to connect to
	 */
	public abstract void establishNewConnectionToPort(String port);
}
