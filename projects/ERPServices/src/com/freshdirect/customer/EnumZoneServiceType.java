package com.freshdirect.customer;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

public class EnumZoneServiceType extends Enum{

	private final String description;
	    public String getCode() {
		  return code;
	}

	private String code; 
	
	public final static EnumZoneServiceType ALL	= new EnumZoneServiceType("00", "ALL", "Default Zone");
	public final static EnumZoneServiceType RES	= new EnumZoneServiceType("01", "RES", "Residential Zone");
	public final static EnumZoneServiceType CORP = new EnumZoneServiceType("02", "COR", "Corporate Zone");
			
	private EnumZoneServiceType(String code, String name, String description) {
		super(name);
		this.code= code;		
		this.description = description;
	}
	

	public String getDescription() {
		return this.description;
	}	

	public static EnumZoneServiceType getEnum(String name) {
		return (EnumZoneServiceType) getEnum(EnumZoneServiceType.class, name);
	}
	
	public static EnumZoneServiceType  getEnumByCode(String code){
		List list=getEnumList();
		for(int i=0;i<list.size();i++){
			EnumZoneServiceType serviceTypeTmp=(EnumZoneServiceType)list.get(i);
			if(serviceTypeTmp.getCode().equalsIgnoreCase(code))  return serviceTypeTmp;
		}
		return null;
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumZoneServiceType.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumZoneServiceType.class);
	}

	public String toString() {
		return this.getName();
	}

	
}
