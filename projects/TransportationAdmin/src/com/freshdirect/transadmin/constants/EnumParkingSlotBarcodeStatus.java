package com.freshdirect.transadmin.constants;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

public class EnumParkingSlotBarcodeStatus  extends Enum {
	
	public static final EnumParkingSlotBarcodeStatus BARCODE_PAVED = new EnumParkingSlotBarcodeStatus("Barcode,Paved","Barcode,Paved");

    public static final EnumParkingSlotBarcodeStatus NOBARCODE_NOTPAVED = new EnumParkingSlotBarcodeStatus("No Barcode,Not Paved","No Barcode,Not Paved");
    
    public static final EnumParkingSlotBarcodeStatus NOBARCODE_PAVED = new EnumParkingSlotBarcodeStatus("No Barcode,Paved","No Barcode,Paved");
    
    public static final EnumParkingSlotBarcodeStatus BARCODE_NOTPAVED = new EnumParkingSlotBarcodeStatus("Barcode,Not Paved","Barcode,Not Paved");
      
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
