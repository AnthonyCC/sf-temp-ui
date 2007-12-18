package com.freshdirect.delivery.restriction;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enum.Enum;

public class EnumDlvRestrictionCriterion extends Enum {

	public static final EnumDlvRestrictionCriterion DELIVERY = new EnumDlvRestrictionCriterion("DELIVERY", "Restrict by delivery");
	public static final EnumDlvRestrictionCriterion CUTOFF = new EnumDlvRestrictionCriterion("CUTOFF", "Restrict by cutoff");
	public static final EnumDlvRestrictionCriterion PURCHASE = new EnumDlvRestrictionCriterion("PURCHASE", "Restrict by purchase time");

	private final String description;

	public EnumDlvRestrictionCriterion(String name, String description) {
		super(name);
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	public static EnumDlvRestrictionCriterion getEnum(String name) {
		return (EnumDlvRestrictionCriterion) getEnum(EnumDlvRestrictionCriterion.class, name);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumDlvRestrictionCriterion.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumDlvRestrictionCriterion.class);
	}

	public static Iterator iterator() {
		return iterator(EnumDlvRestrictionCriterion.class);
	}

}
