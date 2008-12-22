/*
 * Created on Jun 30, 2005
 *
 */
package com.freshdirect.transadmin.util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enum.Enum;

/**
 * @author jng
 *
 */
public class EnumCachedDataType extends Enum {
	
	// DEFAULT TYPES
	public static final EnumCachedDataType TRUCK_DATA = new EnumCachedDataType("TRUCK", "Truck Data");
	public static final EnumCachedDataType ROUTE_DATA = new EnumCachedDataType("ROUTE", "Route Data");
	public static final EnumCachedDataType EMPLOYEE_DATA = new EnumCachedDataType("EMPLOYEE", "Employee Data");

	private final String description;

	public EnumCachedDataType(String name, String description) {
		super(name);
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	public static EnumCachedDataType getEnum(String name) {
		return (EnumCachedDataType) getEnum(EnumCachedDataType.class, name);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumCachedDataType.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumCachedDataType.class);
	}

	public static Iterator iterator() {
		return iterator(EnumCachedDataType.class);
	}

	public String toString() {
		return this.getName();
	}

}
