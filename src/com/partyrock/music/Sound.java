package com.partyrock.music;

/**
 * An abstraction for playing/pausing a song
 * @author Matthew
 * 
 */
public abstract class Sound {

	/**
	 * Plays a given sound from the start
	 */
	public void play() {
		this.play(0);
	}

	/**
	 * Play from a given start time
	 * @param startTime The time in seconds to start from
	 */
	public abstract void play(double startTime);

	/**
	 * Returns the duration of the sound
	 * @return the sound's duration
	 */
	public abstract double getDuration();

	/**
	 * Pauses the sound
	 */
	public abstract void pause();

	/**
	 * Returns the current time in the sound. If it hasn't started playing yet,
	 * then this just returns 0
	 * @return Where we are in the sound
	 */
	public abstract double getCurrentTime();

}