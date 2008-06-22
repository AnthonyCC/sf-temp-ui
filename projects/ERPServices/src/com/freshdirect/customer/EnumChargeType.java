package com.freshdirect.customer;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enum.Enum;

public class EnumChargeType extends Enum {

	public final static EnumChargeType DELIVERY			= new EnumChargeType("DLV", "000000000000008888", "Delivery Surcharge");
	public final static EnumChargeType CC_DECLINED		= new EnumChargeType("CCD", "000000000000002222", "Credit Card Declined Fee");
	public final static EnumChargeType RESTOCKING		= new EnumChargeType("RES", null, "Restocking Fee");
	public final static EnumChargeType PHONE				= new EnumChargeType("PHN", "000000000000001111", "Telephone Handling Fee");
	public final static EnumChargeType MISCELLANEOUS		= new EnumChargeType("MSC", "000000000000004444", "Fuel surcharge");
	public final static EnumChargeType DEPOT_DELIVERY	= new EnumChargeType("DDC", "000000000000008889", "Depot Delivery Fee");
	public final static EnumChargeType RE_DELIVERY		= new EnumChargeType("RDL", "000000000000003333", "Re Delivery Fee");
	public final static EnumChargeType FD_RESTOCKING_FEE	= new EnumChargeType("RFD", null, "FreshDirect Restocking Fee");
	public final static EnumChargeType WBL_RESTOCKING_FEE = new EnumChargeType("RWB", null, "WBL Restocking Fee");
	public final static EnumChargeType BC_RESTOCKING_FEE = new EnumChargeType("RBC", null, "BC Restocking Fee");
	public final static EnumChargeType USQ_RESTOCKING_FEE = new EnumChargeType("RUQ", null, "USQ Restocking Fee");
	public final static EnumChargeType BOUNCED_CHECK = new EnumChargeType("BCF", null, "Bounced Check Fee");

	private final String description;
	private final String materialNumber;

	private EnumChargeType(String name, String materialNumber, String description) {
		super(name);
		this.materialNumber = materialNumber;
		this.description = description;
	}

	public String getCode() {
		return this.getName();
	}

	public String getDescription() {
		return this.description;
	}

	public String getMaterialNumber() {
		return this.materialNumber;
	}

	public static EnumChargeType getEnum(String name) {
		return (EnumChargeType) getEnum(EnumChargeType.class, name);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumChargeType.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumChargeType.class);
	}

	public String toString() {
		return this.getName();
	}

}
