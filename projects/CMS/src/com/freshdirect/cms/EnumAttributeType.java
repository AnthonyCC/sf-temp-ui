package com.freshdirect.cms;

/**
 * Enumeration of all attribute types.
 */
public enum EnumAttributeType {
    BOOLEAN("B", "Boolean", Boolean.FALSE),
    INTEGER("I", "Integer", Integer.valueOf(0)),
    DOUBLE("D", "Double", Double.valueOf(0)),
    STRING("S", "String", ""),
    LONG_TEXT("TXT", "Text", ""),
    RELATIONSHIP("R", "Relationship", null),
    TABLE("T", "Table", null),
    ENUM("E", "Enumeration", null),
    DATE("DT", "Date", null),
    WYSIWYG("WY", "String", ""),
    TIME("TS", "Time", null)
    ;
    
    final String name;
    final String label;
    final Object emptyValue;

	/**
	 * Constructor.
	 * 
	 * @param name a unique name for the value
	 * @param label a human-readble name of the value
	 * @param emptyValue a value that the enum takes if it is considered to be empty.
	 */
	EnumAttributeType(String name, String label, Object emptyValue) {
	    this.name = name;
		this.label = label;
		this.emptyValue = emptyValue;
	}

	
    public String getName() {
        return name;
    }

	public String getLabel() {
		return label;
	}

	public Object getEmptyValue() {
		return emptyValue;
	}


	@Deprecated
    public static EnumAttributeType getEnum(String name) {
        if (name == null)
            return null;

        for (EnumAttributeType e : values()) {
            if (e.getName().equalsIgnoreCase(name)) {
                return e;
            }
        }
        return null;
    }
}
