package com.freshdirect.routing.constants;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

public class EnumProcessInfoType extends Enum {
	
	public static final EnumProcessInfoType ERROR = new EnumProcessInfoType("Error", "Error During Process");
	public static final EnumProcessInfoType WARNING = new EnumProcessInfoType("Warning", "Check the process step");
	public static final EnumProcessInfoType INFO = new EnumProcessInfoType("Info", "Information about process step");
	

	private final String description;
	
	public EnumProcessInfoType(String code, String description) {
		super(code);
		this.description = description;
	}
	
	public static EnumProcessInfoType getEnum(String code) {
		return (EnumProcessInfoType) getEnum(EnumProcessInfoType.class, code);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumProcessInfoType.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumProcessInfoType.class);
	}

	public static Iterator iterator() {
		return iterator(EnumProcessInfoType.class);
	}

	public String toString() {
		return this.getName();
	}

	public String getDescription() {
		return description;
	}
}
