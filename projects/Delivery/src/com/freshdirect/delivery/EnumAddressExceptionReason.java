package com.freshdirect.delivery;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.ValuedEnum;

/**@author ekracoff on Aug 9, 2004*/
public class EnumAddressExceptionReason extends ValuedEnum {

	public final static EnumAddressExceptionReason BLDG_CHANGE = new EnumAddressExceptionReason("BLDG_CHANGE", "Change Building Type", 1);
	public final static EnumAddressExceptionReason ADD_APT = new EnumAddressExceptionReason("ADD_APT", "Add Apartment", 2);
	public final static EnumAddressExceptionReason ADD_BLDG = new EnumAddressExceptionReason("ADD_BLDG", "Add Building", 3);
	public final static EnumAddressExceptionReason ZIP_CHANGE = new EnumAddressExceptionReason("ZIP_CHANGE", "Change Zipcode of Building", 4);
	public final static EnumAddressExceptionReason OTHER = new EnumAddressExceptionReason("OTHER", "Other", 5);
	
	private String description;
	
	private EnumAddressExceptionReason(String name, String description, int value){
		super(name, value);
		this.description = description;
	}
	
	public static EnumAddressExceptionReason getEnum(String type) {
		return (EnumAddressExceptionReason) getEnum(EnumAddressExceptionReason.class, type);
	}

	public static EnumAddressExceptionReason getEnum(int id) {
		return (EnumAddressExceptionReason) getEnum(EnumAddressExceptionReason.class, id);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumAddressExceptionReason.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumAddressExceptionReason.class);
	}

	public static Iterator iterator() {
		return iterator(EnumAddressExceptionReason.class);
	}
	
	public String getDescription(){
		return this.description;
	}

}
