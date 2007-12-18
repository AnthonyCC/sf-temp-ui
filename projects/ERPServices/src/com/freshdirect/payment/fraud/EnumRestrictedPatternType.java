package com.freshdirect.payment.fraud;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enum.ValuedEnum;

public class EnumRestrictedPatternType extends ValuedEnum {

	public final static EnumRestrictedPatternType EQUALS = new EnumRestrictedPatternType("EQ", "Equals to", 0);
	public final static EnumRestrictedPatternType STARTS_WITH = new EnumRestrictedPatternType("SW", "Starts with", 1);
	public final static EnumRestrictedPatternType ENDS_WITH = new EnumRestrictedPatternType("EW", "Ends with", 2);
	public final static EnumRestrictedPatternType CONTAINS = new EnumRestrictedPatternType("CT", "Contains", 3);

	private String description;
	
	private EnumRestrictedPatternType(String code, String description, int id) {
		super(code, id);
		this.description = description;
	}
	
	public static EnumRestrictedPatternType getEnum(String code) {
		return (EnumRestrictedPatternType) getEnum(EnumRestrictedPatternType.class, code);
	}

	public static EnumRestrictedPatternType getEnum(int id) {
		return (EnumRestrictedPatternType) getEnum(EnumRestrictedPatternType.class, id);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumRestrictedPatternType.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumRestrictedPatternType.class);
	}

	public static Iterator iterator() {
		return iterator(EnumRestrictedPatternType.class);
	}
	
	public String getDescription(){
		return this.description;
	}
	
}
