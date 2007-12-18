package com.freshdirect.delivery.announcement;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enum.Enum;

public class EnumUserDeliveryStatus extends Enum {
	
	public static final EnumUserDeliveryStatus HOME_USER = new EnumUserDeliveryStatus("HOME_USER");
	public static final EnumUserDeliveryStatus DEPOT_USER = new EnumUserDeliveryStatus("DEPOT_USER");
	public static final EnumUserDeliveryStatus PICKUP_ONLY_USER = new EnumUserDeliveryStatus("PICKUP_ONLY_USER");

	public EnumUserDeliveryStatus(String name) {
		super(name);
	}

	public static EnumUserDeliveryStatus getEnum(String name) {
		return (EnumUserDeliveryStatus) getEnum(EnumUserDeliveryStatus.class, name);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumUserDeliveryStatus.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumUserDeliveryStatus.class);
	}

	public static Iterator iterator() {
		return iterator(EnumUserDeliveryStatus.class);
	}

}
