package com.freshdirect.transadmin.constants;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

public class EnumParkingSlotBarcodeStatus  extends Enum {
	
	public static final EnumParkingSlotBarcodeStatus BARCODED = new EnumParkingSlotBarcodeStatus("Barcoded","Barcoded");

    public static final EnumParkingSlotBarcodeStatus NOBARCODE = new EnumParkingSlotBarcodeStatus("No Barcode","No Barcode");
    
    private final String description;

	public EnumParkingSlotBarcodeStatus(String name, String description) {
		super(name);
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	public static EnumParkingSlotBarcodeStatus getEnum(String name) {
		return (EnumParkingSlotBarcodeStatus) getEnum(EnumParkingSlotBarcodeStatus.class, name);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumParkingSlotBarcodeStatus.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumParkingSlotBarcodeStatus.class);
	}

	public static Iterator iterator() {
		return iterator(EnumParkingSlotBarcodeStatus.class);
	}

	public String toString() {
		return this.getName();
	}
}
