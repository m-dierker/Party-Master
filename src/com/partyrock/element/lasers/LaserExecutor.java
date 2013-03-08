package com.partyrock.element.lasers;

import com.partyrock.element.ElementExecutor;

public class LaserExecutor extends ElementExecutor {

    @SuppressWarnings("unused")
    private LaserController controller;

    public LaserExecutor(LaserController controller) {
        super(controller);
        this.controller = controller;
    }

    void turnLaserOn() {
        // implement
    }

    void turnLaserOff() {
        // implement
    }

    public void setX(int x) {
        // implement
    }

    public void setY(int y) {
        // implement
    }
}
