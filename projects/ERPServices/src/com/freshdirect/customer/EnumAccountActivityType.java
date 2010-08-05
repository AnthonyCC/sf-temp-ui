package com.freshdirect.customer;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Category;

import com.freshdirect.framework.util.log.LoggerFactory;

public class EnumAccountActivityType implements java.io.Serializable {
	
	private static final long	serialVersionUID	= -2281974517045649443L;

	private final static Category LOGGER = LoggerFactory.getInstance(EnumAccountActivityType.class);
	
	private final static Map<String,EnumAccountActivityType> ACCOUNT_ACTIVITY_MAP = new HashMap<String,EnumAccountActivityType>();
	
	private static int idCounter = 0;

	public final static EnumAccountActivityType		UNKNOWN								= new EnumAccountActivityType( "Unknown", "Unknown Message" );
	
	public final static EnumAccountActivityType		CHANGE_PASSWORD						= new EnumAccountActivityType( "Chg Password", "Change Password" );
	public final static EnumAccountActivityType		CANCEL_ORDER						= new EnumAccountActivityType( "Cancel Order", "Cancel Order" );
	public final static EnumAccountActivityType		ACTIVATE_ACCOUNT					= new EnumAccountActivityType( "Activate Acct", "Activate Account" );
	public final static EnumAccountActivityType		DEACTIVATE_ACCOUNT					= new EnumAccountActivityType( "Deactivate Acct", "Deactivate Account" );

	public final static EnumAccountActivityType		UPDATE_ERP_CUSTOMERINFO				= new EnumAccountActivityType( "U ErpCstInfo", "Update Customer Contact Info" );
	public final static EnumAccountActivityType		UPDATE_ERP_CUSTOMER					= new EnumAccountActivityType( "U ErpCst", "Update UserId / Password" );

	public final static EnumAccountActivityType		UPDATE_DLV_ADDRESS					= new EnumAccountActivityType( "U Dlv Addr", "Update Delivery Address" );
	public final static EnumAccountActivityType		UPDATE_BIL_ADDRESS					= new EnumAccountActivityType( "U Bil Addr", "Update Billing Address" );
	public final static EnumAccountActivityType		UPDATE_PAYMENT_METHOD				= new EnumAccountActivityType( "U Pymt Mthd", "Update Payment Method" );

	public final static EnumAccountActivityType		DELETE_DLV_ADDRESS					= new EnumAccountActivityType( "D Dlv Addr", "Delete Delivery Address" );
	public final static EnumAccountActivityType		DELETE_BIL_ADDRESS					= new EnumAccountActivityType( "D Bil Addr", "Delete Billing Address" );
	public final static EnumAccountActivityType		DELETE_PAYMENT_METHOD				= new EnumAccountActivityType( "D Pymt Mthd", "Delete Payment Method" );

	public final static EnumAccountActivityType		CREATE_ACCOUNT						= new EnumAccountActivityType( "Create Acct", "Create Account" );
	public final static EnumAccountActivityType		ENABLE_SIGNUP_PROMO					= new EnumAccountActivityType( "Enable Promo", "Enable Signup Promotion" );
	public final static EnumAccountActivityType		DISABLE_SIGNUP_PROMO				= new EnumAccountActivityType( "Disable Promo", "Disable Signup Promotion" );

	public final static EnumAccountActivityType		CANCEL_PRE_RESERVATION				= new EnumAccountActivityType( "Cancel Pre-Rsv", "Canceled Pre-Reservation" );
	public final static EnumAccountActivityType		CHANGE_PRE_RESERVATION				= new EnumAccountActivityType( "Change Pre-Rsv", "Changed Pre-Reservation" );
	public final static EnumAccountActivityType		MAKE_PRE_RESERVATION				= new EnumAccountActivityType( "Make Pre-Rsv", "Make Pre-Reservation" );
	public final static EnumAccountActivityType		UPDATE_WEEKLY_RESERVATION			= new EnumAccountActivityType( "Update W-Rsv", "Updated Weekly Reservation" );

