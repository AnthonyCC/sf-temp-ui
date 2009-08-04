package com.freshdirect.cms;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

/**
 * Enumeration of relationship destination cardinalities (One or Many).
 */
public class EnumCardinality extends Enum {

	public static EnumCardinality ONE = new EnumCardinality("One");
	public static EnumCardinality MANY = new EnumCardinality("Many");

	private EnumCardinality(String name) {
		super(name);
	}

	public static EnumCardinality getEnum(String name) {
		return (EnumCardinality) getEnum(EnumCardinality.class, name);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumCardinality.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumCardinality.class);
	}

}