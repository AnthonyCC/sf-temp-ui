package com.freshdirect.transadmin.constants;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

public class EnumDispatchStatusType extends Enum {
	
	public static final EnumDispatchStatusType CONFIRMED = new EnumDispatchStatusType("Confirmed", "Confirmed");
	public static final EnumDispatchStatusType NOTCONFIRMED = new EnumDispatchStatusType("NotConfirmed", "Not Confirmed");

	private final String description;
	
	public EnumDispatchStatusType(String name, String description) {
		super(name);
		this.description = description;
	}
	
	public static EnumDispatchStatusType getEnum(String name) {
		return (EnumDispatchStatusType) getEnum(EnumDispatchStatusType.class, name);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumDispatchStatusType.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumDispatchStatusType.class);
	}

	public static Iterator iterator() {
		return iterator(EnumDispatchStatusType.class);
	}

	public String toString() {
		return this.getName();
	}

	public String getDescription() {
		return description;
	}

}
