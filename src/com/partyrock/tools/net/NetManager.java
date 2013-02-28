package com.partyrock.tools.net;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Scanner;

/**
 * Has some convience methods for hitting URLs
 * @author Matthew
 * 
 */
public class NetManager {
	/**
	 * Accesses a URL
	 * @param url the URL to get
	 * @return the response
	 */
	public static String get(String url) {
		try {
			Scanner in = new Scanner(new URI(url).toURL().openStream());
			String ret = "";
			while (in.hasNext()) {
				ret += in.nextLine();
			}
			in.close();
			return ret;
		} catch (Exception e) {
			System.err.println("Error accessing a URL: " + url);
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Downloads a URL to a given file
	 * @param url the URL to download
	 * @param file the File to download to
	 */
	public static void downloadURLToFile(String url, File file) {
		URL website;
		try {
			website = new URI(url).toURL();
			ReadableByteChannel rbc = Channels.newChannel(website.openStream());
			FileOutputStream fos = new FileOutputStream(file);
			fos.getChannel().transferFrom(rbc, 0, 1 << 24);
			fos.close();
		} catch (Exception e) {
			System.out.println("Error reading url " + url);
			e.printStackTrace();
		}
	}
}
