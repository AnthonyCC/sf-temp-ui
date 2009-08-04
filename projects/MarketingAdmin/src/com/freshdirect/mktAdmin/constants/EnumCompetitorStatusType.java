package com.freshdirect.mktAdmin.constants;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

public class EnumCompetitorStatusType extends Enum {

	public static final EnumCompetitorStatusType ACTIVE = new EnumCompetitorStatusType("Active", "Active");
	public static final EnumCompetitorStatusType INACTIVE = new EnumCompetitorStatusType("Inactive", "Inactive");

	private final String description;
	
	public EnumCompetitorStatusType(String name, String description) {
		super(name);
		this.description = description;
	}
	
	public static EnumCompetitorStatusType getEnum(String name) {
		return (EnumCompetitorStatusType) getEnum(EnumCompetitorStatusType.class, name);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumCompetitorStatusType.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumCompetitorStatusType.class);
	}

	public static Iterator iterator() {
		return iterator(EnumCompetitorStatusType.class);
	}

	public String toString() {
		return this.getName();
	}
	
}
