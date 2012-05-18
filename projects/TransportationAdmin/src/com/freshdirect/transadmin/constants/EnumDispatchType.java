package com.freshdirect.transadmin.constants;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

@SuppressWarnings("serial")
public class EnumDispatchType extends Enum {

	public static final EnumDispatchType ROUTEDISPATCH = new EnumDispatchType("RGD", "Route Dispatch");
	public static final EnumDispatchType LIGHTDUTYDISPATCH = new EnumDispatchType("LDD", "Light Duty Dispatch");

	private final String desc;
	
	public EnumDispatchType(String name, String desc) {
		super(name);
		this.desc = desc;
	}
	
	public static EnumDispatchType getEnum(String name) {
		return (EnumDispatchType) getEnum(EnumDispatchType.class, name);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumDispatchType.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumDispatchType.class);
	}

	public static Iterator iterator() {
		return iterator(EnumDispatchType.class);
	}

	public String toString() {
		return this.getName();
	}

	public String getDesc() {
		return desc;
	}	
}
