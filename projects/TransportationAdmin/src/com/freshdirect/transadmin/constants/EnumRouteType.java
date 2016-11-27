package com.freshdirect.transadmin.constants;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

public class EnumRouteType extends Enum {
	
	public static final EnumRouteType DELIVERYZONE = new EnumRouteType("SIT", "REGULARDELIVERY");
	public static final EnumRouteType CROSSDOCK = new EnumRouteType("CRD", "CROSS DOCK");
	public static final EnumRouteType DEPOTDELIVERY = new EnumRouteType("DPT", "REGULARDEPOT");

	private final String displayName;
	
	public EnumRouteType(String name, String displayName) {
		super(name);
		this.displayName = displayName;
	}
	
	public static EnumDispatchStatusType getEnum(String name) {
		return (EnumDispatchStatusType) getEnum(EnumRouteType.class, name);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumRouteType.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumRouteType.class);
	}

	public static Iterator iterator() {
		return iterator(EnumRouteType.class);
	}

	public String toString() {
		return this.getName();
	}

	public String getDisplayName() {
		return displayName;
	}

}
