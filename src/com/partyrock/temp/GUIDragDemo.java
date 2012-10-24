package com.partyrock.temp;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class GUIDragDemo {
	static TableItem itemBeingDragged;

	public static void main(String[] args) {
		final int COLUMNCOUNT = 4;
		final Display display = new Display();
		Shell shell = new Shell(display);
		shell.setBounds(10, 10, 400, 400);
		final Table table = new Table(shell, SWT.NONE);
		table.setBounds(10, 10, 350, 350);
		table.setHeaderVisible(true);
		/* create the columns */
		int columnWidth = table.getClientArea().width / COLUMNCOUNT;
		for (int i = 0; i < COLUMNCOUNT; i++) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText("Col " + i);
			column.setWidth(columnWidth);
		}
		for (int i = 0; i < 9; i++) { /* create the items */
			TableItem item = new TableItem(table, SWT.NONE);
			for (int j = 0; j < COLUMNCOUNT; j++) {
				item.setText(j, "item" + i + "-" + j);
			}
		}
		table.addListener(SWT.DragDetect, new Listener() {
			public void handleEvent(Event event) {
				TableItem item = table.getItem(new Point(event.x, event.y));
				if (item == null)
					return;
				itemBeingDragged = item;
			}
		});
		table.addListener(SWT.MouseMove, new Listener() {
			public void handleEvent(Event event) {
				if (itemBeingDragged == null)
					return;
				TableItem item = table.getItem(new Point(event.x, event.y));
//				table.setInsertMark(item, true);
			}
		});
		table.addListener(SWT.MouseUp, new Listener() {
			public void handleEvent(Event event) {
				if (itemBeingDragged == null)
					return;
				TableItem item = table.getItem(new Point(event.x, event.y));
				if (item != null && item != itemBeingDragged) {
					/* determine insertion index */
					TableItem[] items = table.getItems();
					int index = -1;
					for (int i = 0; i < items.length; i++) {
						if (items[i] == item) {
							index = i;
							break;
						}
					}
					if (index != -1) { /* always true in this trivial example */
						TableItem newItem = new TableItem(table, SWT.NONE, index);
						for (int i = 0; i < COLUMNCOUNT; i++) {
							newItem.setText(i, itemBeingDragged.getText(i));
						}
						itemBeingDragged.dispose();
						table.setSelection(new TableItem[] { newItem });
					}
				}
//				table.setInsertMark(null, false);
				itemBeingDragged = null;
			}
		});
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}
