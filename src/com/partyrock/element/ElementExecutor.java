package com.partyrock.element;

import com.partyrock.comm.uc.Microcontroller;

public abstract class ElementExecutor {
    private ElementController controller;
    private Microcontroller microcontroller;

    public ElementExecutor(ElementController controller) {
        this.controller = controller;
    }

    public void setMicrocontroller(Microcontroller uc) {
        this.microcontroller = uc;
    }

    public Microcontroller getMicrocontroller() {
        return microcontroller;
    }

    public ElementController getController() {
        return controller;
    }
}
