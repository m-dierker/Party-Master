package com.partyrock.tools;

/**
 * Quick class to time code
 * @author Matthew
 * 
 */
public class Stopwatch {
	private long start;
	private long end;

	public Stopwatch() {

	}

	/**
	 * Starts the stopwatch
	 */
	public void start() {
		start = System.currentTimeMillis();
	}

	/**
	 * Ends the stopwatch
	 */
	public void end() {
		end = System.currentTimeMillis();
	}

	/**
	 * Returns the duration in milliseconds
	 * @return the duration in ms
	 */
	public long getDuration() {
		return end - start;
	}

	public String toString() {
		return getDuration() + "ms";
	}

}
