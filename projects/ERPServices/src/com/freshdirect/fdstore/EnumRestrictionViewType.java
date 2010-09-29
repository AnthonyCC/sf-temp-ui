package com.freshdirect.fdstore;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;
public class EnumRestrictionViewType extends Enum {

	public static final EnumRestrictionViewType RESERVATION = new EnumRestrictionViewType("RSV", "RESERVATION");
	public static final EnumRestrictionViewType CHECKOUT = new EnumRestrictionViewType("CHK", "CHECK-OUT");
	public static final EnumRestrictionViewType AVAILABLE = new EnumRestrictionViewType("AVL", "AVAILABLE");
	
	private final String description;
	private EnumRestrictionViewType(String name, String description) {
		super(name);
		this.description = description;		
	}
	public String getDescription() {
		return description;
	}
	
	public static EnumRestrictionViewType getEnum(String name) {
		return (EnumRestrictionViewType) getEnum(EnumRestrictionViewType.class, name);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumRestrictionViewType.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumRestrictionViewType.class);
	}

	public static Iterator iterator() {
		return iterator(EnumRestrictionViewType.class);
	}
}
