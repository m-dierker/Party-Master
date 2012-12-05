package com.partyrock.comm.uc;

/**
 * A list of the types of Microcontrollers. When you add a new type, add it to
 * MicrocontrollerType's map
 * @author Matthew
 */
public enum MicrocontrollerType {
	LOCAL_ARDUINO("Local Arduino"); // Local Arduino

	private String name;

	private MicrocontrollerType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
