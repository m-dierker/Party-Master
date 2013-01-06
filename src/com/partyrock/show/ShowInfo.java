package com.partyrock.show;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentSkipListMap;

import com.partyrock.LightMaster;
import com.partyrock.anim.Animation;
import com.partyrock.settings.PersistentSettings;

/**
 * Manages everything associated with the show specifically (such as the list of
 * animations)
 * @author Matthew
 * 
 */
@SuppressWarnings("unused")
public class ShowInfo {
	private PersistentSettings showFile;
	private LightMaster master;
	// This will need to be made thread safe at some point probably (the
	// animations ArrayList itself) -- Collections.synchronizedList() will do
	// this.
	private ConcurrentSkipListMap<Long, ArrayList<Animation>> animations;

	public ShowInfo() {

	}
}
