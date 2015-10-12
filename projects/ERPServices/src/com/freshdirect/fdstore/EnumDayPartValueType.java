package com.freshdirect.fdstore;

import org.apache.commons.lang.enums.Enum;
import org.apache.log4j.Category;

import com.freshdirect.framework.util.log.LoggerFactory;

public class EnumDayPartValueType extends Enum {
		
	public static final EnumDayPartValueType DPR1 = new EnumDayPartValueType("DPR1", "DayPart Restriction 1");
	public static final EnumDayPartValueType DPR2 = new EnumDayPartValueType("DPR2", "DayPart Restriction 2");
	public static final EnumDayPartValueType DPR3 = new EnumDayPartValueType("DPR3", "DayPart Restriction 3");
	public static final EnumDayPartValueType DPR4 = new EnumDayPartValueType("DPR4", "DayPart Restriction 4");
	public static final EnumDayPartValueType DPR5 = new EnumDayPartValueType("DPR5", "DayPart Restriction 5");
	
	private static Category LOGGER = LoggerFactory.getInstance(EnumDayPartValueType.class);

	private final String description;

	private EnumDayPartValueType(String name, String description) {
		super(name);
		this.description = description;
	}
	
	public static EnumDayPartValueType getEnum(String name) {
		return (EnumDayPartValueType) getEnum(EnumDayPartValueType.class, name);
	}
}
