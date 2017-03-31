package com.freshdirect.mobileapi.model;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;

public interface MessageCodes {

    public static final String ERR_NO_DELIVERY_ADDRESS = "WARN_NO_DELIVERY_ADDRESS";
    
    public static final String ERR_NO_DELIVERY_ADDRESS_MSG = "You do not have a delivery address associated with your account and will not be able to complete checkout. Please add a delivery address to your account.";

    public static final String ERR_NO_PAYMENT_METHOD = "WARN_NO_PAYMENT_METHOD";

    //public static final String ERR_NO_PAYMENT_METHOD_MSG = "You do not have a payment method associated with your account and will not be able to complete checkout. Please add a payment method to your account.";
    public static final String ERR_NO_PAYMENT_METHOD_MSG = "Sorry, you do not have a payment method associated with your account. Please visit our website to add payment information.";    

    public static final String ERR_SESSION_EXPIRED = "ERR_SESSION_EXPIRED";
    
    public static final String WARN_SESSION_REMOVED = "WARN_SESSION_REMOVED";

    public static final String ERR_CART_EXPIRED = "ERR_CART_EXPIRED";

    public static final String ERR_INCOMPATIBLE_CLIENT = "ERR_INCOMPATIBLE_CLIENT";

    public static final String ERR_IDENTIFY_CARTLINE = "ERR_IDENTIFY_CARTLINE";

    public static final String ERR_SYSTEM = "ERR_SYSTEM";

    public static final String ERR_SYSTEM_MESSAGE = "System error was encountered while processing request.";

    //Add to Cart : Quantity Limit
    public static final String ERR_QUANTITY_LIMIT = "ERR_QUANTITY_LIMIT";

    //Set Delivery Address Step
    public static final String ERR_CHECKOUT_AMOUNT_TOO_LARGE = "ERR_CHECKOUT_AMOUNT_TOO_LARGE";

    public static final String ERR_DLV_PASS_ONLY = "ERR_DLV_PASS_ONLY";

    //Login Errors
    public static final String ERR_INVALID_EMAIL = "ERR_INVALID_EMAIL";

    //Login Errors
    public static final String ERR_AUTHENTICATION = "ERR_AUTHENTICATION";
    
    //Voucher Redemption Error
    public static final String VOUCHER_AUTHENTICATION = "VOUCHER_AUTHENTICATION";

    public static final String ERR_CHECKOUT_AUTHENTICATION_REQUIRED = "ERR_CHECKOUT_AUTHENTICATION_REQUIRED";

    public static final String ERR_ATP_FAILED = "ERR_ATP_FAILED";
    
    public static final String ERR_ATP_MIN_ORDER_FAILED = "ERR_ATP_MIN_ORDER_FAILED";
    
    public static final String ERR_ATP_TYPE_KOSHER = "ERR_ATP_TYPE_KOSHER";

    public static final String ERR_ATP_TYPE_GENERIC_RESTRICTED_AVAILABILITY = "ERR_ATP_TYPE_GENERIC_RESTRICTED_AVAILABILITY";

    public static final String ERR_ATP_TYPE_LIMITED_INVENTORY = "ERR_ATP_TYPE_LIMITED_INVENTORY";

    public static final String ERR_ATP_TYPE_OPTIONS_UNAVAILABLE = "ERR_ATP_TYPE_OPTIONS_UNAVAILABLE";

    public static final String ERR_ATP_TYPE_MUNICIPAL_UNAVAILABILITY = "ERR_ATP_TYPE_MUNICIPAL_UNAVAILABILITY";

    public static final String ERR_ATP_TYPE_GENERIC_UNAVAILABILITY = "ERR_ATP_TYPE_GENERIC_UNAVAILABILITY"; //Out of stock

    public static final String ERR_ATP_TYPE_DELIVERY_PASS = "ERR_ATP_TYPE_DELIVERY_PASS";

    //Alcohol - Age Verification Error
    public static final String ERR_AGE_VERIFICATION = "ERR_AGE_VERIFICATION";

    public static final String ERR_AGE_VERIFICATION_MSG = "Age verification needed";

