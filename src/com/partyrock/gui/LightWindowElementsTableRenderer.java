package com.partyrock.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.widgets.*;

import com.partyrock.LightMaster;

/**
 * Manages the custom rendering for elements in our LightWindow. This class has
 * an obscenely long name.
 * @author Matthew
 * 
 */
public class LightWindowElementsTableRenderer {
	private LightMaster master;
	private LightWindow lightWindow;

	public LightWindowElementsTableRenderer(LightMaster master, LightWindow lightWindow) {
		this.master = master;
		this.lightWindow = lightWindow;
	}

	/**
	 * Add all of the listeners to the main elements table necessary for custom
	 * element and animation rendering
	 * @param table the table to add these listeners too
	 */
	public void addCustomListeners(final Table table) {
		// Set a custom height and width
		table.addListener(SWT.MeasureItem, new Listener() {
			public void handleEvent(Event event) {
				// Custom height, which is just some static predefined constant
				event.height = 30;
				if (event.index == 1) {
					// Set the width of the render column, which is proportional
					// to the total time of the show.
					// Magnification, if necessary, will need to be done here
					double songLength = master.getMusicManager().getSongLength();

					// This happens if there isn't a song
					if (songLength == -1) {
						// Subtract the width of the first column in the default
						// case, and just render the enter table width
						event.width = getChannelWidth(table);
					} else {
						// This is completely arbitrary
						event.width = (int) (10 * songLength);
					}

				}
			}
		});

		// The all important render code! Renders everything necessary for
		// animations
		table.addListener(SWT.EraseItem, new Listener() {
			public void handleEvent(Event event) {

				// Get the item that's being rendered
				TableItem item = (TableItem) event.item;

				// Don't let any silly selection stuff be rendered
				event.detail &= ~SWT.HOT;

				Display display = lightWindow.getDisplay();

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
				Color origBackground = gc.getBackground();
				int channelWidth = getChannelWidth(table);

				// Start Rendering

				// Select a background color based on if the channel is selected
				// or not
				Color backgroundColor = ((event.detail & SWT.SELECTED) != 0) ? display.getSystemColor(SWT.COLOR_BLUE)
						: display.getSystemColor(SWT.COLOR_BLACK);

				// Fill the background based on selection
				if (event.index == 1) {
					// Only fill the column with the animations
					gc.setBackground(backgroundColor);
					gc.fillRectangle(event.x, rect.y, channelWidth, rect.height);
				}

				// Set the color of the separator color
				int separatorColor;
				if (event.index == 0) {
					// The first column separator is black
					separatorColor = SWT.COLOR_BLACK;
				} else {
					separatorColor = SWT.COLOR_GRAY;
				}

				gc.setForeground(display.getSystemColor(separatorColor));
				gc.drawLine(event.x, rect.y + rect.height - 1, event.x + channelWidth, rect.y + rect.height - 1);

				// End rendering

				// Sets the text color for rendering the name (SWT takes care of
				// actually rendering this for us, so we have to resort to
				// stupid hacks)
				System.out.println("setting foreground");
				gc.setForeground(display.getSystemColor(SWT.COLOR_DARK_RED));

				// Resets the original background color
				gc.setBackground(origBackground);

				// We've handled selection here
				event.detail &= ~SWT.SELECTED;
			}
		});
	}

	public int getChannelWidth(final Table table) {
		return table.getSize().x - table.getColumn(0).getWidth();
	}
}
