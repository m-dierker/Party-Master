package com.partyrock.temp;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Basic test of SWT that will develop over time as I figure out how GUI works.
 * @author Matthew
 */
public class GUITest {
	public static void main(String... args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
}
