package com.partyrock.anim;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import com.partyrock.LightMaster;
import com.partyrock.anim.render.AnimationRenderer;
import com.partyrock.settings.SectionSettings;

/**
 * This class is the basis for all animations
 * 
 * @author Matthew
 * 
 */
public abstract class Animation implements Comparable<Animation> {

    /**
     * The start time relative to the start of the song. In milliseconds. If this is a strange use case (beyond an
     * animation in the context of the show), set it to 0.
     */
    private int startTime;
    private LightMaster master;
    private AnimationRenderer renderer;
    private String internalID;

    public Animation(LightMaster master, int startTime, String internalID) {
        this.master = master;
        this.startTime = startTime;
        this.renderer = new AnimationRenderer(this);
        this.internalID = internalID;
    }

    /**
     * What to do when the animation has been triggered
     */
    public abstract void trigger();

    public abstract void increment(double percentage);

    public abstract boolean shouldIncrement();

    public abstract double getDuration();

    public abstract void save(SectionSettings settings);

    public abstract void load(SectionSettings settings);

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
     * Returns the SWT color object from a constant like SWT.COLOR_BLUE. This is a convenience method because Animations
     * will likely deal with a lot of color
     * 
     * @param swtColor The SWT.COLOR_* constant to use
     * @return The actual color object
     */
    public Color sysColor(int swtColor) {
        Display display = master.getWindowManager().getDisplay();
        return display.getSystemColor(swtColor);
    }

    public Color sysColor(int r, int g, int b) {
        return new Color(master.getWindowManager().getDisplay(), r, g, b);
    }

    public int compareTo(Animation animation) {
        // This was a troublesome bug
        if (this == animation) {
            return 0;
        }

        if (this.startTime == animation.startTime) {
            if (this.getDuration() == animation.getDuration()) {
                // At this point it probably just shouldn't matter, but if we return 0, they can get marked as
                // duplicates
                return 1;
            }
            return 10 * (int) (this.getDuration() - animation.getDuration());
        }
        return (10000 * (this.startTime - animation.startTime));
    }

    public AnimationRenderer getRenderer() {
        return renderer;
    }

    public String getInternalID() {
        return internalID;
    }
}
