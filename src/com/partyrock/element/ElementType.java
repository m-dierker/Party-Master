package com.partyrock.element;

/**
 * Lists the various element types
 * @author Matthew
 * 
 */
public enum ElementType {
	LIGHTS("Strand"), LASERS("Laser"), LEDS("LED Panel"), BLINK("Blink");

	private String typeName;

	private ElementType(String typeName) {
		this.typeName = typeName;
	}

	public String getTypeName() {
		return typeName;
	}
}
