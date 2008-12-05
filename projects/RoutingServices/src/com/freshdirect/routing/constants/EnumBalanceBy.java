package com.freshdirect.routing.constants;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

public class EnumBalanceBy extends Enum {
	
	public static final EnumBalanceBy NUMSTOPS = new EnumBalanceBy("sbfNumStops", "No Of Stops");
	public static final EnumBalanceBy QUANTITY = new EnumBalanceBy("sbfQuantity", "Quantity");
	public static final EnumBalanceBy COST = new EnumBalanceBy("sbfCost", "Cost");
	public static final EnumBalanceBy WORKINGTIME = new EnumBalanceBy("sbfWorkingTime", "Working Time");
	
	private final String description;
	
	public EnumBalanceBy(String code, String description) {
		super(code);
		this.description = description;
	}
	
	public static EnumBalanceBy getEnum(String code) {
		return (EnumBalanceBy) getEnum(EnumBalanceBy.class, code);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumBalanceBy.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumBalanceBy.class);
	}

	public static Iterator iterator() {
		return iterator(EnumBalanceBy.class);
	}

	public String toString() {
		return this.getName();
	}

	public String getDescription() {
		return description;
	}

}

