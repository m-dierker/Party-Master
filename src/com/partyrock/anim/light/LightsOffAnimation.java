package com.partyrock.anim.light;

import java.util.ArrayList;
import java.util.EnumSet;

import com.partyrock.LightMaster;
import com.partyrock.anim.ElementAnimation;
import com.partyrock.element.ElementController;
import com.partyrock.element.ElementType;
import com.partyrock.element.lights.LightController;

public class LightsOffAnimation extends ElementAnimation {

    public LightsOffAnimation(LightMaster master, int startTime, String internalID,
            ArrayList<ElementController> elementList, double duration) {
        super(master, startTime, internalID, elementList, duration);
    }

    public void trigger() {
        for (ElementController controller : getElements()) {
            LightController lights = (LightController) controller;
            lights.turnOff();
        }
    }

    public static EnumSet<ElementType> getSupportedTypes() {
        return EnumSet.of(ElementType.LIGHTS);
    }
}
