package com.freshdirect.common.customer;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

public class EnumServiceType extends Enum {

	public static final EnumServiceType HOME = new EnumServiceType("HOME");
	public static final EnumServiceType CORPORATE = new EnumServiceType("CORPORATE");
	public static final EnumServiceType DEPOT = new EnumServiceType("DEPOT");
	public static final EnumServiceType PICKUP = new EnumServiceType("PICKUP");
	//Added for Gift cards.
	public static final EnumServiceType GIFT_CARD_PERSONAL = new EnumServiceType("GCP");
	public static final EnumServiceType GIFT_CARD_CORPORATE = new EnumServiceType("GCC");
	
	public static final EnumServiceType WEB = new EnumServiceType("WEB");
	
	private EnumServiceType(String name) {
		super(name);
	}

	public static EnumServiceType getEnum(String service) {
		return (EnumServiceType) getEnum(EnumServiceType.class, service);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumServiceType.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumServiceType.class);
	}

	public static Iterator iterator() {
		return iterator(EnumServiceType.class);
	}

}