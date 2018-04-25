package com.freshdirect.customer;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public class EnumPaymentType extends Enum {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8152734574417324914L;
	public static final EnumPaymentType REGULAR = new EnumPaymentType("R", "Regular");
	public static final EnumPaymentType ON_FD_ACCOUNT = new EnumPaymentType("X", "On FD Account");
	public static final EnumPaymentType MAKE_GOOD = new EnumPaymentType("M", "Make good");
	public static final EnumPaymentType ADD_ON_ORDER = new EnumPaymentType("O", "Add on Order");

	private final String description;

	public EnumPaymentType(String name, String description) {
		super(name);
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	@JsonCreator
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

	@JsonValue
	public String toString() {
		return this.getName();
	}
}