package com.partyrock.tools.test;

import java.awt.Color;
import java.io.File;
import java.net.URL;
import java.util.Scanner;

import com.partyrock.music.MP3;

/**
 * Flashes the raspberry pi to the four chord song. There isn't anything
 * particularly special going on here. Just wanted it done.
 * @author Matthew
 * 
 */
public class RaspberryPiTest {
	public static void main(String... args) throws Exception {
		MP3 mp3 = new MP3(new File("music/four-chord-song.mp3"));

		mp3.play(60);

		// Code to find the beat
//		Scanner in = new Scanner(System.in);
//		long lastTime = System.currentTimeMillis();
//		while (true) {
//			in.nextLine();
//			System.out.println(System.currentTimeMillis() - lastTime);
//			lastTime = System.currentTimeMillis();
//		}

		// The beat to start flashing to. It's not perfect, but it's close.
		int beatTime = 530 * 2;
		Color[] colors = { Color.red, Color.blue, Color.green, Color.cyan, Color.magenta, Color.yellow };
		int count = 0;
		while (true) {
			long start = System.currentTimeMillis();
			setColor(colors[count++]);
			count %= colors.length;
			long delay = (start + beatTime) - System.currentTimeMillis();
			Thread.sleep(delay < 0 ? 0 : delay);
		}
	}

	public static void setColor(Color color) throws Exception {
		new Scanner(new URL("http://rasppi.dyndns-remote.com/rgb?r=" + color.getRed() + "&g=" + color.getGreen()
				+ "&b=" + color.getBlue()).openStream());
	}
}
