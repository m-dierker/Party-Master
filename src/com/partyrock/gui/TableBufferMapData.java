package com.partyrock.gui;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.TableItem;

public class TableBufferMapData {
    TableItem item;
    Image image;
    Rectangle rect;
    int width;

    public TableBufferMapData(TableItem item, Image image, Rectangle area, int width) {
        this.item = item;
        this.image = image;
        this.rect = area;
        this.width = width;
    }
}
