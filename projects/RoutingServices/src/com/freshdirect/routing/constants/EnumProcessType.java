package com.freshdirect.routing.constants;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

public class EnumProcessType extends Enum  {
	
	public static final EnumProcessType LOAD_CARTONCOUNT = new EnumProcessType("lcc", "Load ERP Carton Count","ERP carton count request failed. Using default");
	public static final EnumProcessType LOAD_ZONETYPE = new EnumProcessType("lzt", "Load Zone Type","Zone type not found. Using default");
	public static final EnumProcessType LOAD_LOCATIONINFO = new EnumProcessType("lli", "Load Location Info","Location not found. New location added");
	public static final EnumProcessType LOAD_LOCATIONGEOCODE = new EnumProcessType("llg", "Load Location Geocode","Unable to geocode using routing system. Using storefront geocode engine");
	public static final EnumProcessType LOAD_SERVICETIMETYPE = new EnumProcessType("lst", "Load Service Time Type","Service time type not found. Using default");
	public static final EnumProcessType LOAD_SERVICETIME = new EnumProcessType("lss", "Load Service Time","Service time not found. Using default");
	public static final EnumProcessType CREATE_ROUTINGSESSION = new EnumProcessType("crs", "Create Routing Session","Create Routing Session");
	public static final EnumProcessType UNASSINGED_ROUTINGBULK = new EnumProcessType("crb", "Bulk Routing Unassigned","Bulk Routing Unassigned");

	private final String description;
	
	private final String processError;
	
	public EnumProcessType(String code, String description, String processError) {
		super(code);
		this.description = description;
		this.processError = processError;		
	}
	
	public static EnumProcessType getEnum(String code) {
		return (EnumProcessType) getEnum(EnumProcessType.class, code);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumProcessType.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumProcessType.class);
	}

	public static Iterator iterator() {
		return iterator(EnumProcessType.class);
	}

	public String toString() {
		return this.getName();
	}

	public String getDescription() {
		return description;
	}
	
	public String getProcessError() {
		return processError;
	}

}
