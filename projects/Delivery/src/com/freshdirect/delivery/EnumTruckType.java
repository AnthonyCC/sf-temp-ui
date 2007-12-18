package com.freshdirect.delivery;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enum.ValuedEnum;

public class EnumTruckType extends ValuedEnum {

	public final static EnumTruckType TYPE_A = new EnumTruckType("Truck", 0);
	//public final static EnumTruckType TYPE_A = new EnumTruckType("15 Feet", 0);
	//public final static EnumTruckType TYPE_B = new EnumTruckType("20 Feet", 1);

	private EnumTruckType(String name, int value) {
		super(name, value);
	}
	
	public static EnumTruckType getEnum(String type) {
		return (EnumTruckType) getEnum(EnumTruckType.class, type);
	}

	public static EnumTruckType getEnum(int type) {
		return (EnumTruckType) getEnum(EnumTruckType.class, type);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumTruckType.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumTruckType.class);
	}

	public static Iterator iterator() {
		return iterator(EnumTruckType.class);
	}
}
