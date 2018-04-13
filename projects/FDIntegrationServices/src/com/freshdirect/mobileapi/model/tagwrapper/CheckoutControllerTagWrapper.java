package com.freshdirect.mobileapi.model.tagwrapper;

import javax.servlet.jsp.JspException;

import org.apache.log4j.Category;

import com.freshdirect.customer.EnumDeliverySetting;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.mobileapi.controller.data.request.DeliveryAddressRequest;
import com.freshdirect.mobileapi.controller.data.request.PaymentMethodRequest;
import com.freshdirect.mobileapi.model.DeliveryAddress.DeliveryAddressType;
import com.freshdirect.mobileapi.model.MessageCodes;
import com.freshdirect.mobileapi.model.ResultBundle;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.util.MobileApiProperties;
import com.freshdirect.webapp.taglib.fdstore.CheckoutControllerTag;
import com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.SiteAccessControllerTag;

public class CheckoutControllerTagWrapper extends ControllerTagWrapper implements RequestParamName, SessionParamName, MessageCodes {

    private static Category LOGGER = LoggerFactory.getInstance(CheckoutControllerTagWrapper.class);

    public static final String ACTION_SET_DELIVERY_ADDRESS = "setDeliveryAddress";
    
    public static final String REQUESTED_PAYMENT_ID = "requestedPaymentId";
    
    public static final String ACTION_EDIT_SET_DELIVERY_ADDRESS = "editAndSetDeliveryAddress"; //Not supported

    public static final String ACTION_DELETE_DELIVERY_ADDRESS = "deleteDeliveryAddress"; //Not supported
    
    public static final String ACTION_RESERVE_DELIVERY_TIMESLOT = "reserveDeliveryTimeSlot";

    public static final String ACTION_SUBMIT_ORDER = "submitOrder";

    public static final String ACTION_SET_PAYMENT_METHOD = "setPaymentMethod";
    
    public static final String ACTION_ADD_SET_PAYMENT_METHOD = "addAndSetPaymentMethod";
    
    public static final String ACTION_ADD_SET_DELIVERY_ADDRESS = "addAndSetDeliveryAddress"; 

    public static final String ACTION_ADD_PAYMENT_METHOD = "addPaymentMethod"; 
    
    public static final String ACTION_EDIT_PAYMENT_METHOD = "editPaymentMethod";

    public static final String ACTION_SET_PAYMENT_METHOD_AND_SUBMIT = "setPaymentAndSubmit"; //Not supported

    public static final String ACTION_DELETE_PAYMENT_METHOD = "deletePaymentMethod";

    public static final String ACTION_SET_DELIVERY_ADDRESS_AND_PAYMENT = "setDeliveryAddressAndPayment"; //Not supported
    
    public static final String ACTION_SET_ORDER_MOBILE_NUMBER = "setOrderMobileNumber";

	private SessionUser sessionUser; 
    
    protected CheckoutControllerTagWrapper(CheckoutControllerTag wrapTarget, SessionUser user) {
        this(wrapTarget, user.getFDSessionUser());
    }

    protected CheckoutControllerTagWrapper(CheckoutControllerTag wrapTarget, FDUserI user) {
        super(wrapTarget, user);
        ((HttpResponseWrapper) this.pageContext.getResponse()).setSuccessUrl(wrapTarget.getSuccessPage());
    }

    public CheckoutControllerTagWrapper(FDUserI user) {
        this(new CheckoutControllerTag(), user);
    }

    public CheckoutControllerTagWrapper(SessionUser sessionUser) {
        this(new CheckoutControllerTag(), sessionUser.getFDSessionUser());
        this.sessionUser = sessionUser;
    }

    public ResultBundle submitOrder() throws FDException {
        addExpectedSessionValues(new String[] { SESSION_PARAM_PROCESSING_ORDER, SESSION_PARAM_MAKEGOOD_COMPLAINT,
                SESSION_PARAM_APPLICATION, SESSION_PARAM_MAKE_GOOD_ORDER, SESSION_PARAM_CRM_AGENT, SESSION_PARAM_CUSTOMER_SERVICE_REP,
                SESSION_PARAM_RECENT_ORDER_NUMBER, SessionName.PENDING_CONVERSION_ORDER_MODIFIED_MODELS, SessionName.PENDING_HELP_EMAIL_EVENT, SessionName.PENDING_SHOP_9_MODELS,TAXATION_TYPE_SESSION, SessionName.PAYPAL_DEVICE_ID, BYPASS_CART_VALIDATION }, new String[] { SESSION_PARAM_PROCESSING_ORDER, SESSION_PARAM_USER,
                SESSION_PARAM_SMART_STORE_PREV_RECOMMENDATIONS, SESSION_PARAM_AUTHORIZED_PEOPLE, SESSION_PARAM_PICKUP_AGREEMENT,
                SESSION_PARAM_RECENT_ORDER_NUMBER, SESSION_PARAM_DLV_PASS_SESSION_ID, SESSION_PARAM_REFERENCED_ORDER,
				SESSION_PARAM_MAKE_GOOD_ORDER, SessionName.PENDING_CONVERSION_ORDER_MODIFIED_MODELS, SessionName.PENDING_HELP_EMAIL_EVENT, SessionName.PENDING_SHOP_9_MODELS,SESSION_PARAM_AUTH_FAIL_MESSAGE,TAXATION_TYPE_SESSION,SessionName.PAYPAL_DEVICE_ID, BYPASS_CART_VALIDATION }); //gets,sets
        addExpectedRequestValues(new String[] { SESSION_PARAM_RECENT_ORDER_NUMBER, REQ_PARAM_IGNORE_PROMO_ERRORS, TAXATION_TYPE, BYPASS_CART_VALIDATION }, new String[] { REQ_PARAM_PAYMENT_METHOD_ID,
                REQ_PARAM_BILLING_REF, TAXATION_TYPE, BYPASS_CART_VALIDATION });//gets,sets
        getWrapTarget().setActionName(ACTION_SUBMIT_ORDER);
        ((HttpSessionWrapper)this.pageContext.getSession()).addExpectedSets(new String[]{"TAXATION_TYPE"});
        this.pageContext.getSession().setAttribute("TAXATION_TYPE", this.sessionUser.isAvalaraTaxed()?"AVAL":null);
        ((HttpSessionWrapper)this.pageContext.getSession()).addExpectedSets(new String[]{"BYPASS_CART_VALIDATION"});
        this.pageContext.getSession().setAttribute("BYPASS_CART_VALIDATION", "YES");
        setMethodMode(true);       
        ResultBundle result = new ResultBundle(executeTagLogic(), this);

        //Tag Lib set some values in session that we need. Get them. 
        result.addExtraData(SESSION_PARAM_RECENT_ORDER_NUMBER, pageContext.getSession().getAttribute(SESSION_PARAM_RECENT_ORDER_NUMBER));

        //((CheckoutControllerTag)wrapTarget).
        LOGGER.debug("Success page after execution is:" + getWrapTarget().getSuccessPage());

        String redirectUrl = ((HttpResponseWrapper) pageContext.getResponse()).getSendRedirectUrl();
        LOGGER.debug("redirectUrl is:" + redirectUrl);

        CheckoutControllerTag wrappedTag = (CheckoutControllerTag) this.getWrapTarget();

        //Check redirect status on credit card auth failure
        if ((result.getActionResult() == null) && (redirectUrl != null)) {
            ActionResult actionResult = new ActionResult();
            if (redirectUrl.startsWith(wrappedTag.getCcdProblemPage())) {
                actionResult.addError(new ActionError(ERR_CREDIT_CARD_PROBLEM, ERR_CREDIT_CARD_PROBLEM_MSG));
            } else if (redirectUrl.equals(wrappedTag.getAuthCutoffPage())) {
                actionResult.addError(new ActionError(ERR_PAYMENT_ACCOUNT_PROBLEM, ERR_PAYMENT_ACCOUNT_PROBLEM_MSG));
            } else if (redirectUrl.equals(wrappedTag.getGcAVSExceptionPage())) {
                actionResult.addError(new ActionError(ERR_GIFTCARD_AVS_EXCEPTION, ERR_GIFTCARD_AVS_EXCEPTION_MSG));
            } else {
                actionResult.addError(new ActionError(ERR_GENERIC_CHECKOUT_EXCEPTION, ERR_GENERIC_CHECKOUT_EXCEPTION_MSG));
            }
            result.setActionResult(actionResult);
        }
        
        if(result.getActionResult().getError("order_amount_fraud") != null) {
        	ActionResult actionResult = new ActionResult();
        	actionResult.addError(new ActionError("order_amount_fraud", result.getActionResult().getError("order_amount_fraud").getDescription()));
        	result.setActionResult(actionResult);
        }

        return result;
    }

