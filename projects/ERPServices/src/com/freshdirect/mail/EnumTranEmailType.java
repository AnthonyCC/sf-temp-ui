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

	
	/*
	 * DHELLER 12-2017
	 * since apache enums cannot be used directly in  a SWITCH stmt, I band aided it by creating constants for each enum, and assigning it accordingly.
	 * By adding a getter(), we now have something for each enum that is an int and CAN be used in a switch stmt.
	 * This was opposed to changing the whole ENUM class to a java 5 enum with all the potential headaches this would incur.
	 * If you the reader feels this is a hack, please feel free to correct; you have my blessings. 
	 */
	public static final int ORDER_SUBMIT_CONST = 1;
	public static final int ORDER_MODIFY_CONST =2;
	public static final int FINAL_INCOICE_CONST = 3;
	public static final int CUST_SIGNUP_CONST = 4;
	public static final int ORDER_CANCEL_CONST = 5;
	public static final int CHARGE_ORDER_CONST = 6;
	public static final int CREDIT_CONFIRM_CONST = 7;
	public static final int FORGOT_PASSWD_CONST = 8;
	public static final int AUTH_FAILURE_CONST =9;
	public static final int CUST_REMINDER_CONST = 10;
	public static final int RECIPE_MAIL_CONST =11;
	public static final int TELL_A_FRIEND_CONST = 12;
	public static final int TELLAFRIEND_RECIPE_CONST = 13;
	public static final int TELLAFRIEND_PRODUCT_CONST = 14;
	public static final int GC_ORDER_SUBMIT_CONST = 15;
	public static final int GC_BULK_ORDER_SUBMIT_CONST = 16;
	public static final int GC_AUTH_FAILURE_CONST = 17;
	public static final int GC_CANCEL_PURCHASER_CONST =18;
	public static final int GC_CANCEL_RECIPENT_CONST = 19;
	public static final int GC_BALANCE_TRANSFER_CONST = 20;
	public static final int GC_CREDIT_CONFIRM_CONST = 21;
	public static final int RH_ORDER_CONFIRM_CONST = 22;
	public static final int GC_RECIPENT_ORDER_CONST = 23;
	public static final int SMART_STORE_DYF_CONST = 24;
	
	
	public static final EnumTranEmailType ORDER_SUBMIT = new EnumTranEmailType("ORDER_SUBMIT", "Order Submission", ORDER_SUBMIT_CONST);
	public static final EnumTranEmailType ORDER_MODIFY = new EnumTranEmailType("ORDER_MODIFY", "Order Modification", ORDER_MODIFY_CONST );
	public static final EnumTranEmailType FINAL_INCOICE = new EnumTranEmailType("FINAL_INVOICE", "Final Order Total After Incoice", FINAL_INCOICE_CONST);
	public static final EnumTranEmailType CUST_SIGNUP = new EnumTranEmailType("CUST_SIGNUP", "Customer SignUp", CUST_SIGNUP_CONST);
	public static final EnumTranEmailType ORDER_CANCEL = new EnumTranEmailType("ORDER_CANCEL", "Order Cancellation", ORDER_CANCEL_CONST);
	public static final EnumTranEmailType CHARGE_ORDER = new EnumTranEmailType("CHARGE_ORDER", "Charge Order ",  CHARGE_ORDER_CONST);
	public static final EnumTranEmailType CREDIT_CONFIRM = new EnumTranEmailType("CREDIT_CONFIRM", "Confirm customer credit ", CREDIT_CONFIRM_CONST);
	public static final EnumTranEmailType FORGOT_PASSWD = new EnumTranEmailType("FORGOT_PASSWD", "Forgot password ", FORGOT_PASSWD_CONST);
	public static final EnumTranEmailType AUTH_FAILURE = new EnumTranEmailType("AUTH_FAILURE", "Authorization failed ", AUTH_FAILURE_CONST );
	public static final EnumTranEmailType CUST_REMINDER = new EnumTranEmailType("CUST_REMINDER", "Customer remainder email ", CUST_REMINDER_CONST);
	public static final EnumTranEmailType RECIPE_MAIL = new EnumTranEmailType("RECIPE_MAIL", "Customer Recipe Email ", RECIPE_MAIL_CONST );
	public static final EnumTranEmailType TELL_A_FRIEND = new EnumTranEmailType("TELL_A_FRIEND", "Tell A friend ", TELL_A_FRIEND_CONST);
	public static final EnumTranEmailType TELLAFRIEND_RECIPE = new EnumTranEmailType("TELLAFRIEND_RECIPE", "Tell A friend Recipe", TELLAFRIEND_RECIPE_CONST);
	public static final EnumTranEmailType TELLAFRIEND_PRODUCT = new EnumTranEmailType("TELLAFRIEND_PRODUCT", "Tell A friend Product", TELLAFRIEND_PRODUCT_CONST );
	public static final EnumTranEmailType GC_ORDER_SUBMIT = new EnumTranEmailType("GC_ORDER_SUBMIT", "GiftCardOrder Submission", GC_ORDER_SUBMIT_CONST );
	public static final EnumTranEmailType GC_BULK_ORDER_SUBMIT = new EnumTranEmailType("GC_BULK_ORDER_SUBMIT", "GiftCard Bulk Order Submission", GC_BULK_ORDER_SUBMIT_CONST);
	public static final EnumTranEmailType GC_AUTH_FAILURE = new EnumTranEmailType("GC_AUTH_FAILURE", "GiftCard Authorization failed ", GC_AUTH_FAILURE_CONST );
	public static final EnumTranEmailType GC_CANCEL_PURCHASER = new EnumTranEmailType("GC_CANCEL_PURCHASER", "GiftCard Cancel email for purchaser ", GC_CANCEL_PURCHASER_CONST);
	public static final EnumTranEmailType GC_CANCEL_RECIPENT = new EnumTranEmailType("GC_CANCEL_RECIPENT", "GiftCard Cancel email for Recipent ", GC_CANCEL_RECIPENT_CONST);
	public static final EnumTranEmailType GC_BALANCE_TRANSFER = new EnumTranEmailType("GC_BALANCE_TRANSFER", "GiftCard Balance Transfer", GC_BALANCE_TRANSFER_CONST);
	public static final EnumTranEmailType GC_CREDIT_CONFIRM = new EnumTranEmailType("GC_CREDIT_CONFIRM", "GiftCard Credit Confirm", GC_CREDIT_CONFIRM_CONST);
	public static final EnumTranEmailType RH_ORDER_CONFIRM = new EnumTranEmailType("RH_ORDER_CONFIRM", "Robinhood Order Confirm", RH_ORDER_CONFIRM_CONST);
	public static final EnumTranEmailType GC_RECIPENT_ORDER = new EnumTranEmailType("GC_RECIPENT_ORDER", "GiftCard Recipent Order Confirm", GC_RECIPENT_ORDER_CONST );
	public static final EnumTranEmailType SMART_STORE_DYF = new EnumTranEmailType("SMART_STORE_DYF", "SmartStore  DYF email", SMART_STORE_DYF_CONST);
	
    private String description;
    public int numericRrepresentation;


	protected EnumTranEmailType(String name, String description, int number) {
		super(name);
	    this.description = description;
	    this.numericRrepresentation=number;
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

    public int getNumericRepresentation() {
		return numericRrepresentation;
	}

	public String toString() {
		return this.description;		
	}	
}
