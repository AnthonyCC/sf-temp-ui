package com.freshdirect.routing.constants;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

public class EnumRoutingFlowType extends Enum {
	
	public static final EnumRoutingFlowType BULKFLOW = new EnumRoutingFlowType("BULK", "Bulk Flow");
	public static final EnumRoutingFlowType LINEARFLOW = new EnumRoutingFlowType("LINEAR", "Linear Flow");
			
	private final String description;
	
	public EnumRoutingFlowType(String code, String description) {
		super(code);
		this.description = description;
	}
	
	public static EnumRoutingFlowType getEnum(String code) {
		return (EnumRoutingFlowType) getEnum(EnumRoutingFlowType.class, code);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumRoutingFlowType.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumRoutingFlowType.class);
	}

	public static Iterator iterator() {
		return iterator(EnumRoutingFlowType.class);
	}

	public String toString() {
		return this.getName();
	}

	public String getDescription() {
		return description;
	}
}