    public ResultBundle setPaymentMethod(String paymentMethodId, String billingReference, String isAccountLevel) throws FDException {
        addExpectedSessionValues(new String[] { SESSION_PARAM_APPLICATION, SESSION_PARAM_CUSTOMER_SERVICE_REP, SESSION_PARAM_CRM_AGENT,SESSION_PARAM_PYMT_VERIFYFLD, SESSION_PARAM_MAKE_GOOD_ORDER, TAXATION_TYPE_SESSION, SessionName.PAYPAL_DEVICE_ID  },
                new String[] { SESSION_PARAM_USER, SESSION_PARAM_PYMT_VERIFYFLD, SESSION_PARAM_MAKE_GOOD_ORDER, TAXATION_TYPE_SESSION, SessionName.PAYPAL_DEVICE_ID  }); //gets,sets
        addExpectedRequestValues(new String[] { REQ_PARAM_CHEF_TABLE, REQ_PARAM_PAYMENT_METHOD_ID, REQ_PARAM_BILLING_REF,
                REQ_PARAM_GIFT_CARD, REQ_PARAM_DONATION, REQ_PARAM_BYPASS_ACCOUNT_CHECK, TAXATION_TYPE,REQ_PARAM_DATA }, new String[] { REQ_PARAM_PAYMENT_METHOD_ID,
                REQ_PARAM_BILLING_REF, TAXATION_TYPE });//gets,sets
        addRequestValue(REQ_PARAM_PAYMENT_METHOD_ID, paymentMethodId);
        addRequestValue(REQ_PARAM_BILLING_REF, billingReference);
        addRequestValue(REQ_PARAM_IS_ACCOUNT_LEVEL, isAccountLevel);
        getWrapTarget().setActionName(ACTION_SET_PAYMENT_METHOD);
        setMethodMode(true);
        
        return doPaymentTagLogic();
    }

