package com.freshdirect.dataloader.payment.reconciliation.paymentech;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

public class EnumPaymentechCategory extends Enum {
	
	/**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final EnumPaymentechCategory SALES = new EnumPaymentechCategory("SALE");
	public static final EnumPaymentechCategory REFUNDS = new EnumPaymentechCategory("REF");
	public static final EnumPaymentechCategory NET_SETTLED_DEPOSIT = new EnumPaymentechCategory("NET");
	public static final EnumPaymentechCategory INTERCHANGE_ASSESSMENT_FEES = new EnumPaymentechCategory("IA");
	public static final EnumPaymentechCategory PAYMENTECH_FEES = new EnumPaymentechCategory("PFEE");
	public static final EnumPaymentechCategory RESERVE_ADJUSTMENTS = new EnumPaymentechCategory("RES");
	public static final EnumPaymentechCategory NET_CHARGEBACK = new EnumPaymentechCategory("CB");
	public static final EnumPaymentechCategory NET_ECP_RETURN = new EnumPaymentechCategory("ERET");
	public static final EnumPaymentechCategory NET_DEBIT_ADJUSTMENTS = new EnumPaymentechCategory("DBADJ");
	public static final EnumPaymentechCategory OTHER_ADJUSTMENTS = new EnumPaymentechCategory("OTH");
	public static final EnumPaymentechCategory AUTH_ONLY = new EnumPaymentechCategory("AUTH");
	public static final EnumPaymentechCategory AUTH_ONLY_DEBIT_SALE = new EnumPaymentechCategory("DBSALE");
	public static final EnumPaymentechCategory AUTH_ONLY_DEBIT_REFUND = new EnumPaymentechCategory("DBREF");
	public static final EnumPaymentechCategory PRE_AUTH_DEBIT = new EnumPaymentechCategory("PREDB");
	public static final EnumPaymentechCategory ECP_PRE_VALIDATION = new EnumPaymentechCategory("PRE");
	public static final EnumPaymentechCategory NON_FINANCIAL = new EnumPaymentechCategory("NON");
	public static final EnumPaymentechCategory REJECTS = new EnumPaymentechCategory("REJ");
	public static final EnumPaymentechCategory DECLINED_DEPOSIT = new EnumPaymentechCategory("DEC");
	public static final EnumPaymentechCategory CANCELLED_SALES = new EnumPaymentechCategory("CNSALE");
	public static final EnumPaymentechCategory CANCELLED_REFUNDS = new EnumPaymentechCategory("CNREF");
	public static final EnumPaymentechCategory BEGINNING_INVENTORY = new EnumPaymentechCategory("BEGINV");
	public static final EnumPaymentechCategory NEW_CC_ECP_CHARGEBACKS = new EnumPaymentechCategory("RECD");
	public static final EnumPaymentechCategory ITEMS_REPRESENTED = new EnumPaymentechCategory("REPR");
	public static final EnumPaymentechCategory PARTIAL_REPRESENTMENT = new EnumPaymentechCategory("PARREP");
	public static final EnumPaymentechCategory PAYMENTECH_ADJUSTMENTS = new EnumPaymentechCategory("ADJPDE");
	public static final EnumPaymentechCategory RETURNED_TO_MERCHANT = new EnumPaymentechCategory("RTM");
	public static final EnumPaymentechCategory PARTIAL_RETURN_TO_MERCHANT = new EnumPaymentechCategory("PARRTM");
	public static final EnumPaymentechCategory ADJUSTMENT_FOR_PARTIAL_ITEMS = new EnumPaymentechCategory("ADJPAR");
	public static final EnumPaymentechCategory FEE_RECEIVED = new EnumPaymentechCategory("FEEREC");
	public static final EnumPaymentechCategory FEE_REPRESENT = new EnumPaymentechCategory("FEEREP");
	public static final EnumPaymentechCategory ACCEPTED_ITEMS_FOR_RESOURCE = new EnumPaymentechCategory("RECRS");
	public static final EnumPaymentechCategory END_INVENTORY = new EnumPaymentechCategory("ENDINV");
	

	protected EnumPaymentechCategory(String name) {
		super(name);
	}
	
	public static EnumPaymentechCategory getEnum(String name) {
		return (EnumPaymentechCategory) getEnum(EnumPaymentechCategory.class, name);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumPaymentechCategory.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumPaymentechCategory.class);
	}

	public static Iterator iterator() {
		return iterator(EnumPaymentechCategory.class);
	}

}
