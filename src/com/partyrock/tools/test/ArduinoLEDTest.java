package com.partyrock.tools.test;

import java.util.Arrays;

import com.partyrock.comm.CommListener;
import com.partyrock.comm.Communicator;
import com.partyrock.comm.serial.SerialCommunicator;

/**
 * Tests the Communicator infrastructure for SerialCommunicator (since this
 * can't be automated). The light will start flashing with a 1000ms delay, and
 * this gets repeatedly divided by two (just to see how fast we can go)
 * @author Matthew
 */
public class ArduinoLEDTest implements CommListener {

	public ArduinoLEDTest() throws Exception {
		Communicator serial = new SerialCommunicator("/dev/tty.usbmodemfa131");
		serial.connect();
		serial.addListener(this);

		int delay = 1000; // delay in ms
		while (true) {
			for (int a = 0; a < 4; a++) {
				serial.sendMsg("1");
				System.out.println("on");
				Thread.sleep(delay);
				serial.sendMsg("0");
				System.out.println("off");
				Thread.sleep(delay);
			}
			if (delay >= 100) {
				delay /= 2;
			}
		}
	}

	@Override
	/**
	 * Prints out the message when it's received
	 */
	public void onCommMessage(String msg) {
		System.out.println("<-- " + msg + " --- "
				+ Arrays.toString(msg.getBytes()));
	}

	public static void main(String... args) throws Exception {
		new ArduinoLEDTest();
	}
}
