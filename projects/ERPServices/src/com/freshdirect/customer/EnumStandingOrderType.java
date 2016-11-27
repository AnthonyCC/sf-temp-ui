package com.freshdirect.customer;


import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

public class EnumStandingOrderType  extends Enum {

	private static final long serialVersionUID = -8712073933321321080L;

	public static final EnumStandingOrderType SO_SKU_REPLACEMENT = new EnumStandingOrderType("SO_SKU_REPLACEMENT", "SO SKU REPLACEMENT");
	
	private final String displayName;
	
	private EnumStandingOrderType(String name, String displayName) {
		super(name);
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public static EnumStandingOrderType getEnum(String name) {
		return (EnumStandingOrderType) getEnum(EnumStandingOrderType.class, name);
	}

	@SuppressWarnings( "unchecked" )
	public static Map<String,EnumStandingOrderType> getEnumMap() {
		return getEnumMap(EnumStandingOrderType.class);
	}

	@SuppressWarnings( "unchecked" )
	public static List<EnumStandingOrderType> getEnumList() {
		return getEnumList(EnumStandingOrderType.class);
	}

	@SuppressWarnings( "unchecked" )
	public static Iterator<EnumStandingOrderType> iterator() {
		return iterator(EnumStandingOrderType.class);
	}
}
