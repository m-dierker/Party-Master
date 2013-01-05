package com.partyrock.tools.net;

import java.net.URI;
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
}
