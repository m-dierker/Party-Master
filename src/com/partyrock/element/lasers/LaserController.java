package com.partyrock.element.lasers;

import com.partyrock.LightMaster;
import com.partyrock.element.ElementController;
import com.partyrock.element.ElementExecutor;
import com.partyrock.element.ElementSimulator;
import com.partyrock.element.ElementType;

public class LaserController extends ElementController {

    private LaserExecutor executor;
    private LaserSimulator simulator;
    private boolean on;

    // 0 - 180
    private int x = 90;
    // 0 - 90
    private int y = 45;

    public LaserController(LightMaster master, String internalID, String name, String id) {
        super(master, internalID, name, id);

        executor = new LaserExecutor(this);
        simulator = new LaserSimulator(this);
    }

    public void setLaser(boolean on) {
        this.on = on;

        if (on) {
            executor.turnLaserOn();
        } else {
            executor.turnLaserOff();
        }
    }

    public boolean isOn() {
        return on;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
        executor.setX(x);
    }

    public void setY(int y) {
        this.y = y;
        executor.setY(y);
    }

    public void setPos(int x, int y) {
        setX(x);
        setY(y);
    }

    @Override
    public ElementExecutor getExecutor() {
        return executor;
    }

    @Override
    public ElementSimulator getSimulator() {
        return simulator;
    }

    @Override
    public ElementType getType() {
        return ElementType.LASERS;
    }

    public void turnLaserOn() {
        setLaser(true);
    }

    public void turnLaserOff() {
        setLaser(false);
    }

}
