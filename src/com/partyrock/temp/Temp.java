package com.partyrock.temp;

import gnu.io.CommPortIdentifier;

import java.util.Enumeration;

/**
 * This is a quick file for testing whatever, including that the git repository is working correctly. Feel free to test
 * anything in this file.
 * 
 * @author Matthew
 */
public class Temp {
    public static void main(String... args) throws Exception {
        @SuppressWarnings("unchecked")
        Enumeration<CommPortIdentifier> x = CommPortIdentifier.getPortIdentifiers();
        while (x.hasMoreElements()) {
            CommPortIdentifier id = x.nextElement();
            System.out.println(id.getName());
        }

    }
}
