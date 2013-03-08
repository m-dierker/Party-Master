package com.partyrock.anim.lasers;

import java.util.ArrayList;
import java.util.EnumSet;

import com.partyrock.LightMaster;
import com.partyrock.anim.ElementAnimation;
import com.partyrock.element.ElementController;
import com.partyrock.element.ElementType;
import com.partyrock.element.lasers.LaserController;
import com.partyrock.settings.SectionSettings;

public class LaserOnAnimation extends ElementAnimation {

    public LaserOnAnimation(LightMaster master, int startTime, String internalID,
            ArrayList<ElementController> elementList, double duration) {
        super(master, startTime, internalID, elementList, duration);
    }

    public void trigger() {
        for (ElementController element : getElements()) {
            LaserController laser = (LaserController) element;

            laser.turnLaserOn();
        }
    }

    public static EnumSet<ElementType> getSupportedTypes() {
        return EnumSet.of(ElementType.LASERS);
    }

    @Override
    protected void saveSettings(SectionSettings settings) {

    }

    @Override
    protected void loadSettings(SectionSettings settings) {

    }

}
