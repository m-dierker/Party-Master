package com.partyrock.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.partyrock.LightMaster;
import com.partyrock.tools.PartyToolkit;

public class LightSimulator extends Shell {

	private LightWindow main;
	private LightMaster master;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Display display = Display.getDefault();
			LightSimulator shell = new LightSimulator(null);
			shell.open();
			shell.layout();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the shell.
	 * @param display
	 */
	public LightSimulator(final LightWindow window) {
		super(window.getDisplay(), SWT.SHELL_TRIM);

		final Display display = window.getDisplay();
		this.main = window;
		this.master = window.getMaster();
		setLayout(new FillLayout(SWT.HORIZONTAL));

		Canvas canvas = new Canvas(this, SWT.NONE);
		canvas.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				GC gc = e.gc;

				System.out.println("paint");

				gc.setBackground(new Color(display, (int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256)));
				gc.fillOval(100, 100, 100, 100);

				PartyToolkit.sleep(16);
			}
		});

		createContents();
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("Party Simulator");
		setSize(642, 412);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
