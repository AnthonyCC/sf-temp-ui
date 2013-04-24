package com.freshdirect.transadmin.constants;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;


public class EnumAssetScanStatus  extends Enum {
	
	public static final EnumAssetScanStatus CHECK_IN = new EnumAssetScanStatus("IN","Check-In");

    public static final EnumAssetScanStatus CHECK_OUT = new EnumAssetScanStatus("OUT","Check-Out");
        
    private final String description;

	public EnumAssetScanStatus(String name, String description) {
		super(name);
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	public static EnumAssetScanStatus getEnum(String name) {
		return (EnumAssetScanStatus) getEnum(EnumAssetScanStatus.class, name);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumAssetScanStatus.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumAssetScanStatus.class);
	}

	public static Iterator iterator() {
		return iterator(EnumAssetScanStatus.class);
	}

	public String toString() {
		return this.getName();
	}
	
	public static EnumAssetScanStatus getEnumByDesc(String desc) {
		
		for(Object _option :  EnumAssetScanStatus.getEnumList()){
			if(((EnumAssetScanStatus) _option).getDescription().equalsIgnoreCase(desc)){
				return (EnumAssetScanStatus) _option;
			}
		}
		return null;
	}
}