    //Alcohol - Age Verification Error
    public static final String ERR_HEALTH_WARNING = "ERR_HEALTH_WARNING";

    public static final String ERR_HEALTH_WARNING_MSG = "Health warning verification";

    //Alcohol - Not allowed to be delivered outside
    public static final String ERR_ALCOHOL_DELIVERY_AREA_RESTRICTION = "ERR_ALCOHOL_DELIVERY_AREA_RESTRICTION";

    //Minimum Order Amount Error
    public static final String ERR_ORDER_MINIMUM = "ERR_ORDER_MINIMUM";

    public static final String ERR_ORDER_MINIMUM_MSG = "We''re sorry; you cannot check out because your pretax order total of {0,number,$0.00} is under the FreshDirect {1,number,$0} minimum order requirement.";
    public static final String ERR_ORDER_MINIMUM_MSG_FDX = "We''re sorry; you cannot check out because your pretax order total of {0,number,$0.00} is under the FoodKick {1,number,$0} minimum order requirement.";

    public static final String ERR_RESTRICTED_ADDRESS = "ERR_RESTRICTED_ADDRESS";
    public static final String ERR_RESTRICTED_ADDRESS_MSG = "We're sorry; FreshDirect does not deliver to this address because it is a commercial building. Unfortunately we are only able to make deliveries to residential buildings. You may enter another address or choose the Pickup option.";


    public static final String ERR_PAYMENT_INVAID_CREDIT_CARD_NUMBER = "ERR_PAYMENT_INVAID_CREDIT_CARD_NUMBER";
    
    public static final String ERR_PAYMENT_INCORRECT_CVV = "ERR_PAYMENT_INCORRECT_CVV";
    
    public static final String ERR_DELIVERY_ADDRESS_INVALID = "ERR_DELIVERY_ADDRESS_INVALID";

    //Invalid Promo code
    public static final String ERR_REDEMPTION_ERROR = "ERR_REDEMPTION_ERROR";

    //Credit card problem
    public static final String ERR_CREDIT_CARD_PROBLEM = "ERR_CREDIT_CARD_PROBLEM";

    public static final String ERR_CREDIT_CARD_PROBLEM_MSG = "There was a problem with the credit card you selected. Please choose or add a new payment method.";

    public static final String ERR_PAYMENT_ACCOUNT_PROBLEM = "ERR_CREDIT_CARD_PROBLEM";

    public static final String ERR_PAYMENT_ACCOUNT_PROBLEM_MSG = "We are unable to process your order because there is a problem with your account.";

    public static final String ERR_GENERIC_CHECKOUT_EXCEPTION = "ERR_GENERIC_CHECKOUT_EXCEPTION";

    public static final String ERR_GIFTCARD_AVS_EXCEPTION = "ERR_GIFTCARD_AVS_EXCEPTION";

    public static final String ERR_GIFTCARD_AVS_EXCEPTION_MSG = "There was a problem with the given payment method";
    
    
    public static final String ERR_GENERIC_CHECKOUT_EXCEPTION_MSG = "There was a problem processing your order submission.";

    public final static String MSG_RESTRICTED_ADDRESS                                       = "We're sorry; FreshDirect does not deliver to this address because it is a commercial building. Unfortunately we are only able to make deliveries to residential buildings. You may enter another address <a href=\"/checkout/step_1_enter.jsp\">here</a> or choose the Pickup option below. To see where we deliver, <a href=\"javascript:popup('/help/delivery_zones.jsp','large')\">click here</a>.";
    
       
    // =============================================== notices =============================== //
    //Minimum Order Amount Error
    public static final String NOTICE_DELIVERY_CUTOFF = "NOTICE_DELIVERY_CUTOFF";

    // =============================================== helpers =============================== //
    
    //ForgotPassword - Unable to find customer
    public static final String ERR_FORGOTPASSWORD_INVALID_EMAIL = "ERR_FORGOTPASSWORD_INVALID_EMAIL";
    
    //ForgotPassword - Unable to find customer
    public static final String ERR_FORGOTPASSWORD_EMAIL_NOT_EXPIRED = "ERR_FORGOTPASSWORD_EMAIL_NOT_EXPIRED";
    
