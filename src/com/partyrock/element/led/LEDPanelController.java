package com.partyrock.element.led;

import org.eclipse.swt.graphics.Color;

import com.partyrock.LightMaster;
import com.partyrock.element.ElementController;
import com.partyrock.element.ElementExecutor;
import com.partyrock.element.ElementSimulator;
import com.partyrock.element.ElementType;
import com.partyrock.settings.SectionSettings;

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

	// We have levels 0 - 5 for each color. This variable doesn't count 0 because math.
	private final static int LEVELS_OF_COLOR = 5;

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
		color = sampleColor(color);
		colors[r][c] = color;
		executor.setColor(r, c, color.getRed(), color.getGreen(), color.getBlue());
	}

	public void setColor(int r, int c, int red, int green, int blue) {
		this.setColor(r, c, new Color(master.getWindowManager().getDisplay(), red, green, blue));
	}

	/**
	 * Samples the color down from 256 levels to a given number
	 * @param color
	 * @return
	 */
	private Color sampleColor(Color color) {
		double red = color.getRed();
		double green = color.getGreen();
		double blue = color.getBlue();

		red /= (255 / LEVELS_OF_COLOR);
		red = (int) Math.round(red);
		red *= 255 / LEVELS_OF_COLOR;

		green /= (255 / LEVELS_OF_COLOR);
		green = (int) Math.round(green);
		green *= 255 / LEVELS_OF_COLOR;

		blue /= (255 / LEVELS_OF_COLOR);
		blue = (int) Math.round(blue);
		blue *= 255 / LEVELS_OF_COLOR;

		return new Color(color.getDevice(), (int) red, (int) green, (int) blue);
	}

	public void saveChildData(SectionSettings settings) {
		settings.put("panel_width", panelWidth);
		settings.put("panel_height", panelHeight);
	}

}