    public ResultBundle addAndSetDeliveryAddress(DeliveryAddressRequest deliveryAddress) throws FDException {
        addExpectedSessionValues(new String[] { SESSION_PARAM_APPLICATION, SESSION_PARAM_CUSTOMER_SERVICE_REP, SESSION_PARAM_CRM_AGENT, SESSION_PARAM_MAKE_GOOD_ORDER, TAXATION_TYPE_SESSION, SessionName.PAYPAL_DEVICE_ID  },
                new String[] { SESSION_PARAM_USER, SessionName.SIGNUP_WARNING, SESSION_PARAM_MAKE_GOOD_ORDER, TAXATION_TYPE_SESSION, SessionName.PAYPAL_DEVICE_ID  }); //gets,sets
        addExpectedRequestValues(new String[] {EnumUserInfoName.DLV_FIRST_NAME.getCode(),EnumUserInfoName.DLV_LAST_NAME.getCode(),EnumUserInfoName.DLV_HOME_PHONE.getCode(),EnumUserInfoName.DLV_HOME_PHONE_EXT.getCode(),
        		EnumUserInfoName.DLV_ADDRESS_1.getCode(),EnumUserInfoName.DLV_ADDRESS_2.getCode(),EnumUserInfoName.DLV_APARTMENT.getCode(),EnumUserInfoName.DLV_CITY.getCode(),EnumUserInfoName.DLV_STATE.getCode(),EnumUserInfoName.DLV_ZIPCODE.getCode(),
        		EnumUserInfoName.DLV_COUNTRY.getCode(),EnumUserInfoName.DLV_DELIVERY_INSTRUCTIONS.getCode(),EnumUserInfoName.DLV_ALTERNATE_DELIVERY.getCode(),EnumUserInfoName.DLV_ALT_FIRSTNAME.getCode(),
        		EnumUserInfoName.DLV_ALT_LASTNAME.getCode(),EnumUserInfoName.DLV_ALT_APARTMENT.getCode(),EnumUserInfoName.DLV_ALT_CONTACT_PHONE.getCode(),
        		EnumUserInfoName.DLV_ALT_CONTACT_EXT.getCode(),EnumUserInfoName.DLV_UNATTENDED_DELIVERY_OPT.getCode(),EnumUserInfoName.DLV_UNATTENDED_DELIVERY_INSTRUCTIONS.getCode(),
        		EnumUserInfoName.DLV_UNATTENDED_CONSENT_SEEN.getCode(), EnumUserInfoName.DLV_ALT_PHONE.getCode(), EnumUserInfoName.DLV_ALT_EXT.getCode(),
        		EnumUserInfoName.DLV_COMPANY_NAME.getCode(), EnumUserInfoName.DLV_SERVICE_TYPE.getCode(),"first_name","last_name","homephone",TAXATION_TYPE}, new String[] {TAXATION_TYPE});//gets,sets
        addRequestValue(EnumUserInfoName.DLV_FIRST_NAME.getCode(), deliveryAddress.getDlvfirstname());
        addRequestValue(EnumUserInfoName.DLV_LAST_NAME.getCode(), deliveryAddress.getDlvlastname());
        addRequestValue(EnumUserInfoName.DLV_COMPANY_NAME.getCode(), deliveryAddress.getDlvcompanyname());
        addRequestValue(EnumUserInfoName.DLV_HOME_PHONE.getCode(), deliveryAddress.getDlvhomephone());
        addRequestValue(EnumUserInfoName.DLV_HOME_PHONE_EXT.getCode(), deliveryAddress.getDlvhomephoneext());
        addRequestValue(EnumUserInfoName.DLV_ADDRESS_1.getCode(), deliveryAddress.getAddress1());
        addRequestValue(EnumUserInfoName.DLV_ADDRESS_2.getCode(), deliveryAddress.getAddress2());
        addRequestValue(EnumUserInfoName.DLV_APARTMENT.getCode(), deliveryAddress.getApartment());
        addRequestValue(EnumUserInfoName.DLV_CITY.getCode(), deliveryAddress.getCity());
        addRequestValue(EnumUserInfoName.DLV_STATE.getCode(), deliveryAddress.getState());
        addRequestValue(EnumUserInfoName.DLV_SERVICE_TYPE.getCode(), deliveryAddress.getDlvServiceType());
        addRequestValue(EnumUserInfoName.DLV_ZIPCODE.getCode(), deliveryAddress.getZipcode());
        addRequestValue(EnumUserInfoName.DLV_COUNTRY.getCode(), deliveryAddress.getCountry());
        addRequestValue(EnumUserInfoName.DLV_DELIVERY_INSTRUCTIONS.getCode(), deliveryAddress.getDeliveryInstructions());
        
        if(deliveryAddress.isDoorman()) {
			//APPDEV-4228  Doorman Property  not Adding/Editing addresses
        	//addRequestValue(EnumUserInfoName.DLV_ALTERNATE_DELIVERY.getCode(), EnumDeliverySetting.DOORMAN.getName());
        	addRequestValue(EnumUserInfoName.DLV_ALTERNATE_DELIVERY.getCode(), EnumDeliverySetting.DOORMAN.getDeliveryCode());
        } else if (deliveryAddress.isNeighbor()) { 
//APPDEV-4228  Doorman Property  not Adding/Editing addresses
        	//addRequestValue(EnumUserInfoName.DLV_ALTERNATE_DELIVERY.getCode(), EnumDeliverySetting.NEIGHBOR.getName());
        	addRequestValue(EnumUserInfoName.DLV_ALTERNATE_DELIVERY.getCode(), EnumDeliverySetting.NEIGHBOR.getDeliveryCode());
        } else {
//APPDEV-4228  Doorman Property  not Adding/Editing addresses        	
        	//addRequestValue(EnumUserInfoName.DLV_ALTERNATE_DELIVERY.getCode(), EnumDeliverySetting.NONE.getName());
        	addRequestValue(EnumUserInfoName.DLV_ALTERNATE_DELIVERY.getCode(), EnumDeliverySetting.NONE.getDeliveryCode());
        }
        
        addRequestValue(EnumUserInfoName.DLV_ALT_FIRSTNAME.getCode(), deliveryAddress.getAlternateFirstName());
        addRequestValue(EnumUserInfoName.DLV_ALT_LASTNAME.getCode(), deliveryAddress.getAlternateLastName());
        addRequestValue(EnumUserInfoName.DLV_ALT_APARTMENT.getCode(), deliveryAddress.getAlternateApartment());
        addRequestValue(EnumUserInfoName.DLV_ALT_PHONE.getCode(), deliveryAddress.getAlternatePhone());
        addRequestValue(EnumUserInfoName.DLV_ALT_EXT.getCode(), deliveryAddress.getAlternatePhoneExt());
        addRequestValue(EnumUserInfoName.DLV_ALT_CONTACT_PHONE.getCode(), deliveryAddress.getAltContactPhone());
        addRequestValue(EnumUserInfoName.DLV_ALT_CONTACT_EXT.getCode(), deliveryAddress.getAltContactPhoneExt());
        addRequestValue(EnumUserInfoName.DLV_UNATTENDED_CONSENT_SEEN.getCode(),deliveryAddress.getUnattendedDeliveryNoticeSeen());
        addRequestValue(EnumUserInfoName.DLV_UNATTENDED_DELIVERY_INSTRUCTIONS.getCode(), deliveryAddress.getUnattendedDeliveryInstr());
        addRequestValue(EnumUserInfoName.DLV_UNATTENDED_DELIVERY_OPT.getCode(), deliveryAddress.getUnattendedDeliveryOpt());
        

        getWrapTarget().setActionName(ACTION_ADD_SET_DELIVERY_ADDRESS);
        setMethodMode(true);

        ActionResult actionResult = executeTagLogic();
        LOGGER.debug("setCheckoutDeliveryAddress[executeTagLogic] :"+ actionResult);
        String successPage = ((CheckoutControllerTag) wrapTarget).getSuccessPage();

        if (actionResult == null) {
            actionResult = new ActionResult();
        }

        CheckoutControllerTag wrappedTag = (CheckoutControllerTag) this.getWrapTarget();
        if (wrappedTag.getAgeVerificationPage().equals(successPage)) {
        	LOGGER.debug("addAndSetDeliveryAddress[ERR_AGE_VERIFICATION] :"+ successPage);
            actionResult.addError(new ActionError(ERR_AGE_VERIFICATION, MobileApiProperties.getMediaPath()
                    + MobileApiProperties.getAlcoholAgeWarningMediaPath()));
        }
        
        /*
        if("true".equals(actionResult.getError("SHOW_UNATTENDED_MSG"))) {
        	actionResult.addError(new ActionError("SHOW_UNATTENDED_MSG", MobileApiProperties.getMediaPath()
                    + MobileApiProperties.getUnattendedDeliveryMediaPath()));
        }
        */
        LOGGER.debug("addAndSetDeliveryAddress[END] ");
        return new ResultBundle(actionResult, this);
    }

