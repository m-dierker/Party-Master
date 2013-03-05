package com.partyrock.anim.execute;

import com.partyrock.anim.ElementAnimation;
import com.partyrock.gui.LightWindow;
import com.partyrock.gui.select.Selection;
import com.partyrock.tools.PartyToolkit;

/**
 * Animates a single animation in a thread
 * 
 * @author Matthew
 * 
 */
public class SingleAnimationExecutor extends Thread {
    private ElementAnimation animation;
    private LightWindow window;

    public SingleAnimationExecutor(LightWindow window, ElementAnimation animation) {
        this.animation = animation;
        this.window = window;
    }

    public static void runAnimation(LightWindow window, ElementAnimation animation) {
        SingleAnimationExecutor s = new SingleAnimationExecutor(window, animation);
        s.start();
    }

    public void run() {
        animation.trigger();

        if (animation.shouldIncrement()) {

            Selection selection = window.getSelection();

            // Override the default duration
            if (selection != null) {
                animation.setDuration(selection.duration);
                window.getMaster().getShowManager().play();
            }

            double startTime = System.currentTimeMillis() / 1000.0;

            double time;
            while (startTime + animation.getDuration() > (time = (System.currentTimeMillis() / 1000.0))) {
                animation.increment((time - startTime) / animation.getDuration());
                Thread.yield();
                PartyToolkit.sleep(10);
            }

            window.getMaster().getShowManager().pause();
        }
    }
}
