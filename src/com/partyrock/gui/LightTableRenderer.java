package com.partyrock.gui;

import java.util.Iterator;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import com.partyrock.LightMaster;
import com.partyrock.anim.Animation;
import com.partyrock.config.PartyConstants;
import com.partyrock.element.ElementController;

/**
 * Manages the custom rendering for elements in our LightWindow. This class has an obscenely long name.
 * 
 * @author Matthew
 * 
 */
public class LightTableRenderer {
    private LightMaster master;
    private LightWindow lightWindow;
    private Color separatorColor;
    private Color selectedChannelColor;

    // private HashMap<TableItem, TableBufferMapData> bufferMap;

    public LightTableRenderer(LightMaster master, LightWindow lightWindow) {
        this.master = master;
        this.lightWindow = lightWindow;

        separatorColor = new Color(lightWindow.getDisplay(), 87, 87, 87);
        selectedChannelColor = new Color(lightWindow.getDisplay(), 0, 13, 170);

        // bufferMap = new HashMap<TableItem, TableBufferMapData>();
    }

    /**
     * Add all of the listeners to the main elements table necessary for custom element and animation rendering
     * 
     * @param table the table to add these listeners too
     */
    public void addCustomListeners(final Table table, final LightWindow window) {

        // Set a custom height and width
        table.addListener(SWT.MeasureItem, new Listener() {
            public void handleEvent(Event event) {

                // Set the width of the name column

                // Custom height, which is just some static predefined constant
                event.height = 30;
                if (event.index == 0) {
                    // Set the width of the name column
                    table.getColumn(0).setWidth(PartyConstants.ELEMENT_NAME_COLUMN_SIZE);
                } else if (event.index == 1) {
                    // Set the width of the render column, which is proportional
                    // to the total time of the show.
                    // Magnification, if necessary, will need to be done here
                    double songLength = master.getShowManager().getMusicDuration();

                    int width;

                    // This happens if there isn't a song
                    if (songLength == -1) {
                        // Subtract the width of the first column in the default
                        // case, and just render the enter table width
                        width = getAnimationColumnWidth(table);
                    } else {
                        width = (int) (PartyConstants.PIXELS_PER_SECOND * songLength);
                    }

                    table.getColumn(1).setWidth(width);

                }
            }
        });

        // The all important render code! Renders everything necessary for
        // animations
        table.addListener(SWT.EraseItem, new Listener() {
            public void handleEvent(Event event) {

                // Get the item that's being rendered
                TableItem item = (TableItem) event.item;
                ElementController element = (ElementController) item.getData();

                // Don't let any silly selection stuff be rendered
                event.detail &= ~SWT.HOT;

                Display display = lightWindow.getDisplay();

                GC gcOrig = event.gc;
                // Area of the entire visible table
                Rectangle area = table.getClientArea();

                // I don't remember why this clipping stuff was necessary but
                // I'll update this comment when I figure it out
                // Clipping
                int width = area.x + area.width - event.x + 1;

                if (width < 0) {
                    // fix for drag and drop, where this can become < 0
                    width = 0;
                }

                Region region = new Region();
                gcOrig.getClipping(region);
                region.add(event.x, event.y, width, event.height + 1);
                gcOrig.setClipping(region);
                region.dispose();

                /*
                 * This is confusing. rect is the bounds of the event, so the Y value is correct, but the width is
                 * wrong, I think since we're increasing the width of the second table by some value. We fix the
                 * rect.width so it doesn't suck, and everything else (x, y, and height) should be right
                 */
                Rectangle rect = event.getBounds();
                // The width is correct for the first column, but not the one
                // with animations
                boolean cached = false;
                if (event.index == 1) {
                    rect.width = getAnimationColumnWidth(table);
                }

                int musicWidth;
                if (master.getShowManager().getMusicDuration() == -1) {
                    musicWidth = table.getClientArea().width - PartyConstants.ELEMENT_NAME_COLUMN_SIZE;
                } else {
                    musicWidth = (int) (PartyConstants.PIXELS_PER_SECOND * master.getShowManager().getMusicDuration());
                }

                Image buffer = new Image(display, (event.index == 1 ? musicWidth
                        : PartyConstants.ELEMENT_NAME_COLUMN_SIZE), rect.height);
                GC gc = new GC(buffer);

                // if (event.index == 1) {
                // TableBufferMapData data = bufferMap.get(item);
                //
                // if (data != null && data.item == item && data.rect.equals(rect) && data.width == musicWidth) {
                // gc.drawImage(data.image, 0, 0);
                // gc.dispose();
                // cached = true;
                // } else {
                // System.out.println("Not cached");
                // }
                // }
                //
                // if (!cached) {
                // Color origBackground = gc.getBackground();

                // Start Rendering

                // Select a background color based on if the channel is selected
                // or not

                Color backgroundColor = ((event.detail & SWT.SELECTED) != 0) ? selectedChannelColor : display
                        .getSystemColor(SWT.COLOR_BLACK);
                gc.setBackground(backgroundColor);
                gc.fillRectangle(0, 0, musicWidth, rect.height);

                // Fill the background based on selection
                if (event.index == 0) {
                    // First column, where the name should go

                    // Tell the ElementRenderer to render the name
                    element.getRenderer().renderName(gc, rect);
                } else if (event.index == 1) {
                    // Render animations
                    Set<Animation> animations = master.getShowManager().getAnimationsForElement(element);

                    if (animations != null) {
                        Iterator<Animation> it = animations.iterator();
                        while (it.hasNext()) {
                            Animation animation = it.next();
                            animation.getRenderer().renderAnimation(gc, rect);
                        }
                    }
                }

                // Set the color of the separator color
                gc.setForeground(separatorColor);
                gc.drawLine(0, 0 + rect.height - 1, 0 + (event.index == 1 ? musicWidth : rect.width),
                        0 + rect.height - 1);
                // }

                gcOrig.drawImage(buffer, rect.x, rect.y);
                buffer.dispose();
                gc.dispose();

                // if (event.index == 0) {
                // buffer.dispose();
                // }

                // if (!cached && event.index == 1) {
                //
                // TableBufferMapData data;
                // if ((data = bufferMap.get(item)) != null) {
                // data.image.dispose();
                // }
                //
                // bufferMap.put(item, new TableBufferMapData(item, buffer, rect, musicWidth));
                // }

                window.getSelectionRenderer().renderSelection(gcOrig, table.getClientArea(), window.getSelection(),
                        false);
                window.getMusicRenderer().renderMusic(gcOrig, table.getClientArea(), false);

                // End rendering

                // Resets the original background color
                // gc.setBackground(origBackground);

                // We've handled selection here
                event.detail &= ~SWT.SELECTED;

                window.getTimeline().redraw();
            }
        });
    }

    public int getAnimationColumnWidth(final Table table) {
        return table.getSize().x - table.getColumn(0).getWidth();
    }
}
