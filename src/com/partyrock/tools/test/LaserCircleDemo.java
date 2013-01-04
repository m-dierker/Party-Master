package com.partyrock.tools.test;

import com.partyrock.comm.serial.SerialCommunicator;
import com.partyrock.tools.PartyToolkit;

/**
 * This makes the laser go in a circle
 * @author Matthew
 * 
 */
public class LaserCircleDemo {
	public static void main(String... args) {
		String port = "/dev/tty.usbserial-A900adQf";
		SerialCommunicator comm = new SerialCommunicator(port);
		comm.connect();
		System.out.println("starting");

		int delay = 700;
		PartyToolkit.delay(2000);

		double t = 0;
		while (true) {
			int motor1 = (int) (90 + 10 * Math.cos(t));
			int motor2 = (int) (90 + 10 * Math.sin(t));
//			int motor1 = (int) (90 + 20 * Math.cos(3 * t));
//			int motor2 = (int) (90 + 20 * Math.sin(5 * t));

			double n = 3;
			double d = 1;
//			int motor1 = (int) (90 + 20 * Math.cos(n / d * t) * Math.cos(t));
//			int motor2 = (int) (90 + 20 * Math.cos(n / d * t) * Math.sin(t));

			String msg = "";
			msg += getMessage(1, motor1);
			msg += getMessage(2, motor2);
			comm.sendMsg(msg);
			System.out.println(motor1 + ", " + motor2);
			System.out.println(msg);

			t += Math.PI / 8;
			t %= 2 * Math.PI;
			PartyToolkit.delay(30);

		}
	}

	public static String getMessage(int motor, int deg) {
		String msg = "." + motor;
		int tens = deg / 10;
		msg += (char) (tens + 'A');
		msg += "" + deg % 10;
		return msg;
	}
}
