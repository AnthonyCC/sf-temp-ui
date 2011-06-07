package com.freshdirect.routing.constants;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

public class EnumTruckPreference  extends Enum {
	
	public static final EnumTruckPreference TRUCK_PREF_01 = new EnumTruckPreference("TP1","TRUCK_PREFERENCE01");

    public static final EnumTruckPreference TRUCK_PREF_02 = new EnumTruckPreference("TP2","TRUCK_PREFERENCE02");
    
    public static final EnumTruckPreference TRUCK_PREF_03 = new EnumTruckPreference("TP3","TRUCK_PREFERENCE03");
    
    public static final EnumTruckPreference TRUCK_PREF_04 = new EnumTruckPreference("TP4","TRUCK_PREFERENCE04");
    
    public static final EnumTruckPreference TRUCK_PREF_05 = new EnumTruckPreference("TP5","TRUCK_PREFERENCE05");
    
    private final String description;

	public EnumTruckPreference(String name, String description) {
		super(name);
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	public static EnumTruckPreference getEnum(String name) {
		return (EnumTruckPreference) getEnum(EnumTruckPreference.class, name);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumTruckPreference.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumTruckPreference.class);
	}

	public static Iterator iterator() {
		return iterator(EnumTruckPreference.class);
	}

	public String toString() {
		return this.getName();
	}
}
