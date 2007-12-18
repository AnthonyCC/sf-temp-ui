/*
 * EnumAccountActivityType.java
 *
 * Created on November 15, 2001, 10:38 PM
 */

package com.freshdirect.customer;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author  jmccarter
 * @version
 */
public class EnumAccountActivityType implements java.io.Serializable {
	
	private final static Map ACCOUNT_ACTIVITY_MAP = new HashMap();

    public final static EnumAccountActivityType CHANGE_PASSWORD		= new EnumAccountActivityType(0, "Chg Password", "Change Password");
    public final static EnumAccountActivityType CANCEL_ORDER		= new EnumAccountActivityType(1, "Cancel Order", "Cancel Order");
    public final static EnumAccountActivityType ACTIVATE_ACCOUNT	= new EnumAccountActivityType(2, "Activate Acct", "Activate Account");
    public final static EnumAccountActivityType DEACTIVATE_ACCOUNT	= new EnumAccountActivityType(3, "Deactivate Acct", "Deactivate Account");

    public final static EnumAccountActivityType UPDATE_ERP_CUSTOMERINFO	= new EnumAccountActivityType(4, "U ErpCstInfo", "Update Customer Contact Info");
    public final static EnumAccountActivityType UPDATE_ERP_CUSTOMER	= new EnumAccountActivityType(5, "U ErpCst", "Update UserId / Password");

    public final static EnumAccountActivityType UPDATE_DLV_ADDRESS	= new EnumAccountActivityType(6, "U Dlv Addr", "Update Delivery Address");
    public final static EnumAccountActivityType UPDATE_BIL_ADDRESS	= new EnumAccountActivityType(7, "U Bil Addr", "Update Billing Address");
    public final static EnumAccountActivityType UPDATE_PAYMENT_METHOD	= new EnumAccountActivityType(8, "U Pymt Mthd", "Update Payment Method");

    public final static EnumAccountActivityType DELETE_DLV_ADDRESS	= new EnumAccountActivityType(9, "D Dlv Addr", "Delete Delivery Address");
    public final static EnumAccountActivityType DELETE_BIL_ADDRESS	= new EnumAccountActivityType(10, "D Bil Addr", "Delete Billing Address");
    public final static EnumAccountActivityType DELETE_PAYMENT_METHOD	= new EnumAccountActivityType(11, "D Pymt Mthd", "Delete Payment Method");

    public final static EnumAccountActivityType CREATE_ACCOUNT		= new EnumAccountActivityType(12, "Create Acct", "Create Account");
    public final static EnumAccountActivityType ENABLE_SIGNUP_PROMO	= new EnumAccountActivityType(13, "Enable Promo", "Enable Signup Promotion");
    public final static EnumAccountActivityType DISABLE_SIGNUP_PROMO	= new EnumAccountActivityType(14, "Disable Promo", "Disable Signup Promotion");
    
    public final static EnumAccountActivityType CANCEL_PRE_RESERVATION = new EnumAccountActivityType(15, "Cancel Pre-Rsv", "Canceled Pre-Reservation");
	public final static EnumAccountActivityType CHANGE_PRE_RESERVATION = new EnumAccountActivityType(16, "Change Pre-Rsv", "Changed Pre-Reservation");
	public final static EnumAccountActivityType MAKE_PRE_RESERVATION = new EnumAccountActivityType(17, "Make Pre-Rsv", "Make Pre-Reservation");
	public final static EnumAccountActivityType UPDATE_WEEKLY_RESERVATION = new EnumAccountActivityType(18, "Update W-Rsv", "Updated Weekly Reservation");

    public final static EnumAccountActivityType AUTHORIZATION_FAILED = new EnumAccountActivityType(19, "AuthFailed", "Authorization Failed");
    public final static EnumAccountActivityType ADD_DLV_ADDRESS	= new EnumAccountActivityType(20, "Add Dlv Addr", "Add Delivery Address");

