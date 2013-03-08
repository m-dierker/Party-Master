package com.partyrock.element.lights;

import com.partyrock.element.ElementExecutor;

public class LightExecutor extends ElementExecutor {
    @SuppressWarnings("unused")
    private LightController controller;

    public LightExecutor(LightController controller) {
        super(controller);
        this.controller = controller;
    }

    /**
     * Turns lights on
     */
    public void on() {
        getMicrocontroller().getCommunicator().sendMsg("1");
    }

    /**
     * Turn lights off
     */
    public void off() {
        // TODO: Implement
    }
}
