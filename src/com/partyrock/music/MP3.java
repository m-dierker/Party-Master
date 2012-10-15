package com.partyrock.music;

import java.io.File;

public class MP3 extends Sound {

	private File file;

	/**
	 * Constructs an MP3 from a given file.
	 * @param file The file
	 */
	public MP3(File file) {
		super();
		this.file = file;
	}

	@Override
	public void play(double startTime) {
		// TODO Auto-generated method stub

	}

	@Override
	public double getDuration() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public double getCurrentTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * Returns the file associated with this MP3
	 * @return The mp3 file
	 */
	public File getFile() {
		return file;
	}
}
