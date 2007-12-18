package com.freshdirect.customer;


import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enum.Enum;

public class EnumUnattendedDeliveryFlag extends Enum {
	
	// DEFAULT TYPES
	public static final EnumUnattendedDeliveryFlag NOT_SEEN = new EnumUnattendedDeliveryFlag("NOT_SEEN", "Never seen");
	public static final EnumUnattendedDeliveryFlag OPT_IN = new EnumUnattendedDeliveryFlag("OPT_IN", "Opted in");
	public static final EnumUnattendedDeliveryFlag OPT_OUT = new EnumUnattendedDeliveryFlag("OPT_OUT", "Opted out");
	public static final EnumUnattendedDeliveryFlag DISCARD_OPT_IN = new EnumUnattendedDeliveryFlag("DISCARD_OPT_IN", "Discard opt in");
	
	private String description;
	
	public EnumUnattendedDeliveryFlag(String name, String description) {
		super(name);
		this.description = description;
	}
	
	public String getDescription() {
		return this.description;
	}

	public static EnumUnattendedDeliveryFlag getEnum(String name) {
		return (EnumUnattendedDeliveryFlag) getEnum(EnumAlertType.class, name);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumUnattendedDeliveryFlag.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumUnattendedDeliveryFlag.class);
	}

	public static Iterator iterator() {
		return iterator(EnumUnattendedDeliveryFlag.class);
	}

	public String toString() {
		return this.getName();
	}
	
	public String toSQLValue() throws IllegalStateException {
		if (equals(NOT_SEEN)) return null;
		else if (equals(OPT_IN)) return "0";
		else if (equals(OPT_OUT)) return "1";
		else if (equals(DISCARD_OPT_IN)) return "0";
		throw new IllegalStateException("Invalid state, name = " + getName());
	}
	
	public static EnumUnattendedDeliveryFlag fromSQLValue(String sqlValue) throws IllegalStateException {
		if (sqlValue == null) return NOT_SEEN;
		else if (sqlValue.equals("0")) return OPT_IN;
		else if (sqlValue.equals("1")) return OPT_OUT;	
		throw new IllegalStateException("Invalid SQL value " + sqlValue + " for unattended delivery flag");
	}
}
