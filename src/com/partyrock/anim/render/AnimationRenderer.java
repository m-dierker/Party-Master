package com.partyrock.anim.render;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;

import com.partyrock.anim.Animation;

/**
 * Renders an animation
 * 
 * @author Matthew
 * 
 */
public class AnimationRenderer {
    private Animation animation;

    public AnimationRenderer(Animation animation) {
        this.animation = animation;
    }

    public Animation getAnimation() {
        return animation;
    }

    public void renderAnimation(GC gc, Rectangle rect) {
        gc.setBackground(animation.sysColor(SWT.COLOR_BLUE));
    }
}