    public static final String ERR_ZIP_INVALID = "ERR_ZIP_INVALID";
    
    public static final String ERR_ZIP_REQUIRED = "ERR_ZIP_REQUIRED";
    public static final String ERR_ZIP_REQUIRED_MSG = "Zip Code is required.";

    public static final String ERR_ADDRESS1_REQUIRED = "ERR_ADDRESS1_REQUIRED";
    public static final String ERR_ADDRESS1_REQUIRED_MSG = "Address is required.";
    
    public static final String ERR_CITY_REQUIRED = "ERR_CITY_REQUIRED";
    public static final String ERR_CITY_REQUIRED_MSG = "City is required.";
    
    public static final String ERR_STATE_REQUIRED = "ERR_STATE_REQUIRED";
    public static final String ERR_STATE_REQUIRED_MSG = "State is required.";

    public static final String ERR_FIRSTNAME_REQUIRED = "ERR_FIRSTNAME_REQUIRED";
    public static final String ERR_FIRSTNAME_REQUIRED_MSG = "First Name is required.";
    
    public static final String ERR_LASTNAME_REQUIRED = "ERR_LASTNAME_REQUIRED";
    public static final String ERR_LASTNAME_REQUIRED_MSG = "Last Name is required.";
    
    public static final String ERR_EMAIL_REQUIRED = "ERR_EMAIL_REQUIRED";
    public static final String ERR_EMAIL_REQUIRED_MSG = "Email is required.";
    //Fix for APPDEV-2242
    public static final String ERR_EMAIL_ALREADY_EXISTS = "ERR_EMAIL_ALREADY_EXISTS";
    public static final String ERR_EMAIL_ALREADY_EXISTS_MSG = "Account already exists.";
    
    public static final String ERR_REPEATEMAIL_REQUIRED = "ERR_REPEATEMAIL_REQUIRED";
    public static final String ERR_REPEATEMAIL_REQUIRED_MSG = "Repeat Email is required.";
    public static final String ERR_REPEATEMAIL_MISMATCH_MSG = "Please enter your email again - it doesn't match what you entered above.";
    
    public static final String ERR_PASSWORD_REQUIRED = "ERR_PASSWORD_REQUIRED";
    public static final String ERR_PASSWORD_REQUIRED_MSG = "Password is required.";
    
    //Fix for APPDEV-2243
    public static final String ERR_PASSWORD_LENGTH = "ERR_PASSWORD_LENGTH";
    public static final String ERR_PASSWORD_LENGTH_MSG = "Password should be at least four characters long.";
    //public static final String ERR_REPEAT_PASSWORD_MISMATCH = "ERR_REPEAT_PASSWORD_MISMATCH";
    //public static final String ERR_REPEAT_PASSWORD_MISMATCH_MSG = "Repeat password doesn't match.";
    
    public static final String ERR_PASSWORDHINT_REQUIRED = "ERR_PASSWORDHINT_REQUIRED";
    public static final String ERR_PASSWORDHINT_REQUIRED_MSG = "Security Question is required.";
    
    public static final String ERR_DLVPHONE_REQUIRED = "ERR_DLVPHONE_REQUIRED";
    public static final String ERR_DLVPHONE_REQUIRED_MSG = "Home Phone is required.";

    public static final String ERR_CCEXPIRATION_REQUIRED = "ERR_CCEXPIRATION_REQUIRED";
    public static final String ERR_CCEXPIRATION_REQUIRED_MSG = "Expiration date is required.";
    
    public static final String ERR_ACCTNUMBER_REQUIRED = "ERR_ACCTNUMBER_REQUIRED";
    public static final String ERR_ACCTNUMBER_REQUIRED_MSG = "Account Number/Credit Cart Number is required.";
    
    public static final String ERR_ACCTTYPE_REQUIRED = "ERR_ACCTTYPE_REQUIRED";
    public static final String ERR_ACCTTYPE_REQUIRED_MSG = "Bank Account Type is required.";

