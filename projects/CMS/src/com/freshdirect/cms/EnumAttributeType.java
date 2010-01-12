package com.freshdirect.cms;

import org.apache.commons.lang.enums.Enum;

/**
 * Enumeration of all attribute types.
 */
public class EnumAttributeType extends Enum {
	private static final long serialVersionUID = 636327470114226135L;

	public static final EnumAttributeType BOOLEAN = new EnumAttributeType("B", "Boolean", Boolean.FALSE);
	public static final EnumAttributeType INTEGER = new EnumAttributeType("I", "Integer", new Integer(0));
	public static final EnumAttributeType DOUBLE = new EnumAttributeType("D", "Double", new Double(0));
	public static final EnumAttributeType STRING = new EnumAttributeType("S", "String", "");
	public static final EnumAttributeType RELATIONSHIP = new EnumAttributeType("R", "Relationship", null);
	public static final EnumAttributeType TABLE = new EnumAttributeType("T", "Table", null);
	public static final EnumAttributeType ENUM = new EnumAttributeType("E", "Enumeration", null);
	public static final EnumAttributeType DATE = new EnumAttributeType("DT", "Date", null);

	private final String label;
	private final Object emptyValue;

	/**
	 * Constructor.
	 * 
	 * @param name a unique name for the value
	 * @param label a human-readble name of the value
	 * @param emptyValue a value that the enum takes if it is considered to be empty.
	 */
	private EnumAttributeType(String name, String label, Object emptyValue) {
		super(name);
		this.label = label;
		this.emptyValue = emptyValue;
	}

	public String getLabel() {
		return label;
	}

	public Object getEmptyValue() {
		return emptyValue;
	}

	public static EnumAttributeType getEnum(String name) {
		return (EnumAttributeType) getEnum(EnumAttributeType.class, name);
	}
}
