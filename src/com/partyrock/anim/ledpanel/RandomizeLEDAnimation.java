package com.partyrock.anim.ledpanel;

import java.util.ArrayList;
import java.util.EnumSet;

import org.eclipse.swt.graphics.Color;

import com.partyrock.LightMaster;
import com.partyrock.anim.ElementAnimation;
import com.partyrock.element.ElementController;
import com.partyrock.element.ElementType;
import com.partyrock.element.led.LEDPanelController;

public class RandomizeLEDAnimation extends ElementAnimation {

	public RandomizeLEDAnimation(LightMaster master, int startTime, ArrayList<ElementController> panels) {
		super(master, startTime, panels);
	}

	/**
	 * Called when the animation is run
	 */
	@Override
	public void trigger() {
		System.out.println("trigger");
		// For each panel, set each LED to a random color
		for (ElementController element : getElements()) {
			LEDPanelController panel = (LEDPanelController) element;
			for (int r = 0; r < panel.getPanelHeight(); r++) {
				for (int c = 0; c < panel.getPanelWidth(); c++) {
					System.out.println("Setting color for " + r + ", " + c);
					panel.setColor(r, c, getRandomColor());
				}
			}
		}
	}

	/**
	 * Returns a completely randomized color
	 */
	public Color getRandomColor() {
		return sysColor((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256));
	}

	/**
	 * Returns the types of elements this animation supports (in this case, trivially just LEDs).
	 * Any animation that only supports LEDs should look just like this
	 */
	public static EnumSet<ElementType> getSupportedTypes() {
		return EnumSet.of(ElementType.LEDS);
	}
}
