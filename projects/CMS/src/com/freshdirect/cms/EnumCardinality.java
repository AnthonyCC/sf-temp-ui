package com.freshdirect.cms;

/**
 * Enumeration of relationship destination cardinalities (One or Many).
 */
public enum EnumCardinality {

	ONE("One"),
	MANY("Many")
	;

    final String name;

	private EnumCardinality(String name) {
		this.name = name;
	}

    public String getName() {
        return name;
    }
	
    @Deprecated
    public static EnumCardinality getEnum(String name) {
        if (name == null)
            return null;

        for (EnumCardinality e : values()) {
            if (e.getName().equalsIgnoreCase(name)) {
                return e;
            }
        }
        return null;
    }
}
