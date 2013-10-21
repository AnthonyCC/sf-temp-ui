package com.freshdirect.transadmin.constants;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

public class EnumAssetStatus  extends Enum {
	
	public static final EnumAssetStatus ACTIVE = new EnumAssetStatus("ACT","Active");

    public static final EnumAssetStatus INCOMING = new EnumAssetStatus("INC","Incoming");
    
    public static final EnumAssetStatus DECOMMISSIONED = new EnumAssetStatus("DCM","Decommissioned");
    
    public static final EnumAssetStatus OUTSERVICE = new EnumAssetStatus("OOS","OOS");
    
    
    private final String description;

	public EnumAssetStatus(String name, String description) {
		super(name);
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	public static EnumAssetStatus getEnum(String name) {
		return (EnumAssetStatus) getEnum(EnumAssetStatus.class, name);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumAssetStatus.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumAssetStatus.class);
	}

	public static Iterator iterator() {
		return iterator(EnumAssetStatus.class);
	}

	public String toString() {
		return this.getName();
	}
	
	public static EnumAssetStatus getEnumByDesc(String desc) {
		
		for(Object _option :  EnumAssetStatus.getEnumList()){
			if(((EnumAssetStatus) _option).getDescription().equalsIgnoreCase(desc)){
				return (EnumAssetStatus) _option;
			}
		}
		return null;
	}
}
