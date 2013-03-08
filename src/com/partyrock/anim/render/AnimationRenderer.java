package com.partyrock.anim.render;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import com.partyrock.anim.Animation;
import com.partyrock.config.PartyConstants;

/**
 * Renders an animation
 * 
 * @author Matthew
 * 
 */
public class AnimationRenderer {
    private Animation animation;
    private static Color gradientForeground = null;
    private static Color gradientBackground = null;

    public AnimationRenderer(Animation animation) {
        this.animation = animation;

        if (gradientForeground == null) {
            gradientForeground = new Color(animation.getMaster().getWindowManager().getDisplay(), 13, 140, 0);
        }

        if (gradientBackground == null) {
            gradientBackground = new Color(animation.getMaster().getWindowManager().getDisplay(), 13, 190, 0);
        }
    }

    public Animation getAnimation() {
        return animation;
    }

    public void renderAnimation(GC gc, Rectangle rect) {
        Display display = animation.getMaster().getWindowManager().getDisplay();
        gc.setForeground(gradientForeground);
        gc.setBackground(gradientBackground);
        int x = getStartX();
        int width = getWidth();

        gc.fillGradientRectangle(x, 0, width, rect.height, true);

        gc.setForeground(display.getSystemColor(SWT.COLOR_GRAY));
        gc.drawLine(x, 0, x, 0 + rect.height);
        gc.drawLine(x + width, 0, x + width, 0 + rect.height);

        gc.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
        String text = animation.getClass().getSimpleName();

        Font oldFont = gc.getFont();
        FontData data = oldFont.getFontData()[0];
        data.setHeight(10);
        gc.setFont(new Font(display, data));

        Point textSize = gc.textExtent(text);
        while (textSize.x > width && !text.equals("")) {
            text = text.substring(0, text.length() - 1);
            textSize = gc.textExtent(text);
        }

        if (!text.equals("")) {
            gc.drawText(text, x, 0 + rect.height / 4, true);
        }

        gc.setFont(oldFont);
    }

    public int getWidth() {
        return (int) (animation.getDuration() * PartyConstants.PIXELS_PER_SECOND);
    }

    public int getStartX() {
        return (int) (animation.getStartTime() / 1000.0 * PartyConstants.PIXELS_PER_SECOND);
    }
}
