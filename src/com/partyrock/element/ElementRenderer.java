package com.partyrock.element;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

/**
 * Renders the name in the ElemenetTable, and can be overriden to customize
 * rendering for children
 * @author Matthew
 * 
 */
public class ElementRenderer {
	private ElementController controller;

	public ElementRenderer(ElementController controller) {
		this.controller = controller;
	}

	/**
	 * Renders the name portion of the table. Should be called on EraseItem. We
	 * do this so we can customize rendering of the name differently for each
	 * Element
	 * @param gc The GC to draw with
	 * @param rect The rectangle representing the bounds of the naming box
	 */
	public void renderName(GC gc, Rectangle rect) {
		String name = controller.getName();
		Point nameSize = gc.textExtent(name);

		gc.setForeground(gc.getDevice().getSystemColor(SWT.COLOR_WHITE));

		int xOffset = (rect.width - nameSize.x) / 2;
		if (xOffset < 0) {
			xOffset = 0;
		}

		int yOffset = (rect.height - nameSize.y) / 2;
		if (yOffset < 0) {
			yOffset = 0;
		}

		gc.drawString(name, rect.x + xOffset, rect.y + yOffset);
	}
}
