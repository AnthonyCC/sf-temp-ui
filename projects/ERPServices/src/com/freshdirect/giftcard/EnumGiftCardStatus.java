package com.freshdirect.giftcard;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

public class EnumGiftCardStatus extends Enum {

	public static final EnumGiftCardStatus UNKNOWN = new EnumGiftCardStatus("U", "Unknown");
	public static final EnumGiftCardStatus ACTIVE = new EnumGiftCardStatus("A", "Active");
	public static final EnumGiftCardStatus INACTIVE = new EnumGiftCardStatus("I", "InActive");
	public static final EnumGiftCardStatus ZERO_BALANCE = new EnumGiftCardStatus("Z", "Zero");
	
	private final String description;

	public EnumGiftCardStatus(String name, String description) {
		super(name);
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	public static EnumGCDeliveryMode getEnum(String name) {
		return (EnumGCDeliveryMode) getEnum(EnumGCDeliveryMode.class, name);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumGCDeliveryMode.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumGCDeliveryMode.class);
	}

	public static Iterator iterator() {
		return iterator(EnumGCDeliveryMode.class);
	}
}