    public ResultBundle addPaymentMethod(PaymentMethodRequest paymentMethod) throws FDException {
        addExpectedSessionValues(new String[] { SESSION_PARAM_APPLICATION, SESSION_PARAM_CUSTOMER_SERVICE_REP, SESSION_PARAM_CRM_AGENT, SESSION_PARAM_PYMT_VERIFYFLD, SESSION_PARAM_MAKE_GOOD_ORDER, TAXATION_TYPE_SESSION , SessionName.PAYPAL_DEVICE_ID, SessionName.PAYMENT_ATTEMPT },
                new String[] { SESSION_PARAM_USER, SESSION_PARAM_PYMT_VERIFYFLD, SESSION_PARAM_MAKE_GOOD_ORDER, TAXATION_TYPE_SESSION, SessionName.PAYPAL_DEVICE_ID, SessionName.PAYMENT_ATTEMPT  }); //gets,sets
        addExpectedRequestValues(new String[] { REQ_PARAM_CARD_EXP_MONTH, REQ_PARAM_CARD_EXP_YEAR, REQ_PARAM_CARD_BRAND,
        		REQ_PARAM_ACCOUNT_NUMBER, REQ_PARAM_ABA_ROUTE_NUMBER, REQ_PARAM_BANK_NAME, REQ_PARAM_BYPASS_BAD_ACCOUNT_CHECK, REQ_PARAM_TERMS,
        		REQ_PARAM_ACCOUNT_NUMBER_VERIFY,REQ_PARAM_BANK_ACCOUNT_TYPE,REQ_PARAM_ACCOUNT_HOLDER,REQ_PARAM_BIL_ADDRESS_1,
        		REQ_PARAM_BIL_ADDRESS_2,REQ_PARAM_BIL_APARTMENT,REQ_PARAM_BIL_CITY,REQ_PARAM_BIL_STATE,REQ_PARAM_BIL_ZIPCODE, REQ_PARAM_PAYMENT_METHOD_TYPE,
        		REQ_PARAM_IS_PAYMENT_METHOD_GIFT_CARD, REQ_PARAM_IS_PAYMENT_METHOD_DONATION, REQ_PARAM_CSV, REQ_PARAM_BIL_COUNTRY, TAXATION_TYPE,REQ_PARAM_DATA}, new String[] {TAXATION_TYPE});//gets,sets
        addRequestValue(REQ_PARAM_CARD_EXP_MONTH, paymentMethod.getCardExpMonth());
        addRequestValue(REQ_PARAM_CARD_EXP_YEAR, paymentMethod.getCardExpYear());
        addRequestValue(REQ_PARAM_CARD_BRAND, paymentMethod.getCardBrand());
        addRequestValue(REQ_PARAM_ACCOUNT_NUMBER, paymentMethod.getAccountNumber());
        addRequestValue(REQ_PARAM_ABA_ROUTE_NUMBER, paymentMethod.getAbaRouteNumber());
        addRequestValue(REQ_PARAM_BANK_NAME, paymentMethod.getBankName());        
        addRequestValue(REQ_PARAM_BYPASS_BAD_ACCOUNT_CHECK, null);
        //addRequestValue(REQ_PARAM_TERMS, paymentMethod.getTerms());
        addRequestValue(REQ_PARAM_TERMS, "Y");
        addRequestValue(REQ_PARAM_ACCOUNT_NUMBER_VERIFY, paymentMethod.getAccountNumberVerify());
        addRequestValue(REQ_PARAM_BANK_ACCOUNT_TYPE, paymentMethod.getBankAccountType());        
        addRequestValue(REQ_PARAM_ACCOUNT_HOLDER, paymentMethod.getAccountHolder());
        addRequestValue(REQ_PARAM_BIL_ADDRESS_1, paymentMethod.getBillAddress1());
        addRequestValue(REQ_PARAM_BIL_ADDRESS_2, paymentMethod.getBillAddress2());
        addRequestValue(REQ_PARAM_BIL_APARTMENT, paymentMethod.getBillApt());        
        addRequestValue(REQ_PARAM_BIL_CITY, paymentMethod.getBillCity());
        addRequestValue(REQ_PARAM_BIL_STATE, paymentMethod.getBillState());
        addRequestValue(REQ_PARAM_BIL_ZIPCODE, paymentMethod.getBillZipCode());        
        addRequestValue(REQ_PARAM_PAYMENT_METHOD_TYPE, paymentMethod.getPaymentMethodType());
        addRequestValue(REQ_PARAM_CSV, paymentMethod.getCsv());
        addRequestValue(REQ_PARAM_BIL_COUNTRY, paymentMethod.getBillingCtry());
        addRequestValue(REQ_PARAM_IS_PAYMENT_METHOD_GIFT_CARD, "false");
        addRequestValue(REQ_PARAM_IS_PAYMENT_METHOD_DONATION, "false");
        addRequestValue(REQ_PARAM_CAPTCHA_TOKEN, paymentMethod.getCaptchaToken());

        getWrapTarget().setActionName(ACTION_ADD_PAYMENT_METHOD);
        setMethodMode(true);
        
        return new ResultBundle(executeTagLogic(), this);
    }
    
    public ResultBundle addPaymentMethodEx(PaymentMethodRequest paymentMethod) throws FDException {
        addExpectedSessionValues(new String[] { SESSION_PARAM_APPLICATION, SESSION_PARAM_CUSTOMER_SERVICE_REP, SESSION_PARAM_CRM_AGENT, SESSION_PARAM_PYMT_VERIFYFLD, SESSION_PARAM_MAKE_GOOD_ORDER, TAXATION_TYPE_SESSION, SessionName.PAYPAL_DEVICE_ID, SessionName.PAYMENT_ATTEMPT },
                new String[] { SESSION_PARAM_USER, SESSION_PARAM_PYMT_VERIFYFLD, SESSION_PARAM_MAKE_GOOD_ORDER, TAXATION_TYPE_SESSION , SessionName.PAYPAL_DEVICE_ID, SessionName.PAYMENT_ATTEMPT }); //gets,sets
        addExpectedRequestValues(new String[] { REQ_PARAM_CARD_EXP_MONTH, REQ_PARAM_CARD_EXP_YEAR, REQ_PARAM_CARD_BRAND,
        		REQ_PARAM_ACCOUNT_NUMBER, REQ_PARAM_ABA_ROUTE_NUMBER, REQ_PARAM_BANK_NAME, REQ_PARAM_BYPASS_BAD_ACCOUNT_CHECK, REQ_PARAM_TERMS,
        		REQ_PARAM_ACCOUNT_NUMBER_VERIFY,REQ_PARAM_BANK_ACCOUNT_TYPE,REQ_PARAM_ACCOUNT_HOLDER,REQ_PARAM_BIL_ADDRESS_1,
        		REQ_PARAM_BIL_ADDRESS_2,REQ_PARAM_BIL_APARTMENT,REQ_PARAM_BIL_CITY,REQ_PARAM_BIL_STATE,REQ_PARAM_BIL_ZIPCODE, REQ_PARAM_PAYMENT_METHOD_TYPE,
        		REQ_PARAM_IS_PAYMENT_METHOD_GIFT_CARD, REQ_PARAM_IS_PAYMENT_METHOD_DONATION, REQ_PARAM_CSV, REQ_PARAM_BIL_COUNTRY, TAXATION_TYPE,REQ_PARAM_DATA}, new String[] {TAXATION_TYPE});//gets,sets
        addRequestValue(REQ_PARAM_CARD_EXP_MONTH, paymentMethod.getCardExpMonth());
        addRequestValue(REQ_PARAM_CARD_EXP_YEAR, paymentMethod.getCardExpYear());
        addRequestValue(REQ_PARAM_CARD_BRAND, paymentMethod.getCardBrand());
        addRequestValue(REQ_PARAM_ACCOUNT_NUMBER, paymentMethod.getAccountNumber());
        addRequestValue(REQ_PARAM_ABA_ROUTE_NUMBER, paymentMethod.getAbaRouteNumber());
        addRequestValue(REQ_PARAM_BANK_NAME, paymentMethod.getBankName());        
        addRequestValue(REQ_PARAM_BYPASS_BAD_ACCOUNT_CHECK, null);
        //addRequestValue(REQ_PARAM_TERMS, paymentMethod.getTerms());
        addRequestValue(REQ_PARAM_TERMS, "Y");
        addRequestValue(REQ_PARAM_ACCOUNT_NUMBER_VERIFY, paymentMethod.getAccountNumberVerify());
        addRequestValue(REQ_PARAM_BANK_ACCOUNT_TYPE, paymentMethod.getBankAccountType());        
        addRequestValue(REQ_PARAM_ACCOUNT_HOLDER, paymentMethod.getAccountHolder());
        addRequestValue(REQ_PARAM_BIL_ADDRESS_1, paymentMethod.getBillAddress1());
        addRequestValue(REQ_PARAM_BIL_ADDRESS_2, paymentMethod.getBillAddress2());
        addRequestValue(REQ_PARAM_BIL_APARTMENT, paymentMethod.getBillApt());        
        addRequestValue(REQ_PARAM_BIL_CITY, paymentMethod.getBillCity());
        addRequestValue(REQ_PARAM_BIL_STATE, paymentMethod.getBillState());
        addRequestValue(REQ_PARAM_BIL_ZIPCODE, paymentMethod.getBillZipCode());        
        addRequestValue(REQ_PARAM_PAYMENT_METHOD_TYPE, paymentMethod.getPaymentMethodType());
        addRequestValue(REQ_PARAM_CSV, paymentMethod.getCsv());
        addRequestValue(REQ_PARAM_BIL_COUNTRY, paymentMethod.getBillingCtry());
        addRequestValue(REQ_PARAM_IS_PAYMENT_METHOD_GIFT_CARD, "false");
        addRequestValue(REQ_PARAM_IS_PAYMENT_METHOD_DONATION, "false");
        addRequestValue(REQ_PARAM_CAPTCHA_TOKEN, paymentMethod.getCaptchaToken());

        getWrapTarget().setActionName(ACTION_ADD_PAYMENT_METHOD);
        setMethodMode(true);
        ActionResult actionResult = executeTagLogic();
        LOGGER.debug("addPaymentMethodEx[executeTagLogic] :"+ actionResult);
        String successPage = ((CheckoutControllerTag) wrapTarget).getSuccessPage();

        if (actionResult == null) {
            actionResult = new ActionResult();
        }

        CheckoutControllerTag wrappedTag = (CheckoutControllerTag) this.getWrapTarget();
        if (wrappedTag.getAgeVerificationPage().equals(successPage)) {
        	LOGGER.debug("addPaymentMethodEx[ERR_AGE_VERIFICATION] :"+ successPage);
            actionResult.addError(new ActionError(ERR_AGE_VERIFICATION, MobileApiProperties.getMediaPath()
                    + MobileApiProperties.getAlcoholAgeWarningMediaPath()));
        }
        
        return new ResultBundle(actionResult, this);
    }

