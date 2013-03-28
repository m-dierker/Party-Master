package com.partyrock.element.lights;

import com.partyrock.element.ElementExecutor;

public class LightExecutor extends ElementExecutor {
    private LightController controller;

    public LightExecutor(LightController controller) {
        super(controller);
        this.controller = controller;
    }

    /**
     * Turns lights on
     */
    public void on() {
        if (getMicrocontroller() == null) {
            return;
        }
        getMicrocontroller().sendMsg(controller.getID().toUpperCase());
    }

    /**
     * Turn lights off
     */
    public void off() {
        if (getMicrocontroller() == null) {
            return;
        }
        getMicrocontroller().sendMsg(controller.getID().toLowerCase());
    }
}
