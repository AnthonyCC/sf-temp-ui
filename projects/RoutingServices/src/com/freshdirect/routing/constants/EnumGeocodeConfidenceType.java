package com.freshdirect.routing.constants;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

public class EnumGeocodeConfidenceType extends Enum {
	
	public static final EnumGeocodeConfidenceType LOW = new EnumGeocodeConfidenceType("gcLow", "Low Confidence");
	public static final EnumGeocodeConfidenceType MEDIUM = new EnumGeocodeConfidenceType("gcMedium", "Medium Confidence");
	public static final EnumGeocodeConfidenceType HIGH = new EnumGeocodeConfidenceType("gcHigh", "High Confidence");

	private final String description;
	
	public EnumGeocodeConfidenceType(String code, String description) {
		super(code);
		this.description = description;
	}
	
	public static EnumGeocodeConfidenceType getEnum(String code) {
		return (EnumGeocodeConfidenceType) getEnum(EnumGeocodeConfidenceType.class, code);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumGeocodeConfidenceType.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumGeocodeConfidenceType.class);
	}

	public static Iterator iterator() {
		return iterator(EnumGeocodeConfidenceType.class);
	}

	public String toString() {
		return this.getName();
	}

	public String getDescription() {
		return description;
	}

}

