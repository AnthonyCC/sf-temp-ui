package com.freshdirect.dataloader.payment.reconciliation.paymentech;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

public class EnumPaymentechSubCategory extends Enum {
	
	public static final EnumPaymentechSubCategory INTERCHANGE = new EnumPaymentechSubCategory("IC");
	public static final EnumPaymentechSubCategory ASSESMENT = new EnumPaymentechSubCategory("AS");
	public static final EnumPaymentechSubCategory AUTHORIZATION_FEES = new EnumPaymentechSubCategory("AUTH");
	public static final EnumPaymentechSubCategory CC_ECP_CHARGEBACK_FEES = new EnumPaymentechSubCategory("PDE");
	public static final EnumPaymentechSubCategory DEPOSIT_FEES = new EnumPaymentechSubCategory("DEP");
	public static final EnumPaymentechSubCategory EQUIPMENT_FEES = new EnumPaymentechSubCategory("EQUIP");
	public static final EnumPaymentechSubCategory MONTHLY_FEES = new EnumPaymentechSubCategory("MONTH");
	public static final EnumPaymentechSubCategory NON_TX_FEES = new EnumPaymentechSubCategory("NON");
	public static final EnumPaymentechSubCategory FUNDS_TRANSFER_FEES = new EnumPaymentechSubCategory("FT");
	public static final EnumPaymentechSubCategory OTHER_FEES = new EnumPaymentechSubCategory("OTH");

	protected EnumPaymentechSubCategory(String name) {
		super(name);
	}
	
	public static EnumPaymentechSubCategory getEnum(String name) {
		return (EnumPaymentechSubCategory) getEnum(EnumPaymentechSubCategory.class, name);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumPaymentechSubCategory.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumPaymentechSubCategory.class);
	}

	public static Iterator iterator() {
		return iterator(EnumPaymentechSubCategory.class);
	}
	
}
