package com.freshdirect.mobileapi.model;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;

public interface MessageCodes {

    public static final String ERR_NO_DELIVERY_ADDRESS = "ERR_NO_DELIVERY_ADDRESS";

    public static final String ERR_NO_PAYMENT_METHOD = "ERR_NO_PAYMENT_METHOD";

    public static final String ERR_NO_PAYMENT_METHOD_MSG = "You do not have a payment method associated with your account and will not be able to complete checkout. Please visit FreshDirect.com to add a payment method to your account.";

    public static final String ERR_SESSION_EXPIRED = "ERR_SESSION_EXPIRED";

    public static final String ERR_CART_EXPIRED = "ERR_CART_EXPIRED";

    public static final String ERR_INCOMPATIBLE_CLIENT = "ERR_INCOMPATIBLE_CLIENT";

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
    public static final String ERR_CHECKOUT_AUTHENTICATION_REQUIRED = "ERR_CHECKOUT_AUTHENTICATION_REQUIRED";

    public static final String ERR_ATP_FAILED = "ERR_ATP_FAILED";

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
    

    //Minimum Order Amount Error
    public static final String ERR_PAYMENT_INVAID_CREDIT_CARD_NUMBER = "ERR_PAYMENT_INVAID_CREDIT_CARD_NUMBER";

    //Invalid Promo code
    public static final String ERR_REDEMPTION_ERROR = "ERR_REDEMPTION_ERROR";
    
    //Credit card problem
    public static final String ERR_CREDIT_CARD_PROBLEM = "ERR_CREDIT_CARD_PROBLEM";
    public static final String ERR_CREDIT_CARD_PROBLEM_MSG = "There was a problem with the credit card you selected. Please choose or add a new payment method.";

    public static final String ERR_PAYMENT_ACCOUNT_PROBLEM = "ERR_CREDIT_CARD_PROBLEM";
    public static final String ERR_PAYMENT_ACCOUNT_PROBLEM_MSG = "We are unable to process your order because there is a problem with your account.";

    public static final String ERR_GENERIC_CHECKOUT_EXCEPTION = "ERR_GENERIC_CHECKOUT_EXCEPTION";
    public static final String ERR_GENERIC_CHECKOUT_EXCEPTION_MSG = "There was a problem processing your order submission.";

    // =============================================== notices =============================== //
    //Minimum Order Amount Error
    public static final String NOTICE_DELIVERY_CUTOFF = "NOTICE_DELIVERY_CUTOFF";

    // =============================================== helpers =============================== //
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
        private static Map<String, ErrorMessage> translations = new HashMap<String, ErrorMessage>();

        static {
            //translations.put("order_minimum", new ErrorMessage(ERR_ORDER_MINIMUM, ErrorMessage.PASS_THROUGH));
        }

        public static ErrorMessage translate(String key, String desc, SessionUser user) {
            ErrorMessage returnValue = null;
            if ("order_minimum".equals(key)) {
                if (user != null) {
                    Double subTotal = new Double(user.getShoppingCart().getSubTotal());
                    Double minimumOrder = new Double(user.getMinimumOrderAmount());

                    //May need to distinguish between delivery and pickup
                    returnValue = new ErrorMessage(ERR_ORDER_MINIMUM, MessageFormat.format(ERR_ORDER_MINIMUM_MSG,
                            new Object[] { subTotal, minimumOrder }));
                }
            } else if ("system".equals(key)) {
                //Generic system error. Pass description through
                returnValue = new ErrorMessage(ERR_SYSTEM, desc);
            } else if ("cardNum".equals(key)) {
                returnValue = new ErrorMessage(ERR_PAYMENT_INVAID_CREDIT_CARD_NUMBER, desc);
            } else if ("quantity".equals(key)) {
                returnValue = new ErrorMessage(ERR_QUANTITY_LIMIT, desc);
            } else if ("order_amount_fraud".equals(key)) {
                if (user != null) {
                    returnValue = new ErrorMessage(ERR_CHECKOUT_AMOUNT_TOO_LARGE, MessageFormat.format(
                            SystemMessageList.MSG_CHECKOUT_AMOUNT_TOO_LARGE, new Object[] { user.getCustomerServiceContact() }));

                } else {
                    returnValue = new ErrorMessage(ERR_CHECKOUT_AMOUNT_TOO_LARGE, desc);
                }
            } else if ("error_dlv_pass_only".equals(key)) {
                returnValue = new ErrorMessage(ERR_DLV_PASS_ONLY, desc);
            } else if ("redemption_error".equals(key)) {
                returnValue = new ErrorMessage(ERR_REDEMPTION_ERROR, desc);
            }

            return returnValue;
        }
    }
}