    public ResultBundle addAndSetPaymentMethod(PaymentMethodRequest paymentMethod) throws FDException {
        addExpectedSessionValues(new String[] { SESSION_PARAM_APPLICATION, SESSION_PARAM_CUSTOMER_SERVICE_REP, SESSION_PARAM_CRM_AGENT, SESSION_PARAM_PYMT_VERIFYFLD, SESSION_PARAM_MAKE_GOOD_ORDER, TAXATION_TYPE_SESSION, SessionName.PAYPAL_DEVICE_ID, SessionName.PAYMENT_ATTEMPT },
                new String[] { SESSION_PARAM_USER, SESSION_PARAM_PYMT_VERIFYFLD, SESSION_PARAM_MAKE_GOOD_ORDER, TAXATION_TYPE_SESSION , SessionName.PAYPAL_DEVICE_ID, SessionName.PAYMENT_ATTEMPT }); //gets,sets
        addExpectedRequestValues(new String[] { REQ_PARAM_CARD_EXP_MONTH, REQ_PARAM_CARD_EXP_YEAR, REQ_PARAM_CARD_BRAND,
        		REQ_PARAM_ACCOUNT_NUMBER, REQ_PARAM_ABA_ROUTE_NUMBER, REQ_PARAM_BANK_NAME, REQ_PARAM_BYPASS_BAD_ACCOUNT_CHECK, REQ_PARAM_TERMS,
        		REQ_PARAM_ACCOUNT_NUMBER_VERIFY,REQ_PARAM_BANK_ACCOUNT_TYPE,REQ_PARAM_ACCOUNT_HOLDER,REQ_PARAM_BIL_ADDRESS_1,
        		REQ_PARAM_BIL_ADDRESS_2,REQ_PARAM_BIL_APARTMENT,REQ_PARAM_BIL_CITY,REQ_PARAM_BIL_STATE,REQ_PARAM_BIL_ZIPCODE, REQ_PARAM_PAYMENT_METHOD_TYPE,
        		REQ_PARAM_IS_PAYMENT_METHOD_GIFT_CARD, REQ_PARAM_IS_PAYMENT_METHOD_DONATION, REQ_PARAM_CSV, REQ_PARAM_BIL_COUNTRY, TAXATION_TYPE,REQ_PARAM_DATA}, new String[] {TAXATION_TYPE});//gets,sets
        addRequestValue(REQ_PARAM_CARD_EXP_MONTH, paymentMethod.getCardExpMonth());
        addRequestValue(REQ_PARAM_CARD_EXP_YEAR, paymentMethod.getCardExpYear());
        addRequestValue(REQ_PARAM_CARD_BRAND, paymentMethod.getCardBrand());
        addRequestValue(REQ_PARAM_ACCOUNT_NUMBER, paymentMethod.getAccountNumber());
        addRequestValue(REQ_PARAM_ABA_ROUTE_NUMBER, paymentMethod.getAbaRouteNumber());
        addRequestValue(REQ_PARAM_BANK_NAME, paymentMethod.getBankName());        
        addRequestValue(REQ_PARAM_BYPASS_BAD_ACCOUNT_CHECK, null);
        //addRequestValue(REQ_PARAM_TERMS, paymentMethod.getTerms());
        addRequestValue(REQ_PARAM_TERMS, "Y");
        addRequestValue(REQ_PARAM_ACCOUNT_NUMBER_VERIFY, paymentMethod.getAccountNumberVerify());
        addRequestValue(REQ_PARAM_BANK_ACCOUNT_TYPE, paymentMethod.getBankAccountType());        
        addRequestValue(REQ_PARAM_ACCOUNT_HOLDER, paymentMethod.getAccountHolder());
        addRequestValue(REQ_PARAM_BIL_ADDRESS_1, paymentMethod.getBillAddress1());
        addRequestValue(REQ_PARAM_BIL_ADDRESS_2, paymentMethod.getBillAddress2());
        addRequestValue(REQ_PARAM_BIL_APARTMENT, paymentMethod.getBillApt());        
        addRequestValue(REQ_PARAM_BIL_CITY, paymentMethod.getBillCity());
        addRequestValue(REQ_PARAM_BIL_STATE, paymentMethod.getBillState());
        addRequestValue(REQ_PARAM_BIL_ZIPCODE, paymentMethod.getBillZipCode());        
        addRequestValue(REQ_PARAM_PAYMENT_METHOD_TYPE, paymentMethod.getPaymentMethodType());
        addRequestValue(REQ_PARAM_CSV, paymentMethod.getCsv());
        addRequestValue(REQ_PARAM_IS_PAYMENT_METHOD_GIFT_CARD, "false");
        addRequestValue(REQ_PARAM_IS_PAYMENT_METHOD_DONATION, "false");
        addRequestValue(REQ_PARAM_BILLING_REF, paymentMethod.getBillingRef());
        addRequestValue(REQ_PARAM_BIL_COUNTRY, paymentMethod.getBillingCtry());
        addRequestValue(REQ_PARAM_CAPTCHA_TOKEN, paymentMethod.getCaptchaToken());

        getWrapTarget().setActionName(ACTION_ADD_SET_PAYMENT_METHOD);
        setMethodMode(true);        
        return doPaymentTagLogic();
    }
    
