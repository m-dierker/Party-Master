package com.partyrock.temp;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * This is a quick file for testing whatever, including that the git repository
 * is working correctly. Feel free to test anything in this file, and commit
 * whatever.
 * @author Matthew
 */
public class Temp {
	public static void main(String... args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.open();
		// Create and check the event loop
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}
