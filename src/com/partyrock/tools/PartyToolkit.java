package com.partyrock.tools;

import org.eclipse.swt.widgets.Shell;

import com.partyrock.gui.dialog.InputDialog;
import com.partyrock.gui.dialog.MessageDialog;

public class PartyToolkit {
	/**
	 * Try catch wrapper for Thread.sleep
	 * @param ms ms to delay
	 */
	public static void sleep(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static boolean openQuestion(Shell parent, String message, String title) {
		MessageDialog dialog = new MessageDialog(parent, message, title, new String[] { "Yes", "No" });
		return dialog.open();
	}

	public static boolean openConfirm(Shell parent, String message, String title) {
		MessageDialog dialog = new MessageDialog(parent, message, title);
		return dialog.open();
	}

	public static String openInput(Shell parent, String message, String title) {
		InputDialog dialog = new InputDialog(parent, message, title);
		return dialog.open();
	}
}
