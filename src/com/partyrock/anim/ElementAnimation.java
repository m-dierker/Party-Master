package com.partyrock.anim;

import java.util.ArrayList;
import java.util.EnumSet;

import com.partyrock.LightMaster;
import com.partyrock.element.ElementController;
import com.partyrock.element.ElementType;

/**
 * Abstract class for an animation that utilizes an element
 * @author Matthew
 * 
 */
public abstract class ElementAnimation extends Animation {

	/**
	 * The child animation can use this however it wants
	 */
	private ArrayList<ElementController> elements;

	public ElementAnimation(LightMaster master, int startTime, ArrayList<ElementController> newElements) {
		super(master, startTime);
		elements = new ArrayList<ElementController>();
		addElements(newElements);
	}

	public ElementAnimation(LightMaster master, int startTime, ElementController element) {
		super(master, startTime);
		addElement(element);
	}

	public void addElement(ElementController element) {
		elements.add(element);
	}

	public void addElements(ArrayList<ElementController> newElements) {
		for (int a = 0; a < newElements.size(); a++) {
			addElement(newElements.get(a));
		}
	}

	public ArrayList<ElementController> getElements() {
		return elements;
	}

	/**
	 * Returns the supported types of the animation. If this isn't set the
	 * animation won't appear anywhere.
	 * @return
	 */
	public abstract EnumSet<ElementType> getSupportedTypes();

}
