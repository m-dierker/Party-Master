package comm.partyrock.settings;

import java.io.IOException;

/**
 * SectionSettings will actually write settings for a given section name in the
 * settings file
 * @author Matthew
 * 
 */
public class SectionSettings {
	private PersistentSettings settings;
	private String sectionName;

	public SectionSettings(PersistentSettings settings, String sectionName) {
		this.settings = settings;
		this.sectionName = sectionName;
	}

	/**
	 * Puts the key/value pair in the settings file for the given section name
	 * @param key the setting key
	 * @param value The value
	 */
	public void put(String key, Object value) {
		settings.put(sectionName, key, value);
	}

	/**
	 * Returns the string value for a given key
	 * @param key The key
	 * @return The value
	 */
	public String get(String key) {
		return settings.get(sectionName, key);
	}

	/**
	 * Returns the value for a given key with the given class type
	 * @param key The key
	 * @param c The class type to return as
	 * @return The value
	 */
	public <T> T get(String key, Class<T> c) {
		return settings.get(sectionName, key, c);
	}

	/**
	 * Stores the settings
	 * @throws IOException Exception that may have occurred during writing
	 */
	public void writeSettings() throws IOException {
		this.settings.writeSettings();
	}
}
