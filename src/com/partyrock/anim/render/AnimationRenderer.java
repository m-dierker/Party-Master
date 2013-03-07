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
        int x = PartyConstants.ELEMENT_NAME_COLUMN_SIZE
                + (int) (animation.getStartTime() / 1000.0 * PartyConstants.PIXELS_PER_SECOND);
        int width = (int) (animation.getDuration() * PartyConstants.PIXELS_PER_SECOND);

        gc.fillGradientRectangle(x, rect.y, width, rect.height, true);

        gc.setForeground(display.getSystemColor(SWT.COLOR_GRAY));
        gc.drawLine(x, rect.y, x, rect.y + rect.height);
        gc.drawLine(x + width, rect.y, x + width, rect.y + rect.height);

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
            gc.drawText(text, x, rect.y + rect.height / 4, true);
        }

        gc.setFont(oldFont);
    }
}
