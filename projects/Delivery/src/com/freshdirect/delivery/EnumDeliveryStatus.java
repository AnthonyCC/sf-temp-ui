package com.freshdirect.delivery;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enum.Enum;

public class EnumDeliveryStatus extends Enum {
	
	public static final EnumDeliveryStatus DELIVER = new EnumDeliveryStatus("Deliver");
	public static final EnumDeliveryStatus DONOT_DELIVER = new EnumDeliveryStatus("Donot Deliver");
	public static final EnumDeliveryStatus PARTIALLY_DELIVER = new EnumDeliveryStatus("Partially Deliver");

	private EnumDeliveryStatus(String name) {
		super(name);
	}

	public static EnumDeliveryStatus getEnum(String service) {
		return (EnumDeliveryStatus) getEnum(EnumDeliveryStatus.class, service);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumDeliveryStatus.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumDeliveryStatus.class);
	}

	public static Iterator iterator() {
		return iterator(EnumDeliveryStatus.class);
	}
}
