package com.partyrock.anim.lights;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.widgets.Shell;

import com.partyrock.LightMaster;
import com.partyrock.anim.ElementAnimation;
import com.partyrock.element.ElementController;
import com.partyrock.element.ElementType;
import com.partyrock.element.lights.LightController;
import com.partyrock.id.ID;

public class LightsRecordAnimation extends ElementAnimation {

    private ConcurrentHashMap<Character, Double> keyMap;
    private ConcurrentHashMap<Character, LightController> lightsMap;

    public LightsRecordAnimation(LightMaster master, int startTime, String internalID,
            ArrayList<ElementController> elementList, double duration) {
        super(master, startTime, internalID, elementList, duration);

        keyMap = new ConcurrentHashMap<Character, Double>();
        lightsMap = new ConcurrentHashMap<Character, LightController>();

        for (ElementController element : master.getElements()) {
            if (element instanceof LightController) {
                LightController lights = (LightController) element;
                lightsMap.put(Character.toUpperCase(lights.getID().charAt(0)), lights);
            }
        }
    }

    public void setup(Shell window) {
        Shell recordWindow = new Shell(getMaster().getWindowManager().getDisplay());
        recordWindow.addKeyListener(new KeyListener() {

            @Override
            public void keyPressed(KeyEvent e) {
                keyDown(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                keyUp(e);
            }

        });
        recordWindow.setBounds(800, 800, 100, 100);
        recordWindow.setVisible(true);
        recordWindow.open();
    }

    public void keyDown(KeyEvent e) {
        if (keyMap.containsKey(Character.toUpperCase(e.character)) || !Character.isLetter(e.character)) {
            return;
        }
        keyMap.put(Character.toUpperCase(e.character), getMaster().getShowManager().getMusic().getCurrentTime());
    }

    public void keyUp(KeyEvent e) {
        if (e == null || e.character == '\0' || !Character.isLetter(e.character) || keyMap == null) {
            return;
        }
        LightController lights = lightsMap.get(Character.toUpperCase(e.character));
        double startTime = keyMap.get(Character.toUpperCase(e.character));
        double endTime = getMaster().getShowManager().getMusic().getCurrentTime();
        double duration = endTime - startTime;

        int startTimeInt = (int) (startTime * 1000);
        int endTimeInt = (int) (endTime * 1000);

        ArrayList<ElementController> elements = new ArrayList<ElementController>();
        elements.add(lights);

        LightsOnAnimation onAnim = new LightsOnAnimation(getMaster(), startTimeInt, ID.genID("an"), elements, duration);
        LightsOffAnimation offAnim = new LightsOffAnimation(getMaster(), endTimeInt, ID.genID("an"), elements, .05);

        getMaster().getShowManager().addAnimation(onAnim);
        getMaster().getShowManager().addAnimation(offAnim);

        keyMap.remove(Character.toUpperCase(e.character));

    }

    public static EnumSet<ElementType> getSupportedTypes() {
        return EnumSet.of(ElementType.LIGHTS);
    }
}