    public static final String ERR_CARDBRAND_REQUIRED = "ERR_CARDBRAND_REQUIRED";
    public static final String ERR_CARDBRAND_REQUIRED_MSG = "Card Type is required.";
    
    public static final String ERR_ABAROUTE_NUMBER_REQUIRED = "ERR_ABAROUTE_NUMBER_REQUIRED";
    public static final String ERR_ABAROUTE_NUMBER_REQUIRED_MSG = "Bank Routing Number is required.";
    
    public static final String ERR_BANK_NAME_REQUIRED = "ERR_BANK_NAME_REQUIRED";
    public static final String ERR_BANK_NAME_REQUIRED_MSG = "Bank Name is required.";
    
    public static final String ERR_CVV_REQUIRED = "ERR_CVV_REQUIRED";
    public static final String ERR_CVV_REQUIRED_MSG = "CVV number is required.";
    
    public static final String ERR_ACCOUNT_HOLDER_NAME_REQUIRED = "ERR_ACCOUNT_HOLDER_NAME_REQUIRED";
    public static final String ERR_ACCOUNT_HOLDER_NAME_REQUIRED_MSG = "Account Holder Name is required.";

    public static final String ERR_BILL_ZIP_REQUIRED = "ERR_BILL_ZIP_REQUIRED";
    public static final String ERR_BILL_ZIP_REQUIRED_MSG = "Billing Zip Code is required.";

    public static final String ERR_BILL_ADDRESS1_REQUIRED = "ERR_BILL_ADDRESS1_REQUIRED";
    public static final String ERR_BILL_ADDRESS1_REQUIRED_MSG = "Billing Address is required.";
    
    public static final String ERR_BILL_CITY_REQUIRED = "ERR_BILL_CITY_REQUIRED";
    public static final String ERR_BILL_CITY_REQUIRED_MSG = "Billing City is required.";
    
    public static final String ERR_BILL_STATE_REQUIRED = "ERR_BILL_STATE_REQUIRED";
    public static final String ERR_BILL_STATE_REQUIRED_MSG = "Billing State is required.";
    
    public final static String MSG_DONT_DELIVER_TO_ADDRESS_MOB				= "We're sorry; FreshDirect does not deliver to this address.";
    public final static String MSG_DONT_DELIVER_TO_ADDRESS_MOB_FDX			= "We're sorry; Foodkick does not deliver to this address.";
    public final static String MSG_RESTRICTED_ADDRESS_MOB 					= "We're sorry; FreshDirect does not deliver to this address because it is a commercial building. Unfortunately we are only able to make deliveries to residential buildings. You may enter another address or choose the Pickup option below.";
    
    public static final String MSG_AUTHENTICATION_FAILED = "We're unable to find an account that matches this information. Please double-check your email address and password. If you have forgotten your password, click on the \"Forgot your password?\" link.";
    
    public final static String MSG_ACCEPT_DP_TERMSANDCONDITIONS 					= "Delivery Pass Terms And Condition Accepted Successfully.";
    
    public final static String MSG_ACCEPT_DP_TERMSANDCONDITIONS_FAILED 					= "Delivery Pass Terms And Condition Acceptance Failed.";
    
    public final static String MSG_ACCEPT_FD_TERMSANDCONDITIONS 					= "Freshdirect Terms And Condition Accepted Successfully.";
    
    public final static String MSG_ACCEPT_FD_TERMSANDCONDITIONS_FAILED 					= "Freshdirect Terms And Condition Acceptance Failed.";
    
    public final static String MSG_ACCEPT_DP_TERMSANDCONDITIONS_NOADDRESS 					= "Delivery Pass Terms And Condition Acceptance Successfully. Address not found in cart";
    
    public static final String ERR_EBT_RESTRICTED = "ERR_EBT_RESTRICTED";
    public final static String MSG_EBT_PRODUCT_NOT_ALLOWED 	= "Some items are ineligible with EBT Card payment";
    
    public static final String WARNING_COUPONSYSTEM_UNAVAILABLE = "WARNING_COUPONSYSTEM_UNAVAILABLE";
    
