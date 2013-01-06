package com.partyrock.anim.blink;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;

import com.partyrock.LightMaster;
import com.partyrock.anim.ElementAnimation;
import com.partyrock.element.ElementController;
import com.partyrock.element.blink.BlinkController;

public class BlinkFadeAnimation extends ElementAnimation {

	private Color color;

	public BlinkFadeAnimation(LightMaster master, int startTime, BlinkController blink) {
		super(master, startTime, blink);
		color = sysColor(SWT.COLOR_RED);

	}

	public BlinkFadeAnimation(LightMaster master, int startTime, ArrayList<ElementController> blinks) {
		super(master, startTime, blinks);
		color = sysColor(SWT.COLOR_RED);

	}

	@Override
	public void trigger() {
		for (ElementController blinkController : getElements()) {
			BlinkController blink = (BlinkController) blinkController;
			blink.setColor(color);
		}
	}
}
