package com.partyrock.id;

import java.util.HashSet;

/**
 * Manages internal IDs for things like microcontrollers, and animations
 * @author Matthew
 * 
 */
public class ID {
	private static HashSet<String> ids = new HashSet<String>();

	/**
	 * See if a given ID exists
	 * @return Whether the ID exists
	 */
	public static boolean hasID(String id) {
		return ids.contains(id);
	}

	/**
	 * Adds an ID to the database of IDs
	 * @param id The ID to check
	 */
	public static void addID(String id) {
		ids.add(id);
	}

	/**
	 * Generates a new ID (ex: prefix "m" for "microcontroller" might give
	 * something like m2398723). The ID is automatically added.
	 * @param prefix The prefix for the ID
	 * @return the new ID
	 */
	public static String genID(String prefix) {
		while (true) {
			int rand = (int) (Math.random() * Integer.MAX_VALUE);
			String id = prefix + rand;
			if (!ids.contains(id)) {
				addID(id);
				return id;
			}
		}
	}
}
