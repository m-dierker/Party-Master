package com.partyrock.show;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListMap;

import com.partyrock.LightMaster;
import com.partyrock.anim.Animation;
import com.partyrock.music.MP3;
import com.partyrock.settings.PersistentSettings;

/**
 * Manages everything associated with the show specifically (such as the list of animations and MP3 file)
 * 
 * @author Matthew
 * 
 */
@SuppressWarnings("unused")
public class LightShowManager {
    private PersistentSettings showFile;
    private MP3 music;
    private LightMaster master;
    private ConcurrentSkipListMap<Integer, List<Animation>> animations;

    public LightShowManager(LightMaster master) {
        this.master = master;

        animations = new ConcurrentSkipListMap<Integer, List<Animation>>();
    }

    /**
     * Adds an animation at the given time
     * 
     * @param startTime
     *            The time to trigger the animation
     * @param animation
     *            The animation to add
     */
    public void addAnimation(int startTime, Animation animation) {
        if (!animations.containsKey(startTime)) {
            animations.put(startTime, Collections.synchronizedList(new ArrayList<Animation>()));
        }

        animations.get(startTime).add(animation);
    }

    public void loadShow(File f) {
        // TODO Auto-generated method stub

    }

    /**
     * Loads a new file as the music
     * 
     * @param f
     */
    public void loadMusic(File f) {
        music = new MP3(f);
    }

    /**
     * Returns the MP3 file representing the music
     * 
     * @return
     */
    public MP3 getMusic() {
        return music;
    }

    /**
     * Plays the music
     */
    public void playMusic() {
        if (music == null) {
            return;
        }

        music.play();
    }

    /**
     * Pauses the music
     */
    public void pauseMusic() {
        if (music == null) {
            return;
        }
        music.pause();
    }

    public void stopMusic() {
        if (music == null) {
            return;
        }

        music.stop();
    }

    /**
     * Returns the duration of the music, or -1 if no music is loaded
     */
    public double getMusicDuration() {
        if (music == null) {
            return -1;
        }

        return music.getDuration();
    }

    /**
     * Returns the current position we're at in the music, or -1 if no music is loaded
     * 
     * @return
     */
    public double getCurrentTime() {
        if (music == null) {
            return -1;
        }

        return music.getCurrentTime();
    }
}
