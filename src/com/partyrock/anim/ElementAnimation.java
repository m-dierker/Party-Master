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
 * 
 * @author Matthew
 * 
 */
public abstract class ElementAnimation extends Animation {

    /**
     * The child animation can use this however it wants
     */
    private ArrayList<ElementController> elements;
    private LightWindow main;

    private boolean increments;

    // Duration in seconds
    private double duration;

    /**
     * Note: This is a STANDARIZED constructor. You cannot change it.
     * 
     * @param master The LightMaster object controlling the show
     * @param startTime The start time for the animation
     * @param elementList The elements to put in the animation
     * @param duration The duration of the animation. If this is an instantaneous animation (ex: display an image), you
     *        can pass in 0 and use trigger().
     */
    public ElementAnimation(LightMaster master, int startTime, ArrayList<ElementController> elementList, double duration) {
        super(master, startTime);
        elements = new ArrayList<ElementController>();
        addElements(elementList);
        this.duration = duration;
    }

    /**
     * Returns the supported types of the animation. If this isn't overridden the animation won't appear anywhere.
     * 
     * @return A list of element types supported by the animation
     */
    public static EnumSet<ElementType> getSupportedTypes() {
        return null;
    }

    /**
     * This method can be overridden, and will be called when using or previewing an animation For example, for a
     * BlinkFadeAnimation, this can show a color picker
     */
    public void setup(Shell window) {

    }

    // You must implement one of the next two methods

    /**
     * Implement trigger() if your animation just needs to be called once
     */
    public void trigger() {

    }

    /**
     * Implement increment() if your animation does something over time
     * 
     * @param percentage The percentage of the way through your animation we are
     */
    public void increment(double percentage) {

    }

    // From here and below are useful methods, but not required to implement anything

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

    public LightWindow getMainWindow() {
        return main;
    }

    public double getDuration() {
        return duration;
    }

    /**
     * Call from a child's constructor when the animation is based on increments
     */
    public void needsIncrements() {
        increments = true;
    }

    public boolean shouldIncrement() {
        return increments;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }
}
