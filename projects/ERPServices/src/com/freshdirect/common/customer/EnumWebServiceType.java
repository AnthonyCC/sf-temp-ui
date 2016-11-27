package com.freshdirect.common.customer;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

public class EnumWebServiceType extends Enum {

	//Added for Gift cards.
	public static final EnumWebServiceType GIFT_CARD_PERSONAL = new EnumWebServiceType("GP");
	public static final EnumWebServiceType GIFT_CARD_CORPORATE = new EnumWebServiceType("GC");
	
	//Added for Robin Hood - Donation.
	public static final EnumWebServiceType DONATION_INDIVIDUAL = new EnumWebServiceType("DI");
	public static final EnumWebServiceType DONATION_BUSINESS = new EnumWebServiceType("DB");
	
	
	private EnumWebServiceType(String name) {
		super(name);
	}

	public static EnumWebServiceType getEnum(String service) {
		return (EnumWebServiceType) getEnum(EnumWebServiceType.class, service);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumWebServiceType.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumWebServiceType.class);
	}

	public static Iterator iterator() {
		return iterator(EnumWebServiceType.class);
	}

}