    public final static EnumAccountActivityType PLACE_ALERT		= new EnumAccountActivityType(21, "Place Alert", "Place Alert");
    public final static EnumAccountActivityType REMOVE_ALERT		= new EnumAccountActivityType(22, "Remove Alert", "Remove Alert");

    public final static EnumAccountActivityType ADD_PAYMENT_METHOD	= new EnumAccountActivityType(23, "A Pymt Mthd", "Add Payment Method");

    public final static EnumAccountActivityType ADD_RESTRICTED_PAYMENT_METHOD	= new EnumAccountActivityType(24, "A Rstr Pymt Mthd", "Add Restricted Payment Method");
    public final static EnumAccountActivityType READD_RESTRICTED_PAYMENT_METHOD	= new EnumAccountActivityType(25, "I Rstr Pymt Mthd", "Readd Restricted Payment Method");
    public final static EnumAccountActivityType EDIT_RESTRICTED_PAYMENT_METHOD	= new EnumAccountActivityType(26, "E Rstr Pymt Mthd", "Edit Restricted Payment Method");
    public final static EnumAccountActivityType REMOVE_RESTRICTED_PAYMENT_METHOD= new EnumAccountActivityType(27, "R Rstr Pymt Mthd", "Remove Restricted Payment Method");

    public final static EnumAccountActivityType SET_CUSTOMER_PROFILE		= new EnumAccountActivityType(28, "Set Cust Prof", "Set Customer Profile");
    public final static EnumAccountActivityType REMOVE_CUSTOMER_PROFILE		= new EnumAccountActivityType(29, "Rm Cust Prof", "Remove Customer Profile");

    public final static EnumAccountActivityType ADD_CUST_PROMO				= new EnumAccountActivityType(30, "A Cust Promo", "Add Customer Promo");
    public final static EnumAccountActivityType EDIT_CUST_PROMO				= new EnumAccountActivityType(31, "E Cust Promo", "Edit Customer Promo");
    public final static EnumAccountActivityType REMOVE_CUST_PROMO			= new EnumAccountActivityType(32, "R Cust Promo", "Remove Customer Promo");
    //Delivery Pass Activity Type.
    public final static EnumAccountActivityType CREDIT_DLV_PASS				= new EnumAccountActivityType(33, "Cr Dlv Pass", "Credited One Delivery");
    public final static EnumAccountActivityType EXTEND_DLV_PASS				= new EnumAccountActivityType(34, "Extd Dlv Pass", "Extended One Week");
    public final static EnumAccountActivityType REDUCE_DLV_PASS				= new EnumAccountActivityType(35, "Reduce Dlv Pass", "Reduced One Week");
    public final static EnumAccountActivityType CANCEL_DLV_PASS				= new EnumAccountActivityType(36, "C Dlv Pass", "Cancel Delivery Pass");
    
    //Mass Returns.
    public final static EnumAccountActivityType MASS_RETURN				= new EnumAccountActivityType(37, "Mass Return", "Mass Return");
    
    public final static EnumAccountActivityType FLIP_AUTORENEW_DP_FLAG				= new EnumAccountActivityType(38, "Flip AutoRenew", "Flip AutoRenew DP Flag");
    public final static EnumAccountActivityType VIEW_CC_ECHECK				= new EnumAccountActivityType(39, "View CC/ECHECK", "View cc/echeck account number");

    private EnumAccountActivityType(int id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
        
        ACCOUNT_ACTIVITY_MAP.put(code, this);
    }

    public String getCode() {
		return this.code;
	}

    public String getName() {
        return this.name;
    }

	public static EnumAccountActivityType getActivityType(String code) {
		return (EnumAccountActivityType) ACCOUNT_ACTIVITY_MAP.get(code);
	}

	public boolean equals(Object o) {
		if (o instanceof EnumAccountActivityType) {
			return this.id == ((EnumAccountActivityType)o).id;
		}
		return false;
	}

    private int id;
    private String code;
    private String name;

}
