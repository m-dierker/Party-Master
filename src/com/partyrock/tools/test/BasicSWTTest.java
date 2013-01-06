package com.partyrock.tools.test;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * A basic SWT test to make sure everything is working
 * @author Matthew
 * 
 */
public class BasicSWTTest {
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
