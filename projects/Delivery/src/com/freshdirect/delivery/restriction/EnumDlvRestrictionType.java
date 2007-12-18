package com.freshdirect.delivery.restriction;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enum.Enum;

public class EnumDlvRestrictionType extends Enum {

	public static final EnumDlvRestrictionType RECURRING_RESTRICTION = new EnumDlvRestrictionType("RRN", "Recurring Restriction");
	public static final EnumDlvRestrictionType ONE_TIME_RESTRICTION = new EnumDlvRestrictionType("OTR", "One time Restriction");

	private final String description;

	public EnumDlvRestrictionType(String name, String description) {
		super(name);
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	public static EnumDlvRestrictionType getEnum(String name) {
		return (EnumDlvRestrictionType) getEnum(EnumDlvRestrictionType.class, name);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumDlvRestrictionType.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumDlvRestrictionType.class);
	}

	public static Iterator iterator() {
		return iterator(EnumDlvRestrictionType.class);
	}

}
