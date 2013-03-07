package com.partyrock.anim.laser;

import java.util.ArrayList;
import java.util.EnumSet;

import org.eclipse.swt.widgets.Shell;

import com.partyrock.LightMaster;
import com.partyrock.anim.ElementAnimation;
import com.partyrock.element.ElementController;
import com.partyrock.element.ElementType;
import com.partyrock.element.lasers.LaserController;
import com.partyrock.settings.Saver;
import com.partyrock.settings.SectionSettings;
import com.partyrock.tools.PartyToolkit;

public class LaserCircleAnimation extends ElementAnimation {

    private int radius = 15;

    public LaserCircleAnimation(LightMaster master, int startTime, String internalID,
            ArrayList<ElementController> lasers, double duration) {
        super(master, startTime, internalID, lasers, duration);

        needsIncrements();
    }

    public void setup(Shell window) {
        String radius = PartyToolkit.openInput(window,
                "What would you like the radius to be? [0-90, Recommended 5-30]", "Radius");

        if (radius != null) {
            try {
                this.radius = Integer.parseInt(radius);
            } catch (Exception e) {
                // If something goes wrong, just let the default stay
            }
        }
    }

    public void trigger() {
        for (ElementController element : getElements()) {
            ((LaserController) element).turnLaserOn();
        }
    }

    public void increment(double percentage) {
        for (ElementController element : getElements()) {
            LaserController laser = (LaserController) element;

            laser.setX((int) Math.round(radius * Math.cos(percentage * 2.0 * Math.PI)) + 90);
            laser.setY((int) Math.round(radius * Math.sin(percentage * 2.0 * Math.PI)) + 45);
        }
    }

    public static EnumSet<ElementType> getSupportedTypes() {
        return EnumSet.of(ElementType.LASERS);
    }

    @Override
    protected void saveSettings(SectionSettings settings) {
        settings.put("radius", Saver.saveInt(radius));
    }

    @Override
    protected void loadSettings(SectionSettings settings) {
        radius = Saver.loadInt(settings.get("radius"), this);
    }

}
