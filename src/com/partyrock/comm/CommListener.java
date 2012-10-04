package com.partyrock.comm;

/**
 * Defines a listener for incoming communication
 * @author Matthew
 */
public interface CommListener {

	/**
	 * Called when a message is received from an input stream
	 * @param msg The message received
	 */
	public void onCommMessage(String msg);
}
