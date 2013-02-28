package com.partyrock.element.led;

import org.eclipse.swt.graphics.Color;

import com.partyrock.LightMaster;
import com.partyrock.element.ElementController;
import com.partyrock.element.ElementExecutor;
import com.partyrock.element.ElementSimulator;
import com.partyrock.element.ElementType;

/**
 * Controls LED Panels
 * @author Matthew
 * 
 */
public class LEDPanelController extends ElementController {

	private int panelWidth, panelHeight;
	private Color[][] colors;
	private LEDPanelExecutor executor;
	private LEDPanelSimulator simulator;

	public LEDPanelController(LightMaster master, String internalID, String name, String id, int width, int height) {
		super(master, internalID, name, id);

		this.panelWidth = width;
		this.panelHeight = height;

		executor = new LEDPanelExecutor(this);
		simulator = new LEDPanelSimulator(this);

		colors = new Color[panelHeight][panelWidth];

		// Initialize all colors as black
		for (int r = 0; r < panelHeight; r++) {
			for (int c = 0; c < panelWidth; c++) {
				colors[r][c] = new Color(master.getWindowManager().getDisplay(), 0, 0, 0);
			}
		}
	}

	@Override
	public ElementExecutor getExecutor() {
		return executor;
	}

	@Override
	public ElementSimulator getSimulator() {
		return simulator;
	}

	@Override
	public ElementType getType() {
		return ElementType.LEDS;
	}

	public Color getColor(int r, int c) {
		return colors[r][c];
	}

	/**
	 * Returns the panel width in LEDs
	 */
	public int getPanelWidth() {
		return panelWidth;
	}

	/**
	 * Returns the panel height in LEDs
	 */
	public int getPanelHeight() {
		return panelHeight;
	}

	public void setColor(int r, int c, Color color) {
		colors[r][c] = color;
		executor.setColor(r, c, color.getRed(), color.getGreen(), color.getBlue());
	}

	public void setColor(int r, int c, int red, int green, int blue) {
		this.setColor(r, c, new Color(master.getWindowManager().getDisplay(), red, green, blue));
	}

}
