package com.freshdirect.delivery.announcement;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

public class EnumPlacement extends Enum {

	public static final EnumPlacement HOME = new EnumPlacement("HOME", 10);
	public static final EnumPlacement DEPARTMENT = new EnumPlacement("DEPARTMENT", 20);
	public static final EnumPlacement CHECKOUT = new EnumPlacement("CHECKOUT", 30);
	public static final EnumPlacement YOUR_ACCOUNT = new EnumPlacement("YOUR_ACCOUNT", 40);
	public static final EnumPlacement DELIVERY_INFO = new EnumPlacement("DELIVERY_INFO", 50);
	public static final EnumPlacement CART = new EnumPlacement("CART", 60);
	public static final EnumPlacement TIMESLOTS = new EnumPlacement("TIMESLOTS", 70);
	public static final EnumPlacement UNKNOWN = new EnumPlacement("UNKNOWN", 0);

	private final int priority;
	
	public EnumPlacement(String name, int priority) {
		super(name);
		this.priority = priority;
	}
	
	public int getPriority(){
		return this.priority;
	}

	public static EnumPlacement getEnum(String name) {
		return (EnumPlacement) getEnum(EnumPlacement.class, name);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumPlacement.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumPlacement.class);
	}

	public static Iterator iterator() {
		return iterator(EnumPlacement.class);
	}
}
