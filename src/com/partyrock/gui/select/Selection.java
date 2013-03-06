package com.partyrock.gui.select;

public class Selection {
    public double start;
    public double duration;

    public Selection(double start, double duration) {
        this.start = start;
        this.duration = duration;
    }

    public String toString() {
        return "Start at " + start + " for " + duration + "s";
    }
}
