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

	/**
	 * Note: This is a STANDARIZED constructor. You cannot change it.
	 * @param master The LightMaster object controlling the show
	 * @param startTime The start time for the animation
	 * @param elementList The elements to put in the animation
	 */
	public ElementAnimation(LightMaster master, int startTime, ArrayList<ElementController> elementList) {
		super(master, startTime);
		elements = new ArrayList<ElementController>();
		addElements(elementList);
	}

	/**
	 * Returns the supported types of the animation. If this isn't overridden
	 * the
	 * animation won't appear anywhere.
	 * @return A list of element types supported by the animation
	 */
	public static EnumSet<ElementType> getSupportedTypes() {
		return null;
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
}
