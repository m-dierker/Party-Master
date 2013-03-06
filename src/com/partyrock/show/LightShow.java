package com.partyrock.show;

import java.util.ArrayList;
import java.util.List;

import com.partyrock.anim.Animation;
import com.partyrock.tools.PartyToolkit;

/**
 * Actually executes the show
 * 
 * @author Matthew
 * 
 */
public class LightShow extends Thread {
    private LightShowManager manager;
    private int startTime;
    private int lastTriggeredTime = -1;
    private ArrayList<Animation> activeAnimations;
    private int duration;

    /**
     * Constructs a new show, which will execute all animations in sequence
     * 
     * @param manager the show manager
     * @param startTime The start time we should play in milliseconds
     * @param duration The duration of the show in milliseconds, or -1 if we should just go until paused
     */
    public LightShow(LightShowManager manager, int startTime, int duration) {
        this.manager = manager;
        this.startTime = startTime;
        this.duration = duration;

        activeAnimations = new ArrayList<Animation>();

        lastTriggeredTime = startTime - 1;
    }

    public void run() {
        long start = System.currentTimeMillis();

        long time = 0;
        while (manager.isPlaying() && (start + duration >= (time = System.currentTimeMillis()) || duration == -1)) {
            // Trigger any new animations, and possibly add them to the active animations list

            // How far we've gone while playing
            int executeUntil = (int) (time - start) + startTime;

            // If the last time we triggered animations is less than the start time in the song plus how far we've gone
            if (lastTriggeredTime < executeUntil) {
                // Trigger animations

                for (int a = lastTriggeredTime + 1; a <= executeUntil; a++) {
                    if (manager.hasAnimationsAtTime(a)) {
                        List<Animation> animations = manager.getAnimationsForTime(a);

                        for (Animation animation : animations) {
                            animation.trigger();
                            if (animation.shouldIncrement()) {
                                activeAnimations.add(animation);
                            }
                        }
                    }
                }
                lastTriggeredTime = executeUntil;
            }

            for (int a = 0; a < activeAnimations.size(); a++) {
                Animation animation = activeAnimations.get(a);

                int durationInAnimation = executeUntil - animation.getStartTime();

                // Check for an expired animation
                if (durationInAnimation > animation.getDuration() * 1000) {
                    System.out.println("Removing animation with duration " + durationInAnimation + ", "
                            + animation.getDuration() * 1000);
                    activeAnimations.remove(a);
                    a--;
                } else {
                    // Do the increment
                    animation.increment(durationInAnimation / 1000.0 / animation.getDuration());
                }
            }

            Thread.yield();
            PartyToolkit.sleep(10);
        }

        if (!manager.isPaused()) {
            manager.pause();
        }
    }
}
