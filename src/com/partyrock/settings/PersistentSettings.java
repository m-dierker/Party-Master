package com.partyrock.settings;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;
import org.ini4j.Wini;

import com.partyrock.system.OSDetector;

/**
 * Settings will be automatically backed by an INI file. This class handles
 * getting the settings, writing the changes, backups, etc. Each instance of the
 * application will need a PersistentSettings object for each INI file, and
 * should assume that once things are written to PersistentSettings that it will
 * be written to the settings file. Keep in mind there may be more than one
 * program running, so writing things from both programs could be bad could be
 * bad. Also possibly added in the future: detection for when the settings file
 * has changed, and loading that in automatically
 * 
 * A PersistentSettings object manages multiple "sections" of the settings file.
 * When a given class wants to actually write a setting, it should call
 * getSettingsForSection() with a unique section name, and use the given
 * SectionSettings to actually write the settings. This is because it's a lot
 * easier to deal with duplicate section names than it is to deal with duplicate
 * setting names.
 * 
 * Note that an INI file may not have the extension .ini
 * 
 * For more information on the ini4j library we're using, see their website:
 * http://ini4j.sourceforge.net/tutorial/IniTutorial.java.html
 * @author Matthew
 */
public class PersistentSettings {
	private Ini ini;
	/**
	 * Maps a given section name to a given SectionSettings object
	 */
	private HashMap<String, SectionSettings> sectionSettings;

	private ArrayList<SettingsUpdateListener> updateListeners;

	public PersistentSettings(File f) {

		updateListeners = new ArrayList<SettingsUpdateListener>();

		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				System.err.println("Error creating file used by PersistentSettings");
				e.printStackTrace();
			}
		}

		sectionSettings = new HashMap<String, SectionSettings>();
		try {
			if (OSDetector.isWindows()) {
				ini = new Wini(f);
			} else {
				ini = new Ini(f);
			}
		} catch (InvalidFileFormatException e) {
			System.out.println("Invalid settings file specified" + f);
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IOException reading the settings file " + f);
			e.printStackTrace();
		}
	}

	/**
	 * Called to wrap ini4j's method of actually putting objects in the ini file
	 * @param section The section name
	 * @param key The setting's key
	 * @param value The value to put in
	 */
	protected void put(String section, String key, Object value) {
		ini.put(section, key, value);
		updateSettingsListeners();
	}

	/**
	 * Returns the string value for a given key
	 * @param section The sectionName for the option
	 * @param key The key
	 * @return The value
	 */
	protected String get(String section, String key) {
		return ini.get(section, key);
	}

	/**
	 * Returns the value for a given key with the given class type
	 * @param section The sectionName for the option
	 * @param key The key
	 * @param c The class to return as (ex: double.class)
	 * @return The value
	 */
	protected <T> T get(String section, String key, Class<T> c) {
		return ini.get(section, key, c);
	}

	/**
	 * Returns all keys for a given section
	 * @param section The section to get the keys for
	 * @return A set of all keys in a given section
	 */
	protected Set<String> keySetForSection(String section) {
		return ini.getAll(section).get(0).keySet();
	}

	/**
	 * Stores the settings
	 * @throws IOException if ini.store() produces an error
	 */
	public void save() throws IOException {
		ini.store();
	}

	/**
	 * Gets the SectionSettings writer for a given section name
	 * @param sectionName The section name
	 * @return The SectionSettings object that should be used to write the file
	 */
	public SectionSettings getSettingsForSection(String sectionName) {
		if (!this.sectionSettings.containsKey(sectionName)) {
			this.sectionSettings.put(sectionName, new SectionSettings(this, sectionName));
		}

		return this.sectionSettings.get(sectionName);
	}

	public void addSettingsUpdateListener(SettingsUpdateListener listener) {
		updateListeners.add(listener);
	}

	/**
	 * Notifies all settings update listeners that there was a change
	 */
	protected void updateSettingsListeners() {
		for (SettingsUpdateListener listener : updateListeners) {
			listener.onSettingsChange();
		}
	}

	public File getFile() {
		return ini.getFile();
	}
}