    public static final String WARNING_COUPONS_EXP_DELIVERY_DATE ="WARNING_COUPONS_EXP_DELIVERY_DATE";
    public final static String MSG_COUPONS_EXP_DELIVERY_DATE ="Some of your coupons are not valid on your chosen delivery date. All of your coupons will be valid if your delivery date is on or before: ";
    
    public final static String MSG_OUTERSPACE_ADDRESS_MOB			= "We're sorry, but you must have a valid home address in one of our delivery zones in order to register a new delivery location.";
    
    public static final String ERR_INVALID_TRANSACTIONCODE = "ERR_INVALID_TRANSACTIONCODE";
    public final static String MSG_INVALID_TRANSACTIONCODE 	= "Invalid transaction code.";
    
    public final static String ERR_MERGE_PAGE_REDIRECT = "ERR_MERGE_PAGE_REDIRECT";
    public final static String ERR_USER_SOCIAL_PROFILE_NOTFOUND = "ERR_USER_SOCIAL_PROFILE_NOTFOUND";
    public final static String ERR_SOCIAL_USER_UNRECOGNIZED = "ERR_SOCIAL_USER_UNRECOGNIZED";
    public final static String ERR_SOCIAL_USER_EMAIL_MISSING = "ERR_SOCIAL_USER_EMAIL_MISSING";
    public final static String ERR_INVALID_USER_TOKEN = "ERR_INVALID_USER_TOKEN";
    public final static String ERR_INVALID_PROVIDER = "ERR_INVALID_PROVIDER";
    public final static String ERR_INVALID_EMAIL_ADDRESS ="ERR_INVALID_EMAIL_ADDRESS";
    public final static String ERR_INVALID_NEW_TOKEN ="ERR_INVALID_NEW_TOKEN";
    public final static String ERR_AUTHENTICATION_BY_FD_ACCOUNT_FAILED ="ERR_AUTHENTICATION_BY_FDACCOUNT_FAILED";
    public final static String ERR_AUTHENTICATION_BY_SOCIAL_ACCOUNT_FAILED ="ERR_AUTHENTICATION_BY_SOCIAL_ACCOUNT_FAILED";
    public final static String ERR_FD_EMAIL_DONT_MATCH_WITH_SOCIAL_EMAIL="ERR_FD_EMAIL_DONT_MATCH_WITH_SOCIAL_EMAIL";
    public final static String ERR_LINK_ACCOUNT_FAILED ="ERR_LINK_ACCOUNT_FAILED";

    public final static String ERR_DIR_ADDRESS_SET_EX="Address set Successfully. Cart has invalid items.";
    public static final String MSG_CHANGE_PASSWORD_TOKEN_EXPIRED = "Sorry, the reset password link has expired, please request a new link.";
    public static final String MSG_USER_NOT_FOUND = "Sorry, the username has not been found.";
    
    public static final String ERR_TSLOT_ALC_RESTRICTED = "ERR_TSLOT_ALC_RESTRICTED";
    public static final String ERR_TSLOT_ALC_RESTRICTED_MSG = "This timeslot is restricted for alcohol delivery. The hours of sale are regulated on a state and county level.";
    
    public static final String ORDER_MINIMUM = "order_minimum";
    public static final String SYSTEM = "system";
    
    public static class ErrorMessage {
        public final static String PASS_THROUGH = "PASS_THROUGH";

        private String key;

        private String message;

        public ErrorMessage(String key, String message) {
            this.key = key;
            this.message = message;
        }

        public String getKey() {
            return key;
        }

        public String getMessage() {
            return message;
        }

    }

    public static class ErrorCodeTranslator {
        private static Map<String, ErrorMessage> reqfield_translations = new HashMap<String, ErrorMessage>();
        
        private static Map<String, ErrorMessage> webToMobile_translations = new HashMap<String, ErrorMessage>();

