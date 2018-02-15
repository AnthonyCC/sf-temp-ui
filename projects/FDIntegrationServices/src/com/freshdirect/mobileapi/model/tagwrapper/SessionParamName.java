package com.freshdirect.mobileapi.model.tagwrapper;

import com.freshdirect.deliverypass.DlvPassConstants;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public interface SessionParamName {

    public static final String SESSION_PARAM_CRM_AGENT = "fd.crm.agent";

    public static final String SESSION_PARAM_APPLICATION = SessionName.APPLICATION;

    public static final String SESSION_PARAM_CUSTOMER_SERVICE_REP = SessionName.CUSTOMER_SERVICE_REP;

    public static final String SESSION_PARAM_USER = SessionName.USER;
    
    public static final String SESSION_PARAM_SOCIALONLYACCOUNT = "SOCIALONLYACCOUNT";
    
    public static final String SESSION_PARAM_SOCIALONLYEMAIL = "SOCIALONLYEMAIL";
    
    public static final String SESSION_PARAM_SOCIALONLYACCOUNT_SKIP_VALIDATION = "SOCIALONLYACCOUNT_SKIP_VALIDATION";

    public static final String SESSION_PARAM_MAKE_GOOD_ORDER = "makeGoodOrder";

    public static final String SESSION_PARAM_MAKEGOOD_COMPLAINT = SessionName.MAKEGOOD_COMPLAINT;

    public static final String SESSION_PARAM_PROCESSING_ORDER = "ProcessingOrder";

    public static final String SESSION_PARAM_SMART_STORE_PREV_RECOMMENDATIONS = SessionName.SMART_STORE_PREV_RECOMMENDATIONS;

    // custom session variable especially for (iPhone) integration services
    public static final String SESSION_PARAM_PREVIOUS_RECOMMENDATIONS = "smartStore.previousRecommendations";

    // custom session variable especially for (iPhone) integration services
    public static final String SESSION_PARAM_PREVIOUS_IMPRESSION = "smartStore.previousImpression";

    public static final String SESSION_PARAM_AUTHORIZED_PEOPLE = SessionName.AUTHORIZED_PEOPLE;

    public static final String SESSION_PARAM_PICKUP_AGREEMENT = SessionName.PICKUP_AGREEMENT;

    public static final String SESSION_PARAM_RECENT_ORDER_NUMBER = SessionName.RECENT_ORDER_NUMBER;

    public static final String SESSION_PARAM_REFERENCED_ORDER = "referencedOrder";

    public static final String SESSION_PARAM_DLV_PASS_SESSION_ID = DlvPassConstants.DLV_PASS_SESSION_ID;

    public static final String SESSION_PARAM_SKUS_ADDED = "SkusAdded";

    public static final String SESSION_PARAM_FD_QUICKCART = "fd.quickcart";
    
    public static final String SESSION_PARAM_SS_PREV_RECOMMENDATIONS = SessionName.SMART_STORE_PREV_RECOMMENDATIONS;
    
    public static final String SESSION_PARAM_SAVINGS_FEATURE_LOOK_UP_TABLE = SessionName.SAVINGS_FEATURE_LOOK_UP_TABLE;
    
    public static final String SESSION_PARAM_PREV_SAVINGS_VARIANT = SessionName.PREV_SAVINGS_VARIANT;
        
    //Apply/Remove Promo Code
     public static final String SESSION_PARAM_APC_PROMO = SessionName.APC_PROMO; 
          
     //Site Access More Page Param
     public static final String SESSION_PARAM_SITEACCESS_MOREPAGE = "morepage";
     
     public static final String SESSION_PARAM_LITE_SIGNUP_COMPLETE = "LITESIGNUP_COMPLETE";
     
     public static final String SESSION_PARAM_REFERRAL_NAME = "REFERRALNAME";
     
     public static final String SESSION_PARAM_PYMT_VERIFYFLD = "verifyFail";
     
     public static final String SESSION_PARAM_PENDING_REGISTRATION_EVENT = SessionName.PENDING_REGISTRATION_EVENT;
     
     public static final String SESSION_PARAM_PENDING_LOGIN_EVENT = SessionName.PENDING_LOGIN_EVENT;

     public static final String SESSION_PARAM_REGISTRATION_LOCATION = SessionName.REGISTRATION_LOCATION;

     public static final String SESSION_PARAM_REGISTRATION_ORIG_ZIP_CODE = SessionName.REGISTRATION_ORIG_ZIP_CODE;

     public static final String SESSION_PARAM_AUTH_FAIL_MESSAGE = "authFailMessage"; 

     public static final String SESSION_PARAM_MODIFY_CART_PRESELECTION_COMPLETED = SessionName.MODIFY_CART_PRESELECTION_COMPLETED;
     
     public static final String SESSION_PARAM_MODIFY_PAYMENT_BILLING_REFERENCE = SessionName.PAYMENT_BILLING_REFERENCE;

     public static final String SMART_STORE_PREV_RECOMMENDATIONS = SessionName.SMART_STORE_PREV_RECOMMENDATIONS;

     public static final String SMART_STORE_IMPRESSION = SessionName.IMPRESSION;
     
     public final static String CURRENT_CART = SessionName.CURRENT_CART; // used to hang on the user's anonymous cart for merge_cart
     
     public final static String CLICK_ID = SessionName.CLICK_ID;
     
     public final static String COUPON_CODE = SessionName.COUPONCODE;
     
     public final static String MSG_FOR_LOGIN_PAGE = SessionName.MSG_FOR_LOGIN_PAGE;
     
     public static final String TAXATION_TYPE_SESSION = "TAXATION_TYPE";
    



    /*
     *             session.removeAttribute(SessionName.AUTHORIZED_PEOPLE);
                session.removeAttribute(SessionName.PICKUP_AGREEMENT);

                // Set the order on the session
                session.setAttribute(SessionName.RECENT_ORDER_NUMBER, orderNumber);
                session.removeAttribute(DlvPassConstants.DLV_PASS_SESSION_ID);

     */
    //SessionName.APPLICATION, 
}
