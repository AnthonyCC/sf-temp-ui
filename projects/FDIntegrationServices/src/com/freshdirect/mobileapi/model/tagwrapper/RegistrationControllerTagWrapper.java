package com.freshdirect.mobileapi.model.tagwrapper;

import org.apache.log4j.Category;

import com.freshdirect.customer.EnumDeliverySetting;
import com.freshdirect.customer.ErpCustomerInfoModel;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mobileapi.controller.data.request.DeliveryAddressRequest;
import com.freshdirect.mobileapi.controller.data.request.EmailPreferenceRequest;
import com.freshdirect.mobileapi.controller.data.request.MobilePreferenceRequest;
import com.freshdirect.mobileapi.controller.data.request.RegisterMessage;
import com.freshdirect.mobileapi.controller.data.request.RegisterMessageFdxRequest;
import com.freshdirect.mobileapi.controller.data.request.SocialRegisterRequest;
import com.freshdirect.mobileapi.model.ResultBundle;
import com.freshdirect.sms.EnumSMSAlertStatus;
import com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName;
import com.freshdirect.webapp.taglib.fdstore.RegistrationControllerTag;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class RegistrationControllerTagWrapper extends ControllerTagWrapper implements RequestParamName, SessionParamName {

	private static Category LOGGER = LoggerFactory.getInstance(RegistrationControllerTagWrapper.class);
	
    public static final String ACTION_REGISTER_FROM_IPHONE = "registerEx";
    
    
    
    //public static final String ACTION_REGISTER_FOR_FDX = "registerFDX";
    
    public static final String ACTION_ADD_DELIVERY_ADDRESS = "addDeliveryAddressEx";
    
    public static final String ACTION_EDIT_DELIVERY_ADDRESS = "editDeliveryAddress";
    
    public static final String ACTION_SET_MOBILE_PREFERENCES = "mobilepreferences"; 
    
    public static final String ACTION_SET_EMAIL_PREFERENCE = "changeEmailPreferenceLevel";


    public RegistrationControllerTagWrapper(FDUserI user) {
        super(new RegistrationControllerTag(), user);
    }

    
    public ResultBundle register(RegisterMessage registerMessage) throws FDException {
        addExpectedSessionValues(new String[] {SESSION_PARAM_APPLICATION,SESSION_PARAM_CUSTOMER_SERVICE_REP, SESSION_PARAM_CRM_AGENT,
        		SESSION_PARAM_SS_PREV_RECOMMENDATIONS, SESSION_PARAM_SAVINGS_FEATURE_LOOK_UP_TABLE,
        		SESSION_PARAM_PREV_SAVINGS_VARIANT, SESSION_PARAM_USER, SESSION_PARAM_REFERRAL_NAME,SessionName.SOCIAL_USER}, new String[] {SESSION_PARAM_SS_PREV_RECOMMENDATIONS, 
        		SESSION_PARAM_SAVINGS_FEATURE_LOOK_UP_TABLE,SESSION_PARAM_PREV_SAVINGS_VARIANT, SESSION_PARAM_USER, SESSION_PARAM_LITE_SIGNUP_COMPLETE,
        		SESSION_PARAM_PENDING_REGISTRATION_EVENT, SESSION_PARAM_PENDING_LOGIN_EVENT, SESSION_PARAM_REGISTRATION_LOCATION, SESSION_PARAM_REGISTRATION_ORIG_ZIP_CODE,SessionName.SOCIAL_USER}); //gets,sets
    	
        addExpectedRequestValues(new String[] {"title", EnumUserInfoName.DLV_FIRST_NAME.getCode(),EnumUserInfoName.DLV_LAST_NAME.getCode(),
        		EnumUserInfoName.DLV_HOME_PHONE.getCode(),"homephoneext","busphone", "busphoneext", "cellphone", "cellphoneext",
        		"workDepartment", "employeeId", EnumUserInfoName.EMAIL.getCode(), EnumUserInfoName.REPEAT_EMAIL.getCode(),
        		EnumUserInfoName.ALT_EMAIL.getCode(), EnumUserInfoName.PASSWORD.getCode(), EnumUserInfoName.REPEAT_PASSWORD.getCode(),
        		"password_hint","terms",EnumUserInfoName.DLV_DELIVERY_INSTRUCTIONS.getCode(),
        		EnumUserInfoName.DLV_SERVICE_TYPE.getCode(),
        		EnumUserInfoName.DLV_COMPANY_NAME.getCode(),
        		EnumUserInfoName.DLV_ADDRESS_1.getCode(),
        		EnumUserInfoName.DLV_ADDRESS_2.getCode(),
        		EnumUserInfoName.DLV_APARTMENT.getCode(),
        		EnumUserInfoName.DLV_CITY.getCode(),
        		EnumUserInfoName.DLV_STATE.getCode(),
        		EnumUserInfoName.DLV_ZIPCODE.getCode(),
        		EnumUserInfoName.DLV_WORK_PHONE.getCode(),
        		"selectAddressList", "deliveryTypeFlag", REQ_PARAM_LITE_SIGNUP,REQ_PARAM_LITE_SIGNUP_SOCIAL,"userToken","provider"}
        		, new String[] {});//gets,sets
        
        addRequestValue(EnumUserInfoName.DLV_FIRST_NAME.getCode(), registerMessage.getFirstName());
        addRequestValue(EnumUserInfoName.DLV_LAST_NAME.getCode(), registerMessage.getLastName());
        addRequestValue(EnumUserInfoName.EMAIL.getCode(), registerMessage.getEmail());
        addRequestValue(EnumUserInfoName.REPEAT_EMAIL.getCode(), registerMessage.getConfirmEmail());
        addRequestValue(EnumUserInfoName.PASSWORD.getCode(), registerMessage.getPassword());
        addRequestValue(EnumUserInfoName.REPEAT_PASSWORD.getCode(), registerMessage.getConfirmPassword());
        addRequestValue("password_hint", registerMessage.getSecurityQuestion());
        addRequestValue(EnumUserInfoName.DLV_SERVICE_TYPE.getCode(), registerMessage.getServiceType());
        addRequestValue(EnumUserInfoName.DLV_ADDRESS_1.getCode(), registerMessage.getAddress1());
        addRequestValue(EnumUserInfoName.DLV_APARTMENT.getCode(), registerMessage.getApartment());
        addRequestValue(EnumUserInfoName.DLV_CITY.getCode(), registerMessage.getCity());
        addRequestValue(EnumUserInfoName.DLV_STATE.getCode(), registerMessage.getState());
        addRequestValue(EnumUserInfoName.DLV_ZIPCODE.getCode(), registerMessage.getZipCode());  
        addRequestValue(EnumUserInfoName.DLV_WORK_PHONE.getCode(), registerMessage.getWorkPhone());
        
        addRequestValue("terms", "Y");
        //addRequestValue("partialDelivery", registerMessage.isPartialDelivery() ? "Y" : "");
        
        
        ((RegistrationControllerTag) getWrapTarget()).setActionName(ACTION_REGISTER_FROM_IPHONE);
        setMethodMode(true);
        ResultBundle resultBundle = new ResultBundle(executeTagLogic(), this);
        return resultBundle;
        
    }
    
   
    public ResultBundle registerSocial(SocialRegisterRequest registerMessage) throws FDException {
        addExpectedSessionValues(new String[] {SESSION_PARAM_APPLICATION,SESSION_PARAM_CUSTOMER_SERVICE_REP, SESSION_PARAM_CRM_AGENT,
        		SESSION_PARAM_SS_PREV_RECOMMENDATIONS, SESSION_PARAM_SAVINGS_FEATURE_LOOK_UP_TABLE,
        		SESSION_PARAM_PREV_SAVINGS_VARIANT, SESSION_PARAM_USER, SESSION_PARAM_REFERRAL_NAME,SessionName.SOCIAL_USER,SessionName.LITECONTACTINFO,SessionName.LITEACCOUNTINFO}, new String[] {SESSION_PARAM_SS_PREV_RECOMMENDATIONS, 
        		SESSION_PARAM_SAVINGS_FEATURE_LOOK_UP_TABLE,SESSION_PARAM_PREV_SAVINGS_VARIANT, SESSION_PARAM_USER, SESSION_PARAM_LITE_SIGNUP_COMPLETE,
        		SESSION_PARAM_PENDING_REGISTRATION_EVENT, SESSION_PARAM_PENDING_LOGIN_EVENT, SESSION_PARAM_REGISTRATION_LOCATION, SESSION_PARAM_REGISTRATION_ORIG_ZIP_CODE,SessionName.SOCIAL_USER,SessionName.LITECONTACTINFO,SessionName.LITEACCOUNTINFO}); //gets,sets
    	
        addExpectedRequestValues(new String[] {"title", EnumUserInfoName.DLV_FIRST_NAME.getCode(),EnumUserInfoName.DLV_LAST_NAME.getCode(),
        		EnumUserInfoName.DLV_HOME_PHONE.getCode(),"homephoneext","busphone", "busphoneext", "cellphone", "cellphoneext",
        		"workDepartment", "employeeId", EnumUserInfoName.EMAIL.getCode(), EnumUserInfoName.REPEAT_EMAIL.getCode(),
        		EnumUserInfoName.ALT_EMAIL.getCode(), 
        		"password_hint","terms",EnumUserInfoName.DLV_DELIVERY_INSTRUCTIONS.getCode(),
        		EnumUserInfoName.DLV_SERVICE_TYPE.getCode(),
        		EnumUserInfoName.DLV_COMPANY_NAME.getCode(),
        		EnumUserInfoName.DLV_ADDRESS_1.getCode(),
        		EnumUserInfoName.DLV_ADDRESS_2.getCode(),
        		EnumUserInfoName.DLV_APARTMENT.getCode(),
        		EnumUserInfoName.DLV_CITY.getCode(),
        		EnumUserInfoName.DLV_STATE.getCode(),
        		EnumUserInfoName.DLV_ZIPCODE.getCode(),
        		EnumUserInfoName.DLV_WORK_PHONE.getCode(),
        		"selectAddressList", "deliveryTypeFlag", REQ_PARAM_LITE_SIGNUP,REQ_PARAM_LITE_SIGNUP_SOCIAL,"fd_successPage","userToken","provider"}
        		, new String[] {"fd_successPage","userToken","provider"});//gets,sets
        
        addRequestValue(EnumUserInfoName.DLV_FIRST_NAME.getCode(), registerMessage.getFirstName());
        addRequestValue(EnumUserInfoName.DLV_LAST_NAME.getCode(), registerMessage.getLastName());
        addRequestValue(EnumUserInfoName.EMAIL.getCode(), registerMessage.getEmail());
        addRequestValue(EnumUserInfoName.REPEAT_EMAIL.getCode(), registerMessage.getEmail());
        String password="^0X!3X!X!1^";
        addRequestValue(EnumUserInfoName.PASSWORD.getCode(), password);
        addRequestValue(EnumUserInfoName.REPEAT_PASSWORD.getCode(), password);
        addRequestValue("password_hint", registerMessage.getSecurityQuestion());
        addRequestValue(EnumUserInfoName.DLV_SERVICE_TYPE.getCode(), registerMessage.getServiceType());
        addRequestValue(EnumUserInfoName.DLV_ADDRESS_1.getCode(), registerMessage.getAddress1());
        addRequestValue(EnumUserInfoName.DLV_APARTMENT.getCode(), registerMessage.getApartment());
        addRequestValue(EnumUserInfoName.DLV_CITY.getCode(), registerMessage.getCity());
        addRequestValue(EnumUserInfoName.DLV_STATE.getCode(), registerMessage.getState());
        addRequestValue(EnumUserInfoName.DLV_ZIPCODE.getCode(), registerMessage.getZipCode());  
        addRequestValue(EnumUserInfoName.DLV_WORK_PHONE.getCode(), registerMessage.getWorkPhone());
        addRequestValue(REQ_PARAM_LITE_SIGNUP,"true");
        addRequestValue(REQ_PARAM_LITE_SIGNUP_SOCIAL,"true");
        addRequestValue("userToken",registerMessage.getUserToken());
        addRequestValue("provider",registerMessage.getProvider());
        
        
        addRequestValue("terms", "Y");
        //addRequestValue("partialDelivery", registerMessage.isPartialDelivery() ? "Y" : "");
        
        
        ((RegistrationControllerTag) getWrapTarget()).setActionName(ACTION_REGISTER_FROM_IPHONE);
        setMethodMode(true);
        ResultBundle resultBundle = new ResultBundle(executeTagLogic(), this);
        return resultBundle;
        
    }

    

    public ResultBundle addDeliveryAddress(DeliveryAddressRequest deliveryAddress) throws FDException {
        addExpectedSessionValues(new String[] { SESSION_PARAM_APPLICATION, SESSION_PARAM_CUSTOMER_SERVICE_REP, SESSION_PARAM_CRM_AGENT },
                new String[] { SESSION_PARAM_USER, SessionName.SIGNUP_WARNING }); //gets,sets
        addExpectedRequestValues(new String[] {EnumUserInfoName.DLV_FIRST_NAME.getCode(),EnumUserInfoName.DLV_LAST_NAME.getCode(),EnumUserInfoName.DLV_HOME_PHONE.getCode(),EnumUserInfoName.DLV_HOME_PHONE_EXT.getCode(),
        		EnumUserInfoName.DLV_ADDRESS_1.getCode(),EnumUserInfoName.DLV_ADDRESS_2.getCode(),EnumUserInfoName.DLV_APARTMENT.getCode(),EnumUserInfoName.DLV_CITY.getCode(),EnumUserInfoName.DLV_STATE.getCode(),EnumUserInfoName.DLV_ZIPCODE.getCode(),
        		EnumUserInfoName.DLV_COUNTRY.getCode(),EnumUserInfoName.DLV_DELIVERY_INSTRUCTIONS.getCode(),EnumUserInfoName.DLV_ALTERNATE_DELIVERY.getCode(),EnumUserInfoName.DLV_ALT_FIRSTNAME.getCode(),
        		EnumUserInfoName.DLV_ALT_LASTNAME.getCode(),EnumUserInfoName.DLV_ALT_APARTMENT.getCode(),EnumUserInfoName.DLV_ALT_CONTACT_PHONE.getCode(),
        		EnumUserInfoName.DLV_ALT_CONTACT_EXT.getCode(),EnumUserInfoName.DLV_UNATTENDED_DELIVERY_OPT.getCode(),EnumUserInfoName.DLV_UNATTENDED_DELIVERY_INSTRUCTIONS.getCode(),
        		EnumUserInfoName.DLV_UNATTENDED_CONSENT_SEEN.getCode(), EnumUserInfoName.DLV_ALT_PHONE.getCode(), EnumUserInfoName.DLV_ALT_EXT.getCode(),
        		EnumUserInfoName.DLV_COMPANY_NAME.getCode(), EnumUserInfoName.DLV_SERVICE_TYPE.getCode(),"first_name","last_name","homephone"}, new String[] {});//gets,sets
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
        
		if (deliveryAddress.isDoorman()) {
//APPDEV-4228  Doorman Property  not Adding/Editing addresses
        	//addRequestValue(EnumUserInfoName.DLV_ALTERNATE_DELIVERY.getCode(), EnumDeliverySetting.DOORMAN.getName());
			addRequestValue(EnumUserInfoName.DLV_ALTERNATE_DELIVERY.getCode(),
					EnumDeliverySetting.DOORMAN.getDeliveryCode());
		} else if (deliveryAddress.isNeighbor()) {
//APPDEV-4228  Doorman Property  not Adding/Editing addresses
        	//addRequestValue(EnumUserInfoName.DLV_ALTERNATE_DELIVERY.getCode(), EnumDeliverySetting.NEIGHBOR.getName());
			addRequestValue(EnumUserInfoName.DLV_ALTERNATE_DELIVERY.getCode(),
					EnumDeliverySetting.NEIGHBOR.getDeliveryCode());
		} else {
//APPDEV-4228  Doorman Property  not Adding/Editing addresses
        	//addRequestValue(EnumUserInfoName.DLV_ALTERNATE_DELIVERY.getCode(), EnumDeliverySetting.NONE.getName());
			addRequestValue(EnumUserInfoName.DLV_ALTERNATE_DELIVERY.getCode(),
					EnumDeliverySetting.NONE.getDeliveryCode());
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
        

        getWrapTarget().setActionName(ACTION_ADD_DELIVERY_ADDRESS);
        setMethodMode(true);
        return new ResultBundle(executeTagLogic(), this);
    }

    public ResultBundle editDeliveryAddress(DeliveryAddressRequest deliveryAddress) throws FDException {
        addExpectedSessionValues(new String[] { SESSION_PARAM_APPLICATION, SESSION_PARAM_CUSTOMER_SERVICE_REP, SESSION_PARAM_CRM_AGENT },
                new String[] { SESSION_PARAM_USER, SessionName.SIGNUP_WARNING }); //gets,sets
        addExpectedRequestValues(new String[] {EnumUserInfoName.DLV_FIRST_NAME.getCode(),EnumUserInfoName.DLV_LAST_NAME.getCode(),EnumUserInfoName.DLV_HOME_PHONE.getCode(),EnumUserInfoName.DLV_HOME_PHONE_EXT.getCode(),
        		EnumUserInfoName.DLV_ADDRESS_1.getCode(),EnumUserInfoName.DLV_ADDRESS_2.getCode(),EnumUserInfoName.DLV_APARTMENT.getCode(),EnumUserInfoName.DLV_CITY.getCode(),EnumUserInfoName.DLV_STATE.getCode(),EnumUserInfoName.DLV_ZIPCODE.getCode(),
        		EnumUserInfoName.DLV_COUNTRY.getCode(),EnumUserInfoName.DLV_DELIVERY_INSTRUCTIONS.getCode(),EnumUserInfoName.DLV_ALTERNATE_DELIVERY.getCode(),EnumUserInfoName.DLV_ALT_FIRSTNAME.getCode(),
        		EnumUserInfoName.DLV_ALT_LASTNAME.getCode(),EnumUserInfoName.DLV_ALT_APARTMENT.getCode(),EnumUserInfoName.DLV_ALT_CONTACT_PHONE.getCode(),
        		EnumUserInfoName.DLV_ALT_CONTACT_EXT.getCode(),EnumUserInfoName.DLV_UNATTENDED_DELIVERY_OPT.getCode(),EnumUserInfoName.DLV_UNATTENDED_DELIVERY_INSTRUCTIONS.getCode(),
        		EnumUserInfoName.DLV_UNATTENDED_CONSENT_SEEN.getCode(), EnumUserInfoName.DLV_ALT_PHONE.getCode(), EnumUserInfoName.DLV_ALT_EXT.getCode(),
        		EnumUserInfoName.DLV_COMPANY_NAME.getCode(), EnumUserInfoName.DLV_SERVICE_TYPE.getCode(), "homephone"}, new String[] {});//gets,sets
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
        addRequestValue(REQ_PARAM_UPDATE_SHIP_ADDRESS_ID, deliveryAddress.getShipToAddressId());

        getWrapTarget().setActionName(ACTION_EDIT_DELIVERY_ADDRESS);
        setMethodMode(true);
        return new ResultBundle(executeTagLogic(), this);
    }
    
    public ResultBundle setMobilePreferences(MobilePreferenceRequest reqestMessage, ErpCustomerInfoModel cm) throws FDException {
    	boolean currentOrderNotices = cm.getOrderNotices().equals(EnumSMSAlertStatus.NONE)? false:true;
		boolean currentOrderExceptions = cm.getOrderExceptions().equals(EnumSMSAlertStatus.NONE) ? false:true;
		boolean currentOffers = cm.getOffers().equals(EnumSMSAlertStatus.NONE) ? false:true;
		boolean currentPartnerMessages = cm.getPartnerMessages().equals(EnumSMSAlertStatus.NONE) ? false:true;
		String currentMobileNo = cm.getMobileNumber()==null ? "":cm.getMobileNumber().getPhone();
			
		
		
        addExpectedSessionValues(new String[] { SESSION_PARAM_APPLICATION, SESSION_PARAM_CUSTOMER_SERVICE_REP, SESSION_PARAM_CRM_AGENT, SESSION_PARAM_MAKE_GOOD_ORDER },
                new String[] { SESSION_PARAM_USER, SESSION_PARAM_MAKE_GOOD_ORDER }); //gets,sets
        addExpectedRequestValues(new String[] { REQ_PARAM_MOBILE_NUMBER,REQ_PARAM_TEXT_OFFERS,REQ_PARAM_TEXT_DELIVERY,REQ_PARAM_ORDER_NOTICES,REQ_PARAM_ORDER_EXCEPTIONS,REQ_PARAM_OFFERS,REQ_PARAM_PARTNER_MESSAGES,REQ_PARAM_ORDER_NOTICES_EXISTING,REQ_PARAM_ORDER_EXCETION_EXISTING,REQ_PARAM_OFFER_EXISTING,REQ_PARAM_PARTNER_EXISTING,REQ_PARAM_MOBILE_EXISTING}, new String[] {});//gets,sets
        addRequestValue(REQ_PARAM_MOBILE_NUMBER, reqestMessage.getMobile_number());
        addRequestValue(REQ_PARAM_ORDER_NOTICES, reqestMessage.getOrder_notices());
        addRequestValue(REQ_PARAM_ORDER_EXCEPTIONS, reqestMessage.getOrder_exceptions());
        addRequestValue(REQ_PARAM_OFFERS, reqestMessage.getOffers());
                
        addRequestValue(REQ_PARAM_ORDER_NOTICES_EXISTING, currentOrderNotices);
        addRequestValue(REQ_PARAM_ORDER_EXCETION_EXISTING, currentOrderExceptions);
        addRequestValue(REQ_PARAM_OFFER_EXISTING, currentOffers);
        addRequestValue(REQ_PARAM_PARTNER_EXISTING, currentPartnerMessages);
        addRequestValue(REQ_PARAM_MOBILE_EXISTING, currentMobileNo);
        getWrapTarget().setActionName(ACTION_SET_MOBILE_PREFERENCES);
        
        setMethodMode(true);
        return new ResultBundle(executeTagLogic(), this);
    }
    
    public ResultBundle setEmailPreferences(EmailPreferenceRequest reqestMessage, ErpCustomerInfoModel cm) throws FDException {
		//cm.setEmailPreferenceLevel(receive_emailLevel)
		
        addExpectedSessionValues(new String[] { SESSION_PARAM_APPLICATION, SESSION_PARAM_CUSTOMER_SERVICE_REP, SESSION_PARAM_CRM_AGENT, SESSION_PARAM_MAKE_GOOD_ORDER },
                new String[] { SESSION_PARAM_USER, SESSION_PARAM_MAKE_GOOD_ORDER }); //gets,sets
        addExpectedRequestValues(new String[] { REQ_PARAM_EMAIL_PREFERENCE_LEVEL}, new String[] {});//gets,sets
                
        addRequestValue(REQ_PARAM_EMAIL_PREFERENCE_LEVEL, cm.getEmailPreferenceLevel());
        
        getWrapTarget().setActionName(ACTION_SET_EMAIL_PREFERENCE);
        setMethodMode(true);
        return new ResultBundle(executeTagLogic(), this);
    }

}