        static {        	        	 
        	reqfield_translations.put("zipcode", new ErrorMessage(ERR_ZIP_REQUIRED, ERR_ZIP_REQUIRED_MSG));
        	reqfield_translations.put("state", new ErrorMessage(ERR_STATE_REQUIRED, ERR_STATE_REQUIRED_MSG));
        	reqfield_translations.put("address1", new ErrorMessage(ERR_ADDRESS1_REQUIRED, ERR_ADDRESS1_REQUIRED_MSG));
        	reqfield_translations.put("city", new ErrorMessage(ERR_CITY_REQUIRED, ERR_CITY_REQUIRED_MSG));
        	reqfield_translations.put("dlvfirstname", new ErrorMessage(ERR_FIRSTNAME_REQUIRED, ERR_FIRSTNAME_REQUIRED_MSG));
        	reqfield_translations.put("dlvlastname", new ErrorMessage(ERR_LASTNAME_REQUIRED, ERR_LASTNAME_REQUIRED_MSG));
        	reqfield_translations.put("email", new ErrorMessage(ERR_EMAIL_REQUIRED, ERR_EMAIL_REQUIRED_MSG));
        	reqfield_translations.put("repeat_email", new ErrorMessage(ERR_REPEATEMAIL_REQUIRED, ERR_REPEATEMAIL_MISMATCH_MSG));
        	reqfield_translations.put("password", new ErrorMessage(ERR_PASSWORD_REQUIRED, ERR_PASSWORD_REQUIRED_MSG));
            //reqfield_translations.put("repeat_password", new ErrorMessage(ERR_REPEAT_PASSWORD_MISMATCH, ERR_REPEAT_PASSWORD_MISMATCH_MSG));
        	reqfield_translations.put("password_hint", new ErrorMessage(ERR_PASSWORDHINT_REQUIRED, ERR_PASSWORDHINT_REQUIRED_MSG));
        	reqfield_translations.put("dlvhomephone", new ErrorMessage(ERR_DLVPHONE_REQUIRED, ERR_DLVPHONE_REQUIRED_MSG));
        	reqfield_translations.put("expiration", new ErrorMessage(ERR_CCEXPIRATION_REQUIRED, ERR_CCEXPIRATION_REQUIRED_MSG));
        	reqfield_translations.put("cardNum", new ErrorMessage(ERR_ACCTNUMBER_REQUIRED, ERR_ACCTNUMBER_REQUIRED_MSG));
        	reqfield_translations.put("csv", new ErrorMessage(ERR_CVV_REQUIRED, ERR_CVV_REQUIRED_MSG));
        	reqfield_translations.put("cardBrand", new ErrorMessage(ERR_CARDBRAND_REQUIRED, ERR_CARDBRAND_REQUIRED_MSG));
        	reqfield_translations.put("bankAccountType", new ErrorMessage(ERR_ACCTTYPE_REQUIRED, ERR_ACCTTYPE_REQUIRED_MSG));
        	reqfield_translations.put("abaRouteNumber", new ErrorMessage(ERR_ABAROUTE_NUMBER_REQUIRED, ERR_ABAROUTE_NUMBER_REQUIRED_MSG));
        	reqfield_translations.put("bankName", new ErrorMessage(ERR_BANK_NAME_REQUIRED, ERR_BANK_NAME_REQUIRED_MSG));
        	reqfield_translations.put("cardHolderName", new ErrorMessage(ERR_ACCOUNT_HOLDER_NAME_REQUIRED, ERR_ACCOUNT_HOLDER_NAME_REQUIRED_MSG));
        	reqfield_translations.put("bil_address1", new ErrorMessage(ERR_BILL_ADDRESS1_REQUIRED, ERR_BILL_ADDRESS1_REQUIRED_MSG));
        	reqfield_translations.put("bil_state", new ErrorMessage(ERR_BILL_STATE_REQUIRED, ERR_BILL_STATE_REQUIRED_MSG));
        	reqfield_translations.put("bil_zipcode", new ErrorMessage(ERR_BILL_ZIP_REQUIRED, ERR_BILL_ZIP_REQUIRED_MSG));
        	reqfield_translations.put("bil_city", new ErrorMessage(ERR_CITY_REQUIRED, ERR_CITY_REQUIRED_MSG));
        	
        	webToMobile_translations.put(SystemMessageList.MSG_DONT_DELIVER_TO_ADDRESS, new ErrorMessage(ERR_NO_DELIVERY_ADDRESS, MSG_DONT_DELIVER_TO_ADDRESS_MOB));
        	webToMobile_translations.put(SystemMessageList.MSG_RESTRICTED_ADDRESS, new ErrorMessage(ERR_NO_DELIVERY_ADDRESS, MSG_RESTRICTED_ADDRESS_MOB));
        }

        public static ErrorMessage translate(String key, String desc, SessionUser user) {
            ErrorMessage returnValue = null;
            if (ORDER_MINIMUM.equals(key)) {
                if (user != null) {
                    Double subTotal = new Double(user.getShoppingCart().getSubTotal());
                    Double minimumOrder = new Double(user.getMinimumOrderAmount());

                    //May need to distinguish between delivery and pickup
                    EnumEStoreId eStore=(user.getUserContext() != null && user.getUserContext().getStoreContext() != null) ? user.getUserContext().getStoreContext().getEStoreId():EnumEStoreId.FD;
                    if(!EnumEStoreId.FDX.equals(eStore)){
                    	returnValue = new ErrorMessage(ERR_ORDER_MINIMUM, MessageFormat.format(ERR_ORDER_MINIMUM_MSG, new Object[] { subTotal,
                    			minimumOrder }));
                    }
                    else
                    {
                    	returnValue = new ErrorMessage(ERR_ORDER_MINIMUM, MessageFormat.format(ERR_ORDER_MINIMUM_MSG_FDX, new Object[] { subTotal,
                    			minimumOrder }));
                    }
                }
            } else if (SYSTEM.equals(key)) {
                //Generic system error. Pass description through
                returnValue = new ErrorMessage(ERR_SYSTEM, desc);
            } else if ("cardNum".equals(key) && desc.contains("invalid")) {
                returnValue = new ErrorMessage(ERR_PAYMENT_INVAID_CREDIT_CARD_NUMBER, desc);
            } else if ("csv".equals(key) && desc.contains("double-check")) {
                returnValue = new ErrorMessage(ERR_PAYMENT_INCORRECT_CVV, desc);
            } else if ("address1".equals(key) && desc.contains("valid home address")) {
                returnValue = new ErrorMessage(ERR_DELIVERY_ADDRESS_INVALID, MSG_OUTERSPACE_ADDRESS_MOB);
            } else if ("zipcode".equals(key) && !desc.contains("required")) {
                returnValue = new ErrorMessage(ERR_ZIP_INVALID, desc);
            } else if ("quantity".equals(key)) {
                returnValue = new ErrorMessage(ERR_QUANTITY_LIMIT, desc);
            } else if ("password".equals(key) && desc.contains("at least four characters long")) {
                returnValue = new ErrorMessage(ERR_PASSWORD_LENGTH, ERR_PASSWORD_LENGTH_MSG);
            } else if ("email".equals(key) && desc.contains("An account already exists")) {
                returnValue = new ErrorMessage(ERR_EMAIL_ALREADY_EXISTS, ERR_EMAIL_ALREADY_EXISTS_MSG);
            } else if ("order_amount_fraud".equals(key)) {
            	/*
                if (user != null) {
                    returnValue = new ErrorMessage(ERR_CHECKOUT_AMOUNT_TOO_LARGE, MessageFormat.format(
                            SystemMessageList.MSG_CHECKOUT_AMOUNT_TOO_LARGE, new Object[] { user.getCustomerServiceContact() }));

                } else {
                */
                    returnValue = new ErrorMessage(ERR_CHECKOUT_AMOUNT_TOO_LARGE, desc);
                //}
            } else if ("error_dlv_pass_only".equals(key)) {
                returnValue = new ErrorMessage(ERR_DLV_PASS_ONLY, desc);
            } else if ("redemption_error".equals(key)) {
                returnValue = new ErrorMessage(ERR_REDEMPTION_ERROR, desc);
            } else if (SystemMessageList.MSG_REQUIRED.equals(desc) && reqfield_translations.containsKey(key)) {
                returnValue = reqfield_translations.get(key);
            } else if (webToMobile_translations.containsKey(desc)) {
            	returnValue = webToMobile_translations.get(desc);
            }

            return returnValue;
        }
    }
}
