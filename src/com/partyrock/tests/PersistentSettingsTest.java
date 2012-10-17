package com.partyrock.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.partyrock.tools.Stopwatch;
import comm.partyrock.settings.PersistentSettings;
import comm.partyrock.settings.SectionSettings;

public class PersistentSettingsTest {

	private File file;
	private PersistentSettings settings;

	@Before
	public void setUp() throws Exception {
		file = new File("unit-tests/settingsTest.ini");
		if (file.exists()) {
			file.delete();
		}
		file.createNewFile();
		settings = new PersistentSettings(file);

	}

	@Test
	public void testSettings() {
		Stopwatch watch = new Stopwatch();
		watch.start();

		// test basic writes
		SectionSettings settings = this.settings.getSettingsForSection("section1");
		settings.put("stringKey", "stringValue");
		settings.put("intValue", 5);
		settings.put("doubleValue", 5.01);

		// test multiline writes
		settings = this.settings.getSettingsForSection("sec2");
		String multilineString = "this\nis\na\nmulti\nline\nstring";
		settings.put("stringKey", multilineString);

		// If this goes above ~100000 (on Matthew's laptop), it causes a Java
		// out of memory error (we overflowed the heap).
		int numLoops = 10000;
		// test a lot of writes
		settings = this.settings.getSettingsForSection("longsec");
		for (int a = 0; a < numLoops; a++) {
			if (a % 1000 == 0)
				System.out.println(a);
			switch (a % 3) {
			case 0:
				settings.put("num" + a, a + "");
				break;
			case 1:
				settings.put(a + "", a);
				break;
			case 2:
				settings.put("num" + a, a + .49999999);
				break;
			}
		}

		System.out.println("Writing settings");

		try {
			settings.writeSettings();
		} catch (IOException e) {
			fail("Error writing the settings file");
			e.printStackTrace();
		}

		watch.end();

		System.out.println(watch + " to write");

		watch.start();

		// test basic reads
		PersistentSettings newSettings = new PersistentSettings(file);
		SectionSettings secSettings = newSettings.getSettingsForSection("section1");

		assertEquals(secSettings.get("stringKey"), "stringValue");
		assertEquals((int) secSettings.get("intValue", int.class), 5);
		assertEquals((double) secSettings.get("doubleValue", double.class), 5.01, 0);

		// test multiline read
		secSettings = newSettings.getSettingsForSection("sec2");
		assertEquals(secSettings.get("stringKey"), multilineString);

		// test lots of reads
		secSettings = this.settings.getSettingsForSection("longsec");
		// 1 billion load test
		for (int a = 0; a < numLoops; a++) {
			switch (a % 3) {
			case 0:
				assertEquals(secSettings.get("num" + a), a + "");
				break;
			case 1:
				assertEquals((int) secSettings.get(a + "", int.class), a);
				break;
			case 2:
				assertEquals((double) secSettings.get("num" + a, double.class), a + .49999999, 0);
				break;
			}
		}

		System.out.println(watch + " to read");

	}
}
