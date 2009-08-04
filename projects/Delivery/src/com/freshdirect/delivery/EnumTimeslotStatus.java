package com.freshdirect.delivery;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.ValuedEnum;

public class EnumTimeslotStatus extends ValuedEnum {
	
	public final static EnumTimeslotStatus NOT_APPLICABLE = new EnumTimeslotStatus("N/A", 0);
	public final static EnumTimeslotStatus STANDARD       = new EnumTimeslotStatus("STD", 1);
	public final static EnumTimeslotStatus PREMIUM        = new EnumTimeslotStatus("PRE", 2);

	private EnumTimeslotStatus(String name, int value) {
		super(name, value);
	}
	
	public static EnumTimeslotStatus getEnum(String status) {
		return (EnumTimeslotStatus) getEnum(EnumTimeslotStatus.class, status);
	}

	public static EnumTimeslotStatus getEnum(int status) {
		return (EnumTimeslotStatus) getEnum(EnumTimeslotStatus.class, status);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumTimeslotStatus.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumTimeslotStatus.class);
	}

	public static Iterator iterator() {
		return iterator(EnumTimeslotStatus.class);
	}

}
