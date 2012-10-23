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
 * Basic test of SWT that will develop over time as I figure out how GUI works.
 * @author Matthew
 */
public class GUITest {

	public static void main(String... args) {

		// init stuff
		final Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Custom gradient selection for Table");
		shell.setLayout(new FillLayout());

		// make the table
		final Table table = new Table(shell, SWT.MULTI | SWT.FULL_SELECTION);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		int columnCount = 3;

		// make each column
		for (int i = 0; i < columnCount; i++) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText("Column " + i);
		}

		// make items
		int itemCount = 8;
		for (int i = 0; i < itemCount; i++) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(new String[] { "item " + i + " a", "item " + i + " b",
					"item " + i + " c" });
		}
		/*
		 * NOTE: MeasureItem, PaintItem and EraseItem are called repeatedly.
		 * Therefore, it is critical for performance that these methods be as
		 * efficient as possible.
		 */
		table.addListener(SWT.EraseItem, new Listener() {
			public void handleEvent(Event event) {

				System.out.println(event);

				// Make the event not "hot" which means the mouse hover
				// background will not be drawn
				event.detail &= ~SWT.HOT;
				// If the cell is selected
				if ((event.detail & SWT.SELECTED) != 0) {
					GC gc = event.gc;
					Rectangle area = table.getClientArea();
					/*
					 * If you wish to paint the selection beyond the end of last
					 * column, you must change the clipping region.
					 */
					int columnCount = table.getColumnCount();

					// last COLUMN, not row, or no columns
					System.out.println("Index: " + event.index);
					// updates the clipping when rendering the third column to
					// extend beyond the end of the third column (as it would
					// normally be clipped short)
					if (event.index == columnCount - 1 || columnCount == 0) {
						int width = area.x + area.width - event.x;
						if (width > 0) {
							Region region = new Region();
							gc.getClipping(region);
							region.add(event.x, event.y, width, event.height);
							gc.setClipping(region);
							region.dispose();
						}
					}
					// uses advanced graphics
					gc.setAdvanced(true);
					// if advanced graphics are on (and they may or may not be,
					// set the alpha)
					if (gc.getAdvanced()) {
						gc.setAlpha(127);
					}
					// Get the rectangle specified by the event
					Rectangle rect = event.getBounds();
					Color foreground = gc.getForeground();
					Color background = gc.getBackground();
					// set colors to our custom colors
					gc.setForeground(display.getSystemColor(SWT.COLOR_RED));
					gc.setBackground(display.getSystemColor(SWT.COLOR_LIST_BACKGROUND));
					System.out.println("Drawing from (0, " + rect.y + ") to ("
							+ area.width + ", " + rect.height + ")");
					gc.fillGradientRectangle(0, rect.y, area.width, rect.height, false);
					// restore colors for subsequent drawing
					gc.setForeground(foreground);
					gc.setBackground(background);
					event.detail &= ~SWT.SELECTED;
				}
			}
		});
		for (int i = 0; i < columnCount; i++) {
			table.getColumn(i).pack();
		}
		table.setSelection(table.getItem(0));
		shell.setSize(500, 200);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}
