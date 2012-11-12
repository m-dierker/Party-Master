package com.partyrock.temp;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

/**
 * SWT has a rendering bug I'm trying to report
 * @author Matthew
 * 
 */
public class SWTBugRepro {

	private final Display display;
	private Shell shell;
	private int CHANNEL_WIDTH = 700;

	public SWTBugRepro() {
		this.display = new Display();
		this.shell = new Shell(display);
		shell.setText("Sample Party Rock GUI");
		shell.setLayout(new FillLayout());

		final Table table = new Table(shell, SWT.MULTI | SWT.FULL_SELECTION);
		table.setHeaderVisible(false);
		table.setLinesVisible(false);
		table.setToolTipText("");

		int columnCount = 2;
		for (int a = 0; a < columnCount; a++) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText("Column");
		}

		int rowCount = 20;
		for (int i = 0; i < rowCount; i++) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(new String[] { (i < rowCount / 2) ? "Lights " + i
					: ("Laser " + (i - rowCount / 2)) });
		}

		table.addListener(SWT.MeasureItem, new Listener() {
			public void handleEvent(Event event) {
				// Custom height
				event.height = 50;
				if (event.index == 1) {
					// custom width for the right column
					event.width = CHANNEL_WIDTH;
				}
			}
		});

		table.addListener(SWT.EraseItem, new Listener() {
			public void handleEvent(Event event) {

				event.detail &= ~SWT.HOT;

				// get the color based on if it's selected
				Color channelColor = ((event.detail & SWT.SELECTED) != 0) ? display.getSystemColor(SWT.COLOR_BLUE)
						: display.getSystemColor(SWT.COLOR_RED);

				GC gc = event.gc;

				// Area of the entire visible table
				Rectangle area = table.getClientArea();

				// Clipping
				int width = area.x + area.width - event.x;

				Region region = new Region();
				gc.getClipping(region);
				region.add(event.x, event.y, width, event.height);
				gc.setClipping(region);
				region.dispose();

				Rectangle rect = event.getBounds();
				Color origBack = gc.getBackground();

				// start rendering

				if (event.index == 1) {
					gc.setBackground(channelColor);
					gc.fillRectangle(event.x, rect.y, CHANNEL_WIDTH, rect.height);
				}

				gc.setForeground(display.getSystemColor(event.index == 0 ? SWT.COLOR_BLACK
						: SWT.COLOR_GREEN));
				gc.drawLine(event.x, rect.y + rect.height - 1, event.x
						+ CHANNEL_WIDTH, rect.y + rect.height - 1);

				gc.setForeground(display.getSystemColor(SWT.COLOR_DARK_RED));

				// end rendering

				gc.setBackground(origBack);

				event.detail &= ~SWT.SELECTED;
			}
		});

		for (int i = 0; i < table.getColumnCount(); i++) {
			table.getColumn(i).pack();
		}

		table.setSelection(table.getItem(0));
		shell.setSize(700, 500);

		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		display.dispose();

	}

	public static void main(String... args) {
		new SWTBugRepro();
	}
}