    public ResultBundle savePaymentMethod(PaymentMethodRequest paymentMethod) throws FDException {
    	ResultBundle resultBundle;
    	String pId = paymentMethod.getPaymentMethodId();    	
    	if ( pId == null || pId.length() <= 0 ){        	
    		addAndSetPaymentMethod(paymentMethod);            
            resultBundle = new ResultBundle(executeTagLogic(), this);            
            resultBundle.addExtraData(REQUESTED_PAYMENT_ID,  
            		((CheckoutControllerTag) getWrapTarget()).getPaymentId());   
        } else {           	               
            editPaymentMethod(paymentMethod);
    		resultBundle = new ResultBundle(executeTagLogic(), this);
    		resultBundle.addExtraData(REQUESTED_PAYMENT_ID,  
            		((CheckoutControllerTag) getWrapTarget()).getPaymentId());
        }
    	
        return resultBundle;
    }

    public ResultBundle editPaymentMethod(PaymentMethodRequest paymentMethod) throws FDException {
        addExpectedSessionValues(new String[] { SESSION_PARAM_APPLICATION, SESSION_PARAM_CUSTOMER_SERVICE_REP, SESSION_PARAM_CRM_AGENT, SESSION_PARAM_PYMT_VERIFYFLD, SESSION_PARAM_MAKE_GOOD_ORDER, TAXATION_TYPE_SESSION , SessionName.PAYPAL_DEVICE_ID, SessionName.PAYMENT_ATTEMPT },
                new String[] { SESSION_PARAM_USER, SESSION_PARAM_PYMT_VERIFYFLD, SESSION_PARAM_MAKE_GOOD_ORDER, TAXATION_TYPE_SESSION, SessionName.PAYPAL_DEVICE_ID, SessionName.PAYMENT_ATTEMPT }); //gets,sets
        addExpectedRequestValues(new String[] { REQ_PARAM_CARD_EXP_MONTH, REQ_PARAM_CARD_EXP_YEAR, REQ_PARAM_CARD_BRAND,
        		REQ_PARAM_ACCOUNT_NUMBER, REQ_PARAM_ABA_ROUTE_NUMBER, REQ_PARAM_BANK_NAME, REQ_PARAM_BYPASS_BAD_ACCOUNT_CHECK, REQ_PARAM_TERMS,
        		REQ_PARAM_ACCOUNT_NUMBER_VERIFY,REQ_PARAM_BANK_ACCOUNT_TYPE,REQ_PARAM_ACCOUNT_HOLDER,REQ_PARAM_BIL_ADDRESS_1,
        		REQ_PARAM_BIL_ADDRESS_2,REQ_PARAM_BIL_APARTMENT,REQ_PARAM_BIL_CITY,REQ_PARAM_BIL_STATE,REQ_PARAM_BIL_ZIPCODE, REQ_PARAM_PAYMENT_METHOD_TYPE,
        		REQ_PARAM_IS_PAYMENT_METHOD_GIFT_CARD, REQ_PARAM_IS_PAYMENT_METHOD_DONATION, REQ_PARAM_CSV,REQ_PARAM_BIL_COUNTRY, TAXATION_TYPE,REQ_PARAM_DATA }, new String[] {TAXATION_TYPE});//gets,sets
        addRequestValue(REQ_PARAM_CARD_EXP_MONTH, paymentMethod.getCardExpMonth());
        addRequestValue(REQ_PARAM_CARD_EXP_YEAR, paymentMethod.getCardExpYear());
        addRequestValue(REQ_PARAM_CARD_BRAND, paymentMethod.getCardBrand());
        addRequestValue(REQ_PARAM_ACCOUNT_NUMBER, paymentMethod.getAccountNumber());
        addRequestValue(REQ_PARAM_ABA_ROUTE_NUMBER, paymentMethod.getAbaRouteNumber());
        addRequestValue(REQ_PARAM_BANK_NAME, paymentMethod.getBankName());        
        addRequestValue(REQ_PARAM_BYPASS_BAD_ACCOUNT_CHECK, null);
        addRequestValue(REQ_PARAM_TERMS, paymentMethod.getTerms());
        addRequestValue(REQ_PARAM_ACCOUNT_NUMBER_VERIFY, paymentMethod.getAccountNumberVerify());
        addRequestValue(REQ_PARAM_BANK_ACCOUNT_TYPE, paymentMethod.getBankAccountType());        
        addRequestValue(REQ_PARAM_ACCOUNT_HOLDER, paymentMethod.getAccountHolder());
        addRequestValue(REQ_PARAM_BIL_ADDRESS_1, paymentMethod.getBillAddress1());
        addRequestValue(REQ_PARAM_BIL_ADDRESS_2, paymentMethod.getBillAddress2());
        addRequestValue(REQ_PARAM_BIL_APARTMENT, paymentMethod.getBillApt());        
        addRequestValue(REQ_PARAM_BIL_CITY, paymentMethod.getBillCity());
        addRequestValue(REQ_PARAM_BIL_STATE, paymentMethod.getBillState());
        addRequestValue(REQ_PARAM_BIL_ZIPCODE, paymentMethod.getBillZipCode());        
        addRequestValue(REQ_PARAM_PAYMENT_METHOD_TYPE, paymentMethod.getPaymentMethodType());
        addRequestValue(REQ_PARAM_CSV, paymentMethod.getCsv());
        addRequestValue(REQ_PARAM_IS_PAYMENT_METHOD_GIFT_CARD, "false");
        addRequestValue(REQ_PARAM_IS_PAYMENT_METHOD_DONATION, "false");
        addRequestValue(REQ_PARAM_EDIT_PAYMENT_ID, paymentMethod.getPaymentMethodId());
        addRequestValue(REQ_PARAM_BIL_COUNTRY, paymentMethod.getBillingCtry());
        addRequestValue(REQ_PARAM_CAPTCHA_TOKEN, paymentMethod.getCaptchaToken());
        
        getWrapTarget().setActionName(ACTION_EDIT_PAYMENT_METHOD);
        setMethodMode(true);
        return new ResultBundle(executeTagLogic(), this);
    }

    public ResultBundle deletePaymentMethod(String paymentMethodId) throws FDException {
        addExpectedSessionValues(new String[] { SESSION_PARAM_APPLICATION, SESSION_PARAM_CUSTOMER_SERVICE_REP, SESSION_PARAM_CRM_AGENT, SESSION_PARAM_MAKE_GOOD_ORDER, TAXATION_TYPE_SESSION , SessionName.PAYPAL_DEVICE_ID },
                new String[] { SESSION_PARAM_USER, SESSION_PARAM_MAKE_GOOD_ORDER, TAXATION_TYPE_SESSION , SessionName.PAYPAL_DEVICE_ID }); //gets,sets
        addExpectedRequestValues(new String[] { REQ_PARAM_DELETE_PAYMENT_ID, TAXATION_TYPE}, new String[] {TAXATION_TYPE});//gets,sets
        addRequestValue(REQ_PARAM_DELETE_PAYMENT_ID, paymentMethodId);
        getWrapTarget().setActionName(ACTION_DELETE_PAYMENT_METHOD);
        setMethodMode(true);
        return new ResultBundle(executeTagLogic(), this);
    }

    public ResultBundle deletePaymentMethodEx(String paymentMethodId) throws FDException {
        addExpectedSessionValues(new String[] { SESSION_PARAM_APPLICATION, SESSION_PARAM_CUSTOMER_SERVICE_REP, SESSION_PARAM_CRM_AGENT, SESSION_PARAM_MAKE_GOOD_ORDER, TAXATION_TYPE_SESSION , SessionName.PAYPAL_DEVICE_ID },
                new String[] { SESSION_PARAM_USER, SESSION_PARAM_MAKE_GOOD_ORDER, TAXATION_TYPE_SESSION, SessionName.PAYPAL_DEVICE_ID  }); //gets,sets
        addExpectedRequestValues(new String[] { REQ_PARAM_DELETE_PAYMENT_ID, TAXATION_TYPE}, new String[] {TAXATION_TYPE});//gets,sets
        addRequestValue(REQ_PARAM_DELETE_PAYMENT_ID, paymentMethodId);
        getWrapTarget().setActionName(ACTION_DELETE_PAYMENT_METHOD);
        setMethodMode(true);
        ActionResult actionResult = executeTagLogic();
        LOGGER.debug("deletePaymentMethodEx[executeTagLogic] :"+ actionResult);
        String successPage = ((CheckoutControllerTag) wrapTarget).getSuccessPage();

        if (actionResult == null) {
            actionResult = new ActionResult();
        }

        CheckoutControllerTag wrappedTag = (CheckoutControllerTag) this.getWrapTarget();
        if (wrappedTag.getAgeVerificationPage().equals(successPage)) {
        	LOGGER.debug("deletePaymentMethodEx[ERR_AGE_VERIFICATION] :"+ successPage);
            actionResult.addError(new ActionError(ERR_AGE_VERIFICATION, MobileApiProperties.getMediaPath()
                    + MobileApiProperties.getAlcoholAgeWarningMediaPath()));
        }
        
        return new ResultBundle(actionResult, this);    
        }
    
    public ResultBundle reserveDeliveryTimeslot(String deliveryTimeslotId) throws FDException {
        addExpectedSessionValues(new String[] { SESSION_PARAM_APPLICATION, SESSION_PARAM_MAKE_GOOD_ORDER, TAXATION_TYPE_SESSION, SessionName.PAYPAL_DEVICE_ID  }, new String[] { SESSION_PARAM_USER, SESSION_PARAM_MAKE_GOOD_ORDER, TAXATION_TYPE_SESSION , SessionName.PAYPAL_DEVICE_ID }); //gets,sets
        addExpectedRequestValues(new String[] { REQ_PARAM_CHEF_TABLE, REQ_PARAM_SLOT_ID, TAXATION_TYPE }, new String[] { REQ_PARAM_SLOT_ID,TAXATION_TYPE });//gets,sets
        addRequestValue(REQ_PARAM_SLOT_ID, deliveryTimeslotId);
        getWrapTarget().setActionName(ACTION_RESERVE_DELIVERY_TIMESLOT);
        setMethodMode(true);
        return new ResultBundle(executeTagLogic(), this);
    }

    /*
     * DUP: com.freshdirect.webapp.taglib.fdstore.CheckoutControllerTag
     * LAST UPDATED ON: 9/30/2009
     * LAST UPDATED WITH SVN#: 6017
     * WHY: Need to detect if tag is trying to redirect to age verification page. would like to 
     *     reference but it's a private field.
     * WHAT: constant in tag class that defines URL for page verification.
     */
    public ResultBundle setCheckoutDeliveryAddress(SessionUser user, String selectAddressId, DeliveryAddressType type) throws FDException {
        addExpectedSessionValues(new String[] { SESSION_PARAM_APPLICATION, SESSION_PARAM_CUSTOMER_SERVICE_REP, SESSION_PARAM_CRM_AGENT, SESSION_PARAM_MAKE_GOOD_ORDER, TAXATION_TYPE_SESSION, SessionName.PAYPAL_DEVICE_ID  },
                new String[] { SESSION_PARAM_USER, SessionName.SIGNUP_WARNING, SESSION_PARAM_MAKE_GOOD_ORDER, TAXATION_TYPE_SESSION, SessionName.PAYPAL_DEVICE_ID  }); //gets,sets
        LOGGER.debug("setCheckoutDeliveryAddress[START] :"+ selectAddressId + ","+type);
        addExpectedRequestValues(new String[] { REQ_PARAM_CHEF_TABLE, REQ_PARAM_PAYMENT_METHOD_ID, REQ_PARAM_BILLING_REF, TAXATION_TYPE }, new String[] {
                REQ_PARAM_PAYMENT_METHOD_ID, REQ_PARAM_BILLING_REF, TAXATION_TYPE });//gets,sets

        addRequestValue("contact_phone_" + selectAddressId, null); //TODO: move to constant file

        //Treat depot and pickup same.
        if (DeliveryAddressType.DEPOT.equals(type) || DeliveryAddressType.PICKUP.equals(type)) {
            selectAddressId = "DEPOT_" + selectAddressId;
        }
        addRequestValue("selectAddressList", selectAddressId); //TODO: move to constant file
        addRequestValue("corpDlvInstructions", null); //TODO: move to constant file
        addRequestValue(EnumUserInfoName.DLV_DELIVERY_INSTRUCTIONS.getCode(), null);
        addRequestValue(EnumUserInfoName.DLV_ALTERNATE_DELIVERY.getCode(), null);
        addRequestValue(EnumUserInfoName.DLV_UNATTENDED_CONSENT_SEEN.getCode(), null);
        addRequestValue(EnumUserInfoName.DLV_UNATTENDED_DELIVERY_INSTRUCTIONS.getCode(), null);
        addRequestValue(EnumUserInfoName.DLV_UNATTENDED_DELIVERY_OPT.getCode(), null);
        addRequestValue("makeGoodOrder", null); //TODO: move to constant file

        getWrapTarget().setActionName(ACTION_SET_DELIVERY_ADDRESS);
        setMethodMode(true);

        ActionResult actionResult = executeTagLogic();
        LOGGER.debug("setCheckoutDeliveryAddress[executeTagLogic] :"+ actionResult);
        String successPage = ((CheckoutControllerTag) wrapTarget).getSuccessPage();

        if (actionResult == null) {
            actionResult = new ActionResult();
        }

        CheckoutControllerTag wrappedTag = (CheckoutControllerTag) this.getWrapTarget();
        if (null !=successPage && successPage.indexOf(wrappedTag.getBlockedAddressPage()) >-1) {
        	actionResult.addError(new ActionError("ERR_ALCOHOL_DELIVERY_AREA_RESTRICTION_NEW", "Wines and spirits are not available for pick-up orders. You may choose a New York State delivery address or you may continue checkout without the wines and spirits in your cart. You may visit FreshDirect Wines & Spirits to shop in person. FreshDirect Wines & Spirits is located at 620 Fifth Avenue, Brooklyn, NY 11215. Store hours are M-W 1pm-10pm, Th-Sa 11am-10pm, Su 12pm-8pm."));
//        	actionResult.addError(new ActionError(ERR_ALCOHOL_DELIVERY_AREA_RESTRICTION, "Alcohol cannot be delivery to the address specified."));
        }else
        if (wrappedTag.getAgeVerificationPage().equals(successPage)) {
        	LOGGER.debug("setCheckoutDeliveryAddress[ERR_AGE_VERIFICATION] :"+ successPage);
            actionResult.addError(new ActionError(ERR_AGE_VERIFICATION, MobileApiProperties.getMediaPath()
                    + MobileApiProperties.getAlcoholAgeWarningMediaPath()));
        }
        LOGGER.debug("setCheckoutDeliveryAddress[END] :"+ selectAddressId + ","+type);
        return new ResultBundle(actionResult, this);
    }
    
