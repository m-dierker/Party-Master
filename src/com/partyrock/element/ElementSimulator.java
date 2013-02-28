package com.partyrock.element;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;

public abstract class ElementSimulator {

	private ElementController controller;
	private int x;
	private int y;
	private Point textSize;

	public ElementSimulator(ElementController controller) {
		this.controller = controller;

		x = 25;
		y = 25;
	}

	/**
	 * Render the element in the simulator
	 * @param gc The GC to draw with
	 */
	public abstract void render(GC gc);

	/**
	 * Renders the name of the element
	 * @param gc
	 */
	public void renderName(GC gc) {
		gc.setForeground(controller.getMaster().getWindowManager().getDisplay().getSystemColor(SWT.COLOR_WHITE));
		gc.drawText(controller.getName(), x, y, true);

		// We have to do this so we can calculate the width and height during a drag
		getTextBounds(gc);
	}

	/**
	 * Returns the size of the name, so the element can render its simulation under it
	 * @param gc The GC to use
	 * @return The Point with the bounds of the name
	 */
	public Point getTextBounds(GC gc) {
		return (textSize = gc.textExtent(controller.getName()));
	}

	/**
	 * Returns the last cached textSize
	 */
	public Point getTextBounds() {
		return textSize;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setPos(int x, int y) {
		setX(x);
		setY(y);
	}

	/**
	 * Returns the X value the simulation is at
	 */
	public int getX() {
		return x;
	}

	/**
	 * Returns the Y value the simulation is at
	 */
	public int getY() {
		return y;
	}

	public abstract int getWidth();

	public abstract int getHeight();

	public boolean mouseDown(MouseEvent e) {
		if (e.x >= getX() && e.y >= getY() && e.x <= getX() + getWidth() && e.y <= getY() + getHeight()) {
			return true;
		}

		return false;
	}
}
