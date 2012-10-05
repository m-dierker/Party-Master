package com.partyrock.comm.serial;

import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * Reads messages in for serial communication. This is *event driven* although
 * RXTX does support direct reading too. Ideally, all we need is event based,
 * which will save processing time.
 * @author Matthew
 */
public class SerialReader implements SerialPortEventListener {

	private SerialCommunicator comm;
	private InputStream in;
	private byte[] buffer = new byte[1024];

	public SerialReader(SerialCommunicator comm, InputStream in) {
		this.comm = comm;
		this.in = in;
	}

	/**
	 * Called automatically when the Serial input has data to read
	 * @param e
	 */
	public void serialEvent(SerialPortEvent event) {
		int data;

		try {
			int len = 0;
			while ((data = in.read()) > -1) {
				if (data == '\n') {
					break;
				}
				buffer[len++] = (byte) data;
			}
			comm.onSerialMessage(new String(buffer, 0, len));
		} catch (IOException e) {
			System.out.println("Error reading input" + Arrays.toString(buffer));
			e.printStackTrace();
		}
	}
}
