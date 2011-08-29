package com.freshdirect.mobileapi.model.tagwrapper;

import org.apache.log4j.Category;

import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mobileapi.controller.data.request.RegisterMessage;
import com.freshdirect.mobileapi.controller.data.request.ZipCheck;
import com.freshdirect.mobileapi.model.ResultBundle;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName;
import com.freshdirect.webapp.taglib.fdstore.FDShoppingCartControllerTag;
import com.freshdirect.webapp.taglib.fdstore.RegistrationControllerTag;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.SiteAccessControllerTag;

public class RegistrationControllerTagWrapper extends NonStandardControllerTagWrapper implements RequestParamName, SessionParamName {

	private static Category LOGGER = LoggerFactory.getInstance(RegistrationControllerTagWrapper.class);
	
    public static final String ACTION_REGISTER_FROM_IPHONE = "registerFromIphone";

    public RegistrationControllerTagWrapper(FDUserI user) {
        super(new RegistrationControllerTag(), user);
    }

    @Override
    protected void setResult() {
        ((RegistrationControllerTag) wrapTarget).setResult(ACTION_RESULT);
    }

    @Override
    protected Object getResult() throws FDException {
        // TODO Auto-generated method stub
        return null;
    }
    
    public ResultBundle register(RegisterMessage registerMessage) throws FDException {
        addExpectedSessionValues(new String[] {SESSION_PARAM_APPLICATION,SESSION_PARAM_CUSTOMER_SERVICE_REP, SESSION_PARAM_CRM_AGENT,
        		SESSION_PARAM_SS_PREV_RECOMMENDATIONS, SESSION_PARAM_SAVINGS_FEATURE_LOOK_UP_TABLE,
        		SESSION_PARAM_PREV_SAVINGS_VARIANT, SESSION_PARAM_USER}, new String[] {SESSION_PARAM_SS_PREV_RECOMMENDATIONS, 
        		SESSION_PARAM_SAVINGS_FEATURE_LOOK_UP_TABLE,SESSION_PARAM_PREV_SAVINGS_VARIANT, SESSION_PARAM_USER}); //gets,sets
    	
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
        		"selectAddressList", "deliveryTypeFlag"}
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
        addRequestValue("terms", "Y");
        //addRequestValue("partialDelivery", registerMessage.isPartialDelivery() ? "Y" : "");
        
        
        ((RegistrationControllerTag) getWrapTarget()).setActionName(ACTION_REGISTER_FROM_IPHONE);
        setMethodMode(true);
        ResultBundle resultBundle = new ResultBundle(executeTagLogic(), this);
        return resultBundle;
        
    }
    
}
