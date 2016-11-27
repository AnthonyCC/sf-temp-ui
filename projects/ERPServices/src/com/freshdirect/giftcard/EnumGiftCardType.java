package com.freshdirect.giftcard;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

public class EnumGiftCardType extends Enum {

	public static final EnumGiftCardType REGULAR_GIFTCARD = new EnumGiftCardType("RGC", "Regular GiftCard");
	
	public static final EnumGiftCardType DONATION_GIFTCARD = new EnumGiftCardType("DGC", "Donation GiftCard");
	
	private final String description;

	public EnumGiftCardType(String name, String description) {
		super(name);
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	public static EnumGiftCardType getEnum(String name) {
		return (EnumGiftCardType) getEnum(EnumGiftCardType.class, name);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumGiftCardType.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumGiftCardType.class);
	}

	public static Iterator iterator() {
		return iterator(EnumGiftCardType.class);
	}
}