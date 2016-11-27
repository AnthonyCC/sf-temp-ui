package com.freshdirect.giftcard;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

public class EnumGCDeliveryMode extends Enum {

	public static final EnumGCDeliveryMode EMAIL = new EnumGCDeliveryMode("E", "Email");
	public static final EnumGCDeliveryMode PDF = new EnumGCDeliveryMode("P", "PDF");
	
	private final String description;

	public EnumGCDeliveryMode(String name, String description) {
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