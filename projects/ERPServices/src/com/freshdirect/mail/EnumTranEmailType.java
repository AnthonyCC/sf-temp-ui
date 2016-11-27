package com.freshdirect.mail;

/*
 * Created on Jun 2, 2005
 */

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

/**
 * @author jng
 *
 */
public class EnumTranEmailType extends Enum {
	public static final EnumTranEmailType ORDER_SUBMIT = new EnumTranEmailType("ORDER_SUBMIT", "Order Submission");
	public static final EnumTranEmailType ORDER_MODIFY = new EnumTranEmailType("ORDER_MODIFY", "Order Modification");
	public static final EnumTranEmailType FINAL_INCOICE = new EnumTranEmailType("FINAL_INVOICE", "Final Order Total After Incoice");
	public static final EnumTranEmailType CUST_SIGNUP = new EnumTranEmailType("CUST_SIGNUP", "Customer SignUp");
	public static final EnumTranEmailType ORDER_CANCEL = new EnumTranEmailType("ORDER_CANCEL", "Order Cancellation");
	public static final EnumTranEmailType CHARGE_ORDER = new EnumTranEmailType("CHARGE_ORDER", "Charge Order ");
	public static final EnumTranEmailType CREDIT_CONFIRM = new EnumTranEmailType("CREDIT_CONFIRM", "Confirm customer credit ");
	public static final EnumTranEmailType FORGOT_PASSWD = new EnumTranEmailType("FORGOT_PASSWD", "Forgot password ");
	public static final EnumTranEmailType AUTH_FAILURE = new EnumTranEmailType("AUTH_FAILURE", "Authorization failed ");
	public static final EnumTranEmailType CUST_REMINDER = new EnumTranEmailType("CUST_REMINDER", "Customer remainder email ");
	public static final EnumTranEmailType RECIPE_MAIL = new EnumTranEmailType("RECIPE_MAIL", "Customer Recipe Email ");
	public static final EnumTranEmailType TELL_A_FRIEND = new EnumTranEmailType("TELL_A_FRIEND", "Tell A friend ");
	public static final EnumTranEmailType TELLAFRIEND_RECIPE = new EnumTranEmailType("TELLAFRIEND_RECIPE", "Tell A friend Recipe");
	public static final EnumTranEmailType TELLAFRIEND_PRODUCT = new EnumTranEmailType("TELLAFRIEND_PRODUCT", "Tell A friend Product");
	public static final EnumTranEmailType GC_ORDER_SUBMIT = new EnumTranEmailType("GC_ORDER_SUBMIT", "GiftCardOrder Submission");
	public static final EnumTranEmailType GC_BULK_ORDER_SUBMIT = new EnumTranEmailType("GC_BULK_ORDER_SUBMIT", "GiftCard Bulk Order Submission");
	public static final EnumTranEmailType GC_AUTH_FAILURE = new EnumTranEmailType("GC_AUTH_FAILURE", "GiftCard Authorization failed ");
	public static final EnumTranEmailType GC_CANCEL_PURCHASER = new EnumTranEmailType("GC_CANCEL_PURCHASER", "GiftCard Cancel email for purchaser ");
	public static final EnumTranEmailType GC_CANCEL_RECIPENT = new EnumTranEmailType("GC_CANCEL_RECIPENT", "GiftCard Cancel email for Recipent ");
	public static final EnumTranEmailType GC_BALANCE_TRANSFER = new EnumTranEmailType("GC_BALANCE_TRANSFER", "GiftCard Balance Transfer");
	public static final EnumTranEmailType GC_CREDIT_CONFIRM = new EnumTranEmailType("GC_CREDIT_CONFIRM", "GiftCard Credit Confirm");
	public static final EnumTranEmailType RH_ORDER_CONFIRM = new EnumTranEmailType("RH_ORDER_CONFIRM", "Robinhood Order Confirm");
	public static final EnumTranEmailType GC_RECIPENT_ORDER = new EnumTranEmailType("GC_RECIPENT_ORDER", "GiftCard Recipent Order Confirm");
	public static final EnumTranEmailType SMART_STORE_DYF = new EnumTranEmailType("SMART_STORE_DYF", "SmartStore  DYF email");
	
    private String description;

    protected EnumTranEmailType(String name, String description) {
		super(name);
	    this.description = description;
	}
	
	public static EnumTranEmailType getEnum(String type) {
		return (EnumTranEmailType) getEnum(EnumTranEmailType.class, type);
	}
		
	public static Map getEnumMap() {
		return getEnumMap(EnumTranEmailType.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumTranEmailType.class);
	}

	public static Iterator iterator() {
		return iterator(EnumTranEmailType.class);
	}

	public String getDescription(){
		return this.description;
	}

	public String toString() {
		return this.description;		
	}	
}
