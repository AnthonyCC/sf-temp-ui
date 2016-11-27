package com.freshdirect.transadmin.constants;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

public class EnumCrisisMngBatchReportType  extends Enum {
	
	public static final EnumCrisisMngBatchReportType MARKETING = new EnumCrisisMngBatchReportType("MKR","MARKETING");

    public static final EnumCrisisMngBatchReportType VOICESHOT = new EnumCrisisMngBatchReportType("VSR","VOICESHOT");
    
    public static final EnumCrisisMngBatchReportType TIMESLOTEXCEPTION = new EnumCrisisMngBatchReportType("TSR","TIMESLOTEXCEPTION");
    
    public static final EnumCrisisMngBatchReportType SOSIMULATIONREPORT = new EnumCrisisMngBatchReportType("SSR","SOSIMULATIONREPORT");

    public static final EnumCrisisMngBatchReportType SOFAILUREREPORT = new EnumCrisisMngBatchReportType("SFR","SOFAILUREREPORT");

    private final String description;

	public EnumCrisisMngBatchReportType(String name, String description) {
		super(name);
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	public static EnumCrisisMngBatchReportType getEnum(String name) {
		return (EnumCrisisMngBatchReportType) getEnum(EnumCrisisMngBatchReportType.class, name);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumCrisisMngBatchReportType.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumCrisisMngBatchReportType.class);
	}

	public static Iterator iterator() {
		return iterator(EnumCrisisMngBatchReportType.class);
	}

	public String toString() {
		return this.getName();
	}
}