    public ResultBundle setCheckoutOrderMobileNumber(SessionUser user, String orderMobileNumber) throws FDException	 {
        addExpectedSessionValues(new String[] { SESSION_PARAM_APPLICATION, SESSION_PARAM_CUSTOMER_SERVICE_REP, SESSION_PARAM_CRM_AGENT, SESSION_PARAM_MAKE_GOOD_ORDER, TAXATION_TYPE_SESSION, SessionName.PAYPAL_DEVICE_ID  },
                new String[] { SESSION_PARAM_USER, SESSION_PARAM_MAKE_GOOD_ORDER, TAXATION_TYPE_SESSION, SessionName.PAYPAL_DEVICE_ID  }); //gets,sets
        addExpectedRequestValues(new String[] { REQ_PARAM_MOBILE_NUMBER, TAXATION_TYPE}, new String[] {TAXATION_TYPE});//gets,sets
        addRequestValue(REQ_PARAM_MOBILE_NUMBER, orderMobileNumber);
        addRequestValue("orderMobileNumber", orderMobileNumber);
       
        getWrapTarget().setActionName(ACTION_SET_ORDER_MOBILE_NUMBER);
        setMethodMode(true);
        ActionResult actionResult = executeTagLogic();
        LOGGER.debug("setorderMobileNumber[executeTagLogic] :"+ actionResult);
        String successPage = ((CheckoutControllerTag) wrapTarget).getSuccessPage();

        if (actionResult == null) {
            actionResult = new ActionResult();
        }

        CheckoutControllerTag wrappedTag = (CheckoutControllerTag) this.getWrapTarget();
        if (wrappedTag.getAgeVerificationPage().equals(successPage)) {
        	LOGGER.debug("setorderMobileNumber[ERR_AGE_VERIFICATION] :"+ successPage);
            actionResult.addError(new ActionError(ERR_AGE_VERIFICATION, MobileApiProperties.getMediaPath()
                    + MobileApiProperties.getAlcoholAgeWarningMediaPath()));
        }
        return new ResultBundle(actionResult, this);
    }
    
    
    public ResultBundle deleteDeliveryAddress(String deleteShipToAddressId) throws FDException {
        addExpectedSessionValues(new String[] { SESSION_PARAM_APPLICATION, SESSION_PARAM_CUSTOMER_SERVICE_REP, SESSION_PARAM_CRM_AGENT, SESSION_PARAM_MAKE_GOOD_ORDER, TAXATION_TYPE_SESSION, SessionName.PAYPAL_DEVICE_ID  },
                new String[] { SESSION_PARAM_USER, SESSION_PARAM_MAKE_GOOD_ORDER, TAXATION_TYPE_SESSION , SessionName.PAYPAL_DEVICE_ID }); //gets,sets
        addExpectedRequestValues(new String[] { REQ_PARAM_DELETE_SHIP_ADDRESS_ID, TAXATION_TYPE}, new String[] {TAXATION_TYPE});//gets,sets
        addRequestValue(REQ_PARAM_DELETE_SHIP_ADDRESS_ID, deleteShipToAddressId);
        getWrapTarget().setActionName(ACTION_DELETE_DELIVERY_ADDRESS);
        setMethodMode(true);
        return new ResultBundle(executeTagLogic(), this);
    }
    
    public ResultBundle deleteDeliveryAddressEx(String deleteShipToAddressId) throws FDException {
        addExpectedSessionValues(new String[] { SESSION_PARAM_APPLICATION, SESSION_PARAM_CUSTOMER_SERVICE_REP, SESSION_PARAM_CRM_AGENT, SESSION_PARAM_MAKE_GOOD_ORDER, TAXATION_TYPE_SESSION, SessionName.PAYPAL_DEVICE_ID  },
                new String[] { SESSION_PARAM_USER, SESSION_PARAM_MAKE_GOOD_ORDER, TAXATION_TYPE_SESSION, SessionName.PAYPAL_DEVICE_ID  }); //gets,sets
        addExpectedRequestValues(new String[] { REQ_PARAM_DELETE_SHIP_ADDRESS_ID, TAXATION_TYPE}, new String[] {TAXATION_TYPE});//gets,sets
        addRequestValue(REQ_PARAM_DELETE_SHIP_ADDRESS_ID, deleteShipToAddressId);
        getWrapTarget().setActionName(ACTION_DELETE_DELIVERY_ADDRESS);
        setMethodMode(true);
        ActionResult actionResult = executeTagLogic();
        LOGGER.debug("deleteDeliveryAddressEx[executeTagLogic] :"+ actionResult);
        String successPage = ((CheckoutControllerTag) wrapTarget).getSuccessPage();

        if (actionResult == null) {
            actionResult = new ActionResult();
        }

        CheckoutControllerTag wrappedTag = (CheckoutControllerTag) this.getWrapTarget();
        if (wrappedTag.getAgeVerificationPage().equals(successPage)) {
        	LOGGER.debug("deleteDeliveryAddressEx[ERR_AGE_VERIFICATION] :"+ successPage);
            actionResult.addError(new ActionError(ERR_AGE_VERIFICATION, MobileApiProperties.getMediaPath()
                    + MobileApiProperties.getAlcoholAgeWarningMediaPath()));
        }
        
        return new ResultBundle(actionResult, this);  
        }
    
    /*
     * DUP: com.freshdirect.webapp.taglib.fdstore.AgeVerificationControllerTag
     * LAST UPDATED ON: 10/12/2009
     * LAST UPDATED WITH SVN#: 5951
     * WHY: Address verification for alcohol was tightly integrated with age verification.  
     *     The logic is duplicated here so that could be better reused
     * WHAT: logic to check is alcohol delivery is allowed for specified address
     */
    /**
     * @param address
     * @return
     * @throws JspException
     * @throws FDResourceException
     */
    private boolean verifyAddress(ErpAddressModel address) throws FDResourceException {
        if (!FDDeliveryManager.getInstance().checkForAlcoholDelivery(address)) {
            //response.sendRedirect(response.encodeRedirectURL(blockedAddressPage));
            return false;
        }
        return true;
    }
    
    private ResultBundle doPaymentTagLogic() throws FDException {
    	
    	ActionResult actionResult = executeTagLogic();        
        String successPage = ((CheckoutControllerTag) wrapTarget).getSuccessPage();

        if (actionResult == null) {
            actionResult = new ActionResult();
        }

        CheckoutControllerTag wrappedTag = (CheckoutControllerTag) this.getWrapTarget();
        if (successPage != null && (successPage.startsWith(wrappedTag.getEbtUnavailableItemsPage())
        		|| successPage.startsWith(wrappedTag.getEbtCRMUnavailableItemsPage())) ) {
        	
            actionResult.addError(new ActionError(ERR_EBT_RESTRICTED, MSG_EBT_PRODUCT_NOT_ALLOWED));
        }
        
        return new ResultBundle(actionResult, this);
    }

}