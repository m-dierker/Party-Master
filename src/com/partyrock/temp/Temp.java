package com.partyrock.temp;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import net.sf.image4j.codec.ico.ICODecoder;

/**
 * This is a quick file for testing whatever, including that the git repository
 * is working correctly. Feel free to test anything in this file.
 * @author Matthew
 */
public class Temp {
	public static void main(String... args) throws Exception {
		File file = new File("tmp-favicon.ico");
//		NetManager.downloadURLToFile("http://facebook.com/favicon.ico", file);
		List<BufferedImage> images = ICODecoder.read(file);
		System.out.println(images);
	}
}
