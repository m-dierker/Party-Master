package com.partyrock.comm;

import java.util.Vector;

/**
 * The basics for a class that communicates over some protocol with I/O, such as
 * serial. This keeps track of CommListeners, so we can notify another object
 * when a message is received
 * @author Matthew
 */
public abstract class Communicator {

	/**
	 * The list of all listeners this communicator should notify
	 */
	private Vector<CommListener> listeners;

	public Communicator() {
		listeners = new Vector<CommListener>();
	}

	public void addListener(CommListener listener) {
		listeners.add(listener);
	}

	/**
	 * Returns all listeners
	 * @return An ArrayList of all objects listening for communication
	 */
	public Vector<CommListener> getListeners() {
		return listeners;
	}

	/**
	 * Optional method for the communicator to connect to its given
	 * communication method. This is here so disconnect() can exist too.
	 * @return Returns true on success
	 */
	public boolean connect() {
		return true;
	}

	/**
	 * Optional method that can be implemented by a child to disconnect from a
	 * communication method
	 */
	public void disconnect() {

	}

	/**
	 * Optional method that should be implemented by any communicator that can
	 * send a message
	 * @param msg
	 */
	public void sendMsg(String msg) {
		System.out.println("This Communicator does not support sending messages: " + msg);
	}
}
