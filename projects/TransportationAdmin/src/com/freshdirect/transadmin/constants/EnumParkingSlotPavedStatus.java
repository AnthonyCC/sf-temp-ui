package com.freshdirect.transadmin.constants;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

public class EnumParkingSlotPavedStatus  extends Enum {
		
    public static final EnumParkingSlotPavedStatus PAVED = new EnumParkingSlotPavedStatus("Paved","Paved");
    
    public static final EnumParkingSlotPavedStatus NOTPAVED = new EnumParkingSlotPavedStatus("Not Paved","Not Paved");
      
    private final String description;

	public EnumParkingSlotPavedStatus(String name, String description) {
		super(name);
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	public static EnumParkingSlotPavedStatus getEnum(String name) {
		return (EnumParkingSlotPavedStatus) getEnum(EnumParkingSlotPavedStatus.class, name);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumParkingSlotPavedStatus.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumParkingSlotPavedStatus.class);
	}

	public static Iterator iterator() {
		return iterator(EnumParkingSlotPavedStatus.class);
	}

	public String toString() {
		return this.getName();
	}
}
