package com.partyrock.temp;

import java.io.File;

import com.partyrock.settings.Saver;

/**
 * This is a quick file for testing whatever, including that the git repository is working correctly. Feel free to test
 * anything in this file.
 * 
 * @author Matthew
 */
public class Temp {
    public static void main(String... args) throws Exception {
        File file = new File("music/i_knew_you_were_trouble.mp3");
        Saver.saveLocalFile(file);

    }
}
