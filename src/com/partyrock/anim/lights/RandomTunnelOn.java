package com.partyrock.anim.lights;

import java.util.ArrayList;
import java.util.EnumSet;

import com.partyrock.LightMaster;
import com.partyrock.anim.ElementAnimation;
import com.partyrock.element.ElementController;
import com.partyrock.element.ElementType;
import com.partyrock.element.lights.LightController;

public class RandomTunnelOn extends ElementAnimation {

    private int triggered = -1;
    private ArrayList<ElementController> tempElements;

    public RandomTunnelOn(LightMaster master, int startTime, String internalID,
            ArrayList<ElementController> elementList, double duration) {
        super(master, startTime, internalID, elementList, duration);

        needsIncrements();
    }

    public void trigger() {
        for (ElementController controllers : getElements()) {
            LightController lights = (LightController) controllers;
            lights.turnOff();
        }

        tempElements = new ArrayList<ElementController>(getElements());
    }

    public void increment(double percentage) {
        int shouldBeTriggered = (int) (percentage * getElements().size());

        if (triggered < shouldBeTriggered) {
            for (int a = triggered + 1; a <= shouldBeTriggered && tempElements.size() > 0; a++) {
                LightController lights = (LightController) tempElements
                        .get((int) (Math.random() * tempElements.size()));
                lights.turnOn();
                tempElements.remove(lights);
            }
            shouldBeTriggered = triggered;
        }
    }

    public static EnumSet<ElementType> getSupportedTypes() {
        return EnumSet.of(ElementType.LIGHTS);
    }

}
