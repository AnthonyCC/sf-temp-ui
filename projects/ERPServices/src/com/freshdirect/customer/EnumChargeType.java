package com.freshdirect.customer;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

public class EnumChargeType extends Enum {

	public final static EnumChargeType DELIVERY			= new EnumChargeType("DLV", "000000000000008888", "Delivery Surcharge", "FR010000");
	public final static EnumChargeType CC_DECLINED		= new EnumChargeType("CCD", "000000000000002222", "Credit Card Declined Fee", "NT");
	public final static EnumChargeType RESTOCKING		= new EnumChargeType("RES", null, "Restocking Fee", "NT");
	public final static EnumChargeType PHONE				= new EnumChargeType("PHN", "000000000000001111", "Telephone Handling Fee", "NT");
	public final static EnumChargeType MISCELLANEOUS		= new EnumChargeType("MSC", "000000000000004444", "Fuel surcharge", "FR010000");
	public final static EnumChargeType DEPOT_DELIVERY	= new EnumChargeType("DDC", "000000000000008889", "Depot Delivery Fee", "NT");
	public final static EnumChargeType RE_DELIVERY		= new EnumChargeType("RDL", "000000000000003333", "Re Delivery Fee", "NT");
	public final static EnumChargeType FD_RESTOCKING_FEE	= new EnumChargeType("RFD", null, "FreshDirect Restocking Fee", "NT");
	public final static EnumChargeType WBL_RESTOCKING_FEE = new EnumChargeType("RWB", null, "WBL Restocking Fee", "NT");
	public final static EnumChargeType BC_RESTOCKING_FEE = new EnumChargeType("RBC", null, "BC Restocking Fee", "NT");
	public final static EnumChargeType USQ_RESTOCKING_FEE = new EnumChargeType("RUQ", null, "USQ Restocking Fee", "NT");
	public final static EnumChargeType FDW_RESTOCKING_FEE = new EnumChargeType("RUW", null, "FDW Restocking Fee", "NT");
	public final static EnumChargeType BOUNCED_CHECK = new EnumChargeType("BCF", null, "Bounced Check Fee", "NT");
	public final static EnumChargeType DLVPREMIUM = new EnumChargeType("DPR", "000000000000008888", "Premium Fee", "NT");
	public final static EnumChargeType TIP = new EnumChargeType("TIP", "000000000000006666", "Tip", "NT");

	private final String description;
	private final String materialNumber;
	private final String taxCode;

	private EnumChargeType(String name, String materialNumber, String description, String taxCode) {
		super(name);
		this.materialNumber = materialNumber;
		this.description = description;
		this.taxCode = taxCode;
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
	
	public String getTaxCode() {
		return taxCode;
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
