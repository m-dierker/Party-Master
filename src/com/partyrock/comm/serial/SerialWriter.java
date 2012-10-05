package com.partyrock.comm.serial;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Vector;

/**
 * Writes messages out for serial communication
 * @author Matthew
 */
public class SerialWriter implements Runnable {

	private SerialCommunicator comm;
	private OutputStream out;
	private boolean shouldDie;
	/**
	 * The messages we need to send. Yay threadsafe!
	 */
	private Vector<String> messages;

	public SerialWriter(SerialCommunicator comm, OutputStream out) {
		this.comm = comm;
		this.out = out;

		messages = new Vector<String>();
	}

	/**
	 * Run the writer in a separate Thread, and actually write messages
	 */
	public void run() {
		String msg = null;
		while (!shouldDie) {
			try {
				if (messages.size() > 0) {
					msg = messages.remove(0);
					this.out.write(msg.getBytes());
				}
			} catch (IOException e) {
				// TODO (Matthew): Add automatic disconnection/reconnection for
				// when messages can't send.
				System.out.println("Unable to send message: " + msg);
				e.printStackTrace();
			}
		}
	}

	/**
	 * Queues the message to be sent out the serial port
	 * @param msg The message to be sent
	 */
	public void sendMsg(String msg) {
		messages.add(msg);
	}

	/**
	 * Should kill the writer's thread relatively rapidly
	 */
	public void die() {
		shouldDie = true;
	}
}
