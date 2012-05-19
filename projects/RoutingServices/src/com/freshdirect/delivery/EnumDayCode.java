package com.freshdirect.delivery;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.ValuedEnum;

public class EnumDayCode extends ValuedEnum {

	public final static EnumDayCode MONDAY = new EnumDayCode("Monday", Calendar.MONDAY);
	public final static EnumDayCode TUESDAY = new EnumDayCode("Tuesday", Calendar.TUESDAY);
	public final static EnumDayCode WEDNESDAY = new EnumDayCode("Wednesday", Calendar.WEDNESDAY);
	public final static EnumDayCode THURSDAY = new EnumDayCode("Thursday", Calendar.THURSDAY);
	public final static EnumDayCode FRIDAY = new EnumDayCode("Friday", Calendar.FRIDAY);
	public final static EnumDayCode SATURDAY = new EnumDayCode("Saturday", Calendar.SATURDAY);
	public final static EnumDayCode SUNDAY = new EnumDayCode("Sunday", Calendar.SUNDAY);

	private EnumDayCode(String name, int value) {
		super(name, value);
	}

	public static EnumDayCode getEnum(String day) {
		return (EnumDayCode) getEnum(EnumDayCode.class, day);
	}

	public static EnumDayCode getEnum(int day) {
		return (EnumDayCode) getEnum(EnumDayCode.class, day);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumDayCode.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumDayCode.class);
	}

	public static Iterator iterator() {
		return iterator(EnumDayCode.class);
	}

}
