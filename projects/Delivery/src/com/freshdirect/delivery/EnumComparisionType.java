package com.freshdirect.delivery;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

public class EnumComparisionType extends Enum implements Serializable{

	private static final long serialVersionUID = 477572236425979685L;
	
	public final static EnumComparisionType GREATER_OR_EQUAL = new EnumComparisionType("GEQ", ">=");
	public final static EnumComparisionType LESS_OR_EQUAL = new EnumComparisionType("LEQ", "<=");	
	public final static EnumComparisionType EQUAL = new EnumComparisionType("EQL", "=");
	
	private final String comparisionType;

	public EnumComparisionType(String name, String comparisionType) {
		super(name);
		this.comparisionType = comparisionType;
	}
	
	public static EnumComparisionType getEnum(String name) {
		return (EnumComparisionType) getEnum(EnumComparisionType.class, name);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumComparisionType.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumComparisionType.class);
	}

	public static Iterator iterator() {
		return iterator(EnumComparisionType.class);
	}

	public String toString() {
		return this.getName();
	}

	public String getComparisionType() {
		return comparisionType;
	}
}
