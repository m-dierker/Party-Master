package com.partyrock.anim;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;

import com.partyrock.LightMaster;
import com.partyrock.anim.blink.BlinkFadeAnimation;
import com.partyrock.anim.laser.LaserCircleAnimation;
import com.partyrock.anim.laser.LaserOnAnimation;
import com.partyrock.anim.ledpanel.LEDFaviconAnimation;
import com.partyrock.anim.ledpanel.LEDWipeAnimation;
import com.partyrock.anim.ledpanel.RandomizeLEDAnimation;
import com.partyrock.anim.light.LightsOffAnimation;
import com.partyrock.anim.light.LightsOnAnimation;
import com.partyrock.element.ElementController;
import com.partyrock.element.ElementType;

/**
 * Manages the master list of all animations. When you make an animation, you need to add it here.
 * 
 * @author Matthew
 * 
 */
public class LightAnimationManager {
    private LightMaster master;
    private ArrayList<Class<? extends ElementAnimation>> animationList;

    public LightAnimationManager(LightMaster master) {
        this.master = master;
        animationList = new ArrayList<Class<? extends ElementAnimation>>();

        // Add new animations to the list below
        // They'll be automatically pulled for the preview list, execute list, etc.

        // LEDs
        animationList.add(RandomizeLEDAnimation.class);
        animationList.add(LEDFaviconAnimation.class);
        animationList.add(LEDWipeAnimation.class);

        // Lasers
        animationList.add(LaserOnAnimation.class);
        animationList.add(LaserCircleAnimation.class);

        // Lights
        animationList.add(LightsOnAnimation.class);
        animationList.add(LightsOffAnimation.class);

        // Debug
        animationList.add(BlinkFadeAnimation.class);
    }

    /**
     * Get a list of all possible animations
     * 
     * @return A list of animations
     */
    public ArrayList<Class<? extends ElementAnimation>> getAnimationList() {
        return animationList;
    }

    /**
     * This is used in menus to return a list of all animations that will work on *all* of the given elements So if two
     * lasers and three strands of lights are selected, this will only return Animations that work with *both* of those
     * things and can take in a combination of both of them
     * 
     * @param elements The elements to check
     * @return A list of animation classes that can support the entirely of the incoming elements
     */
    @SuppressWarnings("unchecked")
    public ArrayList<Class<? extends ElementAnimation>> getAnimationListForElements(
            ArrayList<ElementController> elements) {
        ArrayList<Class<? extends ElementAnimation>> ret = new ArrayList<Class<? extends ElementAnimation>>();

        HashSet<ElementType> elementTypes = new HashSet<ElementType>();

        for (ElementController element : elements) {
            elementTypes.add(element.getType());
        }

        for (Class<? extends ElementAnimation> c : getAnimationList()) {
            try {
                EnumSet<ElementType> supportedTypes = (EnumSet<ElementType>) c.getMethod("getSupportedTypes").invoke(
                        null);

                boolean valid = true;
                for (ElementType type : elementTypes) {
                    if (!supportedTypes.contains(type)) {
                        valid = false;
                        break;
                    }
                }

                if (valid) {
                    ret.add(c);
                }

            } catch (Exception e) {
                System.out.println("Element of class " + c.getName()
                        + " doesn't have a getSupportedTypes() method setup correctly.");
                e.printStackTrace();
            }
        }

        return ret;
    }

    public LightMaster getMaster() {
        return master;
    }
}
