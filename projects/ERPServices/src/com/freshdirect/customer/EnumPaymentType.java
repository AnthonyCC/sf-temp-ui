package com.freshdirect.customer;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enum.Enum;

public class EnumPaymentType extends Enum {

	public static final EnumPaymentType REGULAR = new EnumPaymentType("R", "Regular");
	public static final EnumPaymentType ON_FD_ACCOUNT = new EnumPaymentType("X", "On FD Account");
	public static final EnumPaymentType MAKE_GOOD = new EnumPaymentType("M", "Make good");

	private final String description;

	public EnumPaymentType(String name, String description) {
		super(name);
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	public static EnumPaymentType getEnum(String name) {
		return (EnumPaymentType) getEnum(EnumPaymentType.class, name);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumPaymentType.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumPaymentType.class);
	}

	public static Iterator iterator() {
		return iterator(EnumPaymentType.class);
	}

	public String toString() {
		return this.getName();
	}
}