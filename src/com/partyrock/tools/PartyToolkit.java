package com.partyrock.tools;

public class PartyToolkit {
	/**
	 * Try catch wrapper for Thread.sleep
	 * @param ms ms to delay
	 */
	public static void delay(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