	public final static EnumAccountActivityType		AUTHORIZATION_FAILED				= new EnumAccountActivityType( "AuthFailed", "Authorization Failed" );
	public final static EnumAccountActivityType		ADD_DLV_ADDRESS						= new EnumAccountActivityType( "Add Dlv Addr", "Add Delivery Address" );

	public final static EnumAccountActivityType		PLACE_ALERT							= new EnumAccountActivityType( "Place Alert", "Place Alert" );
	public final static EnumAccountActivityType		REMOVE_ALERT						= new EnumAccountActivityType( "Remove Alert", "Remove Alert" );

	public final static EnumAccountActivityType		ADD_PAYMENT_METHOD					= new EnumAccountActivityType( "A Pymt Mthd", "Add Payment Method" );

	public final static EnumAccountActivityType		ADD_RESTRICTED_PAYMENT_METHOD		= new EnumAccountActivityType( "A Rstr Pymt Mthd", "Add Restricted Payment Method" );
	public final static EnumAccountActivityType		READD_RESTRICTED_PAYMENT_METHOD		= new EnumAccountActivityType( "I Rstr Pymt Mthd", "Readd Restricted Payment Method" );
	public final static EnumAccountActivityType		EDIT_RESTRICTED_PAYMENT_METHOD		= new EnumAccountActivityType( "E Rstr Pymt Mthd", "Edit Restricted Payment Method" );
	public final static EnumAccountActivityType		REMOVE_RESTRICTED_PAYMENT_METHOD	= new EnumAccountActivityType( "R Rstr Pymt Mthd", "Remove Restricted Payment Method" );

	public final static EnumAccountActivityType		SET_CUSTOMER_PROFILE				= new EnumAccountActivityType( "Set Cust Prof", "Set Customer Profile" );
	public final static EnumAccountActivityType		REMOVE_CUSTOMER_PROFILE				= new EnumAccountActivityType( "Rm Cust Prof", "Remove Customer Profile" );

	public final static EnumAccountActivityType		ADD_CUST_PROMO						= new EnumAccountActivityType( "A Cust Promo", "Add Customer Promo" );
	public final static EnumAccountActivityType		EDIT_CUST_PROMO						= new EnumAccountActivityType( "E Cust Promo", "Edit Customer Promo" );
	public final static EnumAccountActivityType		REMOVE_CUST_PROMO					= new EnumAccountActivityType( "R Cust Promo", "Remove Customer Promo" );
	
	// Delivery Pass Activity Type.
	public final static EnumAccountActivityType		CREDIT_DLV_PASS						= new EnumAccountActivityType( "Cr Dlv Pass", "Credited One Delivery" );
	public final static EnumAccountActivityType		EXTEND_DLV_PASS						= new EnumAccountActivityType( "Extd Dlv Pass", "Extended One Week" );
	public final static EnumAccountActivityType		EXTEND_DLV_PASS_PROMOTION			= new EnumAccountActivityType( "Extd Dlv Pass Promotion", "Extended x days" );
	public final static EnumAccountActivityType		REDUCE_DLV_PASS						= new EnumAccountActivityType( "Reduce Dlv Pass", "Reduced One Week" );
	public final static EnumAccountActivityType		CANCEL_DLV_PASS						= new EnumAccountActivityType( "C Dlv Pass", "Cancel Delivery Pass" );

	// Mass Returns.
	public final static EnumAccountActivityType		MASS_RETURN							= new EnumAccountActivityType( "Mass Return", "Mass Return" );

