package com.partyrock.element;

import com.partyrock.LightMaster;

/**
 * An element controller is the main umbrella for an element. An element is
 * defined to be a single thing in the show (ex: a light strand, or a laser).
 * The controller should know what the current state of the element is (so the
 * submodules described below will get this data from the controller)
 * 
 * In addition to a controller, each element has
 * - An executor, which actually executes the action, such as actually sending
 * the serial command to rotate the object, sending the HTTP request to a remote
 * client, or actually causing a motor to rotate
 * - A simulator, which simulates the element graphically in the Simulator.
 * - A renderer, which renders the element in the GUI. The renderer may not need
 * to be customized, as it will read the animation list for the element and
 * render it
 * @author Matthew
 * 
 */
public abstract class ElementController {
	private LightMaster master;
	private String name;
	private String id;

	/**
	 * Constructor
	 * @param master The LightMaster running everything
	 * @param name The element's name
	 * @param id The element's id. This can/should be used when communicating
	 *            with the element (and every element will need some sort of
	 *            identifier defined by the protocol)
	 */
	public ElementController(LightMaster master, String name, String id) {
		this.master = master;
		setName(name);
		setID(id);
	}

	/**
	 * Returns the LightMaster for the element
	 */
	public LightMaster getMaster() {
		return master;
	}

	/**
	 * Gets the element's name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the element's name
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	public abstract ElementExecutor getExecutor();

	public abstract ElementRenderer getRenderer();

	public abstract ElementSimulator getSimulator();

	public String getID() {
		return id;
	}

	public void setID(String id) {
		this.id = id;
	}

	/**
	 * The type name for the Elements GUI
	 * @return
	 */
	public abstract String getTypeName();
}