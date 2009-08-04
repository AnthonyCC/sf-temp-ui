package com.freshdirect.framework.util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

public class EnumMonth extends Enum {

	public static final EnumMonth JAN = new EnumMonth("1", "JAN");
	public static final EnumMonth FEB = new EnumMonth("2", "FEB");
	public static final EnumMonth MAR = new EnumMonth("3", "MAR");
	public static final EnumMonth APR = new EnumMonth("4", "APR");
	public static final EnumMonth MAY = new EnumMonth("5", "MAY");
	public static final EnumMonth JUN = new EnumMonth("6", "JUN");
	public static final EnumMonth JUL = new EnumMonth("7", "JUL");
	public static final EnumMonth AUG = new EnumMonth("8", "AUG");
	public static final EnumMonth SEP = new EnumMonth("9", "SEP");
	public static final EnumMonth OCT = new EnumMonth("10", "OCT");
	public static final EnumMonth NOV = new EnumMonth("11", "NOV");
	public static final EnumMonth DEC = new EnumMonth("12", "DEC");
	    
	private final String description;

	public EnumMonth(String name, String description) {
		super(name);
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	public static EnumMonth getEnum(String name) {
		return (EnumMonth) getEnum(EnumMonth.class, name);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumMonth.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumMonth.class);
	}

	public static Iterator iterator() {
		return iterator(EnumMonth.class);
	}

}
