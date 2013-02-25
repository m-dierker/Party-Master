package com.partyrock.anim;

import java.util.ArrayList;
import java.util.EnumSet;

import org.eclipse.swt.widgets.Shell;

import com.partyrock.LightMaster;
import com.partyrock.element.ElementController;
import com.partyrock.element.ElementType;
import com.partyrock.gui.LightWindow;

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
	@SuppressWarnings("unused")
	private LightWindow mainWindow;

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

	/**
	 * This method can be overridden, and will be called when using or previewing an animation
	 * For example, for a BlinkFadeAnimation, this can show a color picker
	 */
	public void setup(Shell window) {

	}
}
