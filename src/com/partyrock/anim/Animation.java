package com.partyrock.anim;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import com.partyrock.LightMaster;

/**
 * This class is the basis for all animations
 * @author Matthew
 * 
 */
public abstract class Animation {

	/**
	 * The start time relative to the start of the song. In milliseconds. If
	 * this is a strange use case (beyond an animation in the context of the
	 * show), set it to 0.
	 */
	private int startTime;
	private LightMaster master;

	public Animation(LightMaster master, int startTime) {
		this.master = master;
		this.startTime = startTime;
	}

	/**
	 * What to do when the animation has been triggered
	 */
	public abstract void trigger();

	/**
	 * Returns the start time of the animation
	 */
	public int getStartTime() {
		return startTime;
	}

	public LightMaster getMaster() {
		return master;
	}

	/**
	 * Returns the SWT color object from a constant like SWT.COLOR_BLUE. This is
	 * a convienence method because Animations will likely deal with a lot of
	 * color
	 * @param swtColor The SWT.COLOR_* constant to use
	 * @return The actual color object
	 */
	public Color sysColor(int swtColor) {
		Display display = master.getWindowManager().getDisplay();
		return display.getSystemColor(swtColor);
	}

}
