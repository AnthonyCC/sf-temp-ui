package com.freshdirect.delivery;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

public class EnumRegionServiceType extends Enum {
	
	public static final EnumRegionServiceType HOME = new EnumRegionServiceType("HOME");
	public static final EnumRegionServiceType CORPORATE = new EnumRegionServiceType("CORPORATE");
	public static final EnumRegionServiceType HYBRID = new EnumRegionServiceType("HYBRID");

	public EnumRegionServiceType(String name) {
		super(name);
	}

	public static EnumRegionServiceType getEnum(String name) {
		if(name!=null)
			return (EnumRegionServiceType) getEnum(EnumRegionServiceType.class, name);

		return null;
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumRegionServiceType.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumRegionServiceType.class);
	}

	public static Iterator iterator() {
		return iterator(EnumRegionServiceType.class);
	}
	
	public static boolean isHybrid(EnumRegionServiceType serviceType){
		return EnumRegionServiceType.HYBRID.equals(serviceType);
	}
	
	public static boolean isRegular(EnumRegionServiceType serviceType){
		return EnumRegionServiceType.HOME.equals(serviceType) || EnumRegionServiceType.CORPORATE.equals(serviceType);
	}
}