	public final static EnumAccountActivityType		FLIP_AUTORENEW_DP_FLAG				= new EnumAccountActivityType( "Flip AutoRenew", "Flip AutoRenew DP Flag" );
	public final static EnumAccountActivityType		AUTORENEW_DP_FLAG_ON				= new EnumAccountActivityType( "DP AR ON", "AutoRenew DP flag turned ON" );
	public final static EnumAccountActivityType		AUTORENEW_DP_FLAG_OFF				= new EnumAccountActivityType( "DP AR OFF", "AutoRenew DP flag turned OFF" );
	public final static EnumAccountActivityType		VIEW_CC_ECHECK						= new EnumAccountActivityType( "View CC/ECHECK", "View cc/echeck account number" );
	public final static EnumAccountActivityType		ADD_GIFT_CARD						= new EnumAccountActivityType( "AddGiftCard", "Add Gift Card" );
	public final static EnumAccountActivityType		GC_APPLY_FAILED						= new EnumAccountActivityType( "GCApplyFailed", "GC Apply Failed" );
	public final static EnumAccountActivityType		REMOVE_GIFT_CARD					= new EnumAccountActivityType( "RemoveGiftCard", "Remove Gift Card" );
	
	// Standing Orders
	public final static EnumAccountActivityType		STANDINGORDER_FAILED				= new EnumAccountActivityType( "SO-Failed", "Failed to make Standing Order" );
	public final static EnumAccountActivityType		STANDINGORDER_PLACED				= new EnumAccountActivityType( "SO-Placed", "Standing Order placed successfully." );
	public final static EnumAccountActivityType		STANDINGORDER_SKIPPED				= new EnumAccountActivityType( "SO-Skipped", "Standing Order skipped." );

	// Masquerade
	public final static EnumAccountActivityType		MASQUERADE_LOGIN					= new EnumAccountActivityType( "MasqueradeLogin", "Masquerade agent logged in." );
	public final static EnumAccountActivityType		MASQUERADE_LOGOUT					= new EnumAccountActivityType( "MasqueradeLogout", "Masquerade agent logged out." );

	public final static EnumAccountActivityType		PLACE_ORDER							= new EnumAccountActivityType( "Place Order", "Place Order" );
	public final static EnumAccountActivityType		PLACE_GC_ORDER						= new EnumAccountActivityType( "Place GC Order", "Place GiftCard Order" );
	public final static EnumAccountActivityType		PLACE_DON_ORDER						= new EnumAccountActivityType( "Place Don Order", "Place Donation Order" );
	public final static EnumAccountActivityType		PLACE_SUBS_ORDER					= new EnumAccountActivityType( "Place Subs Order", "Place Subscription Order" );
	public final static EnumAccountActivityType		MODIFY_ORDER						= new EnumAccountActivityType( "Modify Order", "Modify Order" );

	public final static EnumAccountActivityType		STANDINGORDER_CREATED				= new EnumAccountActivityType( "SO-Created", "Standing Order created." );
	public final static EnumAccountActivityType		STANDINGORDER_MODIFIED				= new EnumAccountActivityType( "SO-Modified", "Standing Order modified." );
	public final static EnumAccountActivityType		STANDINGORDER_DELETED				= new EnumAccountActivityType( "SO-Deleted", "Standing Order deleted." );
	public final static EnumAccountActivityType		STANDINGORDER_SAVE_FAILED			= new EnumAccountActivityType( "SO-SaveFailed", "Standing Order update/save failed." );
	
    private EnumAccountActivityType(String code, String name) {
        this.id = idCounter++;
        this.code = code;
        this.name = name;
        
        if ( code == null || code.length() == 0 ) {
        	LOGGER.warn( "Activity code cannot be null or empty. This will cause sql errors later." );
        	return;
        }
        if ( code.length() > 16 ) {
        	LOGGER.warn( "Activity code ["+code+"] cannot be longer than 16. This will cause sql errors later." );
        	return;        	
        }
        
        ACCOUNT_ACTIVITY_MAP.put(code, this);
    }

    public String getCode() {
		return this.code;
	}

    public String getName() {
        return this.name;
    }

	public static EnumAccountActivityType getActivityType(String code) {
		EnumAccountActivityType activityType = ACCOUNT_ACTIVITY_MAP.get(code);
		// to avoid null pointer exception in case of unrecognized messages (when you need to downgrade)
		return activityType != null ? activityType : UNKNOWN;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof EnumAccountActivityType) {
			return this.id == ((EnumAccountActivityType)o).id;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return getCode() + " / " + getName();
	}

    private int id;
    private String code;
    private String name;

}
