package com.partyrock.temp;

import java.io.File;

import org.ini4j.Ini;

/**
 * This is a quick file for testing whatever, including that the git repository
 * is working correctly. Feel free to test anything in this file.
 * @author Matthew
 */
public class Temp {
	public static void main(String... args) throws Exception {
		File f = new File("temp.ini");
		f.createNewFile();

		Ini ini = new Ini(f);

		ini.put("sec1", "key1", "val1");
		ini.put("sec2", "key1", "val2");

		ini.store();

		System.out.println(ini.getAll("sec1").get(0));

	}
}
