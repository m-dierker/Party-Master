package com.partyrock.temp;

import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

/**
 * Drag and drop demo without rewritten code
 * @author Matthew
 * 
 */
public class PartyRockDragDrop implements KeyListener {

	private final Display display;
	private Shell shell;
	private int CHANNEL_WIDTH = 700;
	private TableItem itemBeingDragged;
	private TableItem itemMarked;
	private Table table;
	private int[] colors = { SWT.COLOR_CYAN, SWT.COLOR_GREEN, SWT.COLOR_RED, SWT.COLOR_BLUE, SWT.COLOR_MAGENTA,
			SWT.COLOR_YELLOW };
	private HashMap<TableItem, Color> colorMap;

	public PartyRockDragDrop() {

		colorMap = new HashMap<TableItem, Color>();

		// Initialization stuff
		this.display = new Display();
		this.shell = new Shell(display);
		shell.setText("Sample Party Rock GUI");
		GridLayout layout = new GridLayout(1, false);
		shell.setLayout(layout);

		ToolBar bar = new ToolBar(shell, SWT.HORIZONTAL);
		for (int i = 0; i < 8; i++) {
			ToolItem item = new ToolItem(bar, SWT.PUSH);
			item.setText("Item " + i);
		}
		bar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		bar.pack();

		ScrolledComposite tableScroll = new ScrolledComposite(shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		tableScroll.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tableScroll.setExpandHorizontal(true);
		tableScroll.setExpandVertical(true);

		// Make the table
		table = new Table(tableScroll, SWT.MULTI | SWT.FULL_SELECTION);
		table.setHeaderVisible(false);
		table.setLinesVisible(false);
		table.setToolTipText("");
		table.addKeyListener(this);

		tableScroll.setContent(table);
//		table.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, true, false));

		// Construct columns (even though we don't see them)
		int columnCount = 2;
		for (int a = 0; a < columnCount; a++) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText("Column");
		}

		// Construct the rows
		int rowCount = 18;
		for (int i = 0; i < rowCount; i++) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(new String[] { (i < rowCount / 2) ? "Lights " + i : ("Laser " + (i - rowCount / 2)) });
		}

		// Set a custom height and width
		table.addListener(SWT.MeasureItem, new Listener() {
			public void handleEvent(Event event) {
				// Custom height
				event.height = 50;
				if (event.index == 1) {
					// Set the width of the render column
					event.width = CHANNEL_WIDTH;
				}
			}
		});

		// Render code!
		table.addListener(SWT.EraseItem, new Listener() {
			@SuppressWarnings("unused")
			public void handleEvent(Event event) {

				// Get the item that's being rendered
				TableItem item = (TableItem) event.item;

				event.detail &= ~SWT.HOT;

				// pick the color based on if the cell is selected
				Color channelColor = ((event.detail & SWT.SELECTED) != 0) ? display.getSystemColor(SWT.COLOR_BLUE)
						: display.getSystemColor(SWT.COLOR_RED);

				GC gc = event.gc;
				// Area of the entire visible table
				Rectangle area = table.getClientArea();

				// Clipping
				int width = area.x + area.width - event.x;

				if (width < 0) {
					// fix for drag and drop, where this can become < 0
					width = 0;
				}
				Region region = new Region();
				gc.getClipping(region);
				region.add(event.x, event.y, width, event.height + 1);
				gc.setClipping(region);
				region.dispose();

				Rectangle rect = event.getBounds();
				Color origBack = gc.getBackground();

				// start rendering

				if (event.index == 1) {
					Color color;
					if (colorMap.containsKey(item)) {
						color = colorMap.get(item);
					} else {
						color = display.getSystemColor(colors[(int) (Math.random() * colors.length)]);
						colorMap.put(item, color);
					}
					drawAnimation(gc, event.x, rect.y, CHANNEL_WIDTH, rect.height, color);
				}

				int separatorColor;
				if (itemMarked != null && table.indexOf(item) == table.indexOf(itemMarked) - 1) {
					// Hack for DnD, since the above line is over the
					// separator for some reason
					separatorColor = SWT.COLOR_CYAN;
				} else if (event.index == 0) {
					separatorColor = SWT.COLOR_BLACK;
				} else {
					separatorColor = SWT.COLOR_GREEN;
				}

				gc.setForeground(display.getSystemColor(separatorColor));
				gc.drawLine(event.x, rect.y + rect.height - 1, event.x + CHANNEL_WIDTH, rect.y + rect.height - 1);

				// Check if DnD is currently occuring
				if (item == itemMarked) {
					System.out.println("Rendering line");
					gc.setBackground(display.getSystemColor(SWT.COLOR_CYAN));
					gc.fillRectangle(event.x, rect.y, CHANNEL_WIDTH, 2);
				}

				// end rendering

				// sets the text color
				gc.setForeground(display.getSystemColor(SWT.COLOR_DARK_RED));

				// resets the background color
				gc.setBackground(origBack);

				event.detail &= ~SWT.SELECTED;
			}
		});

		for (int i = 0; i < table.getColumnCount(); i++) {
			table.getColumn(i).pack();
		}

		table.setSelection(table.getItem(0));
		shell.setSize(700, 500);

		/* Start DnD */

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
				// Mark the item we're on top of with a line because it will be
				// inserted above it.
				setItemMarked(table.getItem(new Point(event.x, event.y)));
				table.redraw();
				System.out.println("itemMarked -- " + itemMarked);
			}
		});
		table.addListener(SWT.MouseUp, new Listener() {
			public void handleEvent(Event event) {
				if (itemBeingDragged == null)
					return;
				TableItem item = table.getItem(new Point(event.x, event.y));
				if (item != null && item != itemBeingDragged) {
					TableItem[] items = table.getItems();

					int index = -1;
					for (int i = 0; i < items.length; i++) {
						if (items[i] == item) {
							index = i;
						}
					}

					// index is now the insertion index
					if (index != -1) {
						// This shouldn't fail ever but ya never know
						TableItem[] selected = table.getSelection();
						TableItem[] newSelected = new TableItem[selected.length];
						for (int x = 0; x < selected.length; x++) {
							TableItem sel = selected[x];
							// Make the new TableItem
							TableItem newSel = new TableItem(table, SWT.NONE, index + x);
							newSelected[x] = newSel;
							// Copy columns
							for (int i = 0; i < table.getColumnCount(); i++) {
								newSel.setText(i, sel.getText(i));
							}
						}
						// Remove the old selected items
						for (TableItem sel : selected) {
							sel.dispose();
						}
						// Select the new items
						table.setSelection(newSelected);
					}

				}
				setItemMarked(null);
				itemBeingDragged = null;
			}
		});

		/* End DnD */

		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		display.dispose();

	}

	public void drawAnimation(GC gc, int x, int y, int width, int height, Color color) {
		gc.setBackground(color);
		gc.fillRoundRectangle(x, y, width, height, 5, 5);
	}

	public void setItemMarked(TableItem item) {
		this.itemMarked = item;
		table.redraw();
	}

	public static void main(String... args) {
		new PartyRockDragDrop();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.keyCode) {
		case SWT.ESC:
			exit();
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	public void exit() {
		shell.dispose();
	}

}
