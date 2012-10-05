package com.partyrock.comm.serial;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.TooManyListenersException;

import com.partyrock.comm.CommListener;
import com.partyrock.comm.Communicator;

/**
 * Communicates over the serial protocol
 * @author Matthew
 */
public class SerialCommunicator extends Communicator {

	private String portName;
	private SerialWriter writer;
	private SerialReader reader;

	/**
	 * Construct with a given Serial Port
	 */
	public SerialCommunicator(String port) {
		super();
		setPort(port);
	}

	/**
	 * Create a connection on the given port
	 */
	public boolean connect() {
		// Try to identify the given port name
		CommPortIdentifier portIdentifier;
		try {
			portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
		} catch (NoSuchPortException e1) {
			System.out.println("Error: No such SerialPort exists");
			return false;
		}

		// Check if it's currently in use
		if (portIdentifier.isCurrentlyOwned()) {
			System.out.println("Error: Port is currently in use");
			return false;
		}

		// Construct the CommPort
		CommPort commPort;
		try {
			commPort = portIdentifier.open(this.getClass().getName(), 2000);
		} catch (PortInUseException e) {
			System.out.println("Error: CommPort is currently in use");
			return false;
		}

		// Make sure it's the right type
		if (!(commPort instanceof SerialPort)) {
			System.out.println("Error: The selected port was invalid, and does not appear to be a SerialPort");
		}

		// Setup the SerialPort for communication
		SerialPort serialPort = (SerialPort) commPort;
		try {
			serialPort.setSerialPortParams(57600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
		} catch (UnsupportedCommOperationException e) {
			System.out.println("Error setting the SerialPort params");
			return false;
		}

		// Get the I/O streams, and construt the in and out readers
		try {
			InputStream in = serialPort.getInputStream();
			OutputStream out = serialPort.getOutputStream();

			writer = new SerialWriter(this, out);
			reader = new SerialReader(this, in);

			// Actually start up the writer.
			new Thread(writer).start();

			// Add the reader to be a listener on the port
			try {
				serialPort.addEventListener(reader);
				serialPort.notifyOnDataAvailable(true);
			} catch (TooManyListenersException e) {
				// This is silly exception
				System.out.println("The SerialComm port has too many listeners");
				return false;
			}

		} catch (IOException e) {
			System.out.println("Error reading the SerialPort's I/O streams");
			return false;
		}

		return true;
	}

	public void setPort(String port) {
		this.portName = port;
	}

	public String getPort() {
		return portName;
	}

	/**
	 * Called when a message has been read
	 * @param msg The received message
	 */
	public void onSerialMessage(String msg) {
		// Notify all listeners
		for (CommListener listener : getListeners()) {
			listener.onCommMessage(msg);
		}
	}
}
