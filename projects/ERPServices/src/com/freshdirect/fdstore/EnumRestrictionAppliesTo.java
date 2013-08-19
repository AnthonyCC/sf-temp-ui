package com.freshdirect.fdstore;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;
public class EnumRestrictionAppliesTo extends Enum {
	public static final EnumRestrictionAppliesTo SO_INSTANCE = new EnumRestrictionAppliesTo("Instance", "Instance");
	public static final EnumRestrictionAppliesTo SO_TEMPLATE = new EnumRestrictionAppliesTo("Template", "Template");
	public static final EnumRestrictionAppliesTo BOTH = new EnumRestrictionAppliesTo("Both", "Both");
	
	private final String description;
	private EnumRestrictionAppliesTo(String name, String description) {
		super(name);
		this.description = description;		
	}
	public String getDescription() {
		return description;
	}
	
	public static EnumRestrictionAppliesTo getEnum(String name) {
		return (EnumRestrictionAppliesTo) getEnum(EnumRestrictionAppliesTo.class, name);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumRestrictionAppliesTo.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumRestrictionAppliesTo.class);
	}

	public static Iterator iterator() {
		return iterator(EnumRestrictionAppliesTo.class);
	}
}
