package com.partyrock.music;

import com.partyrock.LightMaster;

/**
 * This class is in charge of keeping track of the MP3(s) for the show, flipping
 * between them, etc.
 * 
 * @author Matthew
 * 
 */
public class LightMusicManager {
	private LightMaster master;
	private MP3 currentSong;

	// Possibly need an ArrayList of songs here or something like that

	public LightMusicManager(LightMaster master) {
		this.master = master;
		currentSong = null;
	}

	/**
	 * Returns the duration of the current song, or -1 if no song has been
	 * loaded
	 * @return The duration of the current song if possible, -1 if not
	 */
	public double getSongLength() {
		if (currentSong == null) {
			return -1;
		}

		return currentSong.getDuration();
	}
}
