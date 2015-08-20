package com.freshdirect.mobileapi.model.tagwrapper;

import org.apache.log4j.Category;

import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mobileapi.controller.data.request.ZipCheck;
import com.freshdirect.mobileapi.model.ResultBundle;
import com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.SiteAccessControllerTag;

public class SiteAccessControllerTagWrapper extends NonStandardControllerTagWrapper implements RequestParamName, SessionParamName, SessionName {

	private static Category LOGGER = LoggerFactory.getInstance(SiteAccessControllerTagWrapper.class);
	
    public static final String ACTION_CHECK_BY_ZIP = "checkByZipCode";

    public static final String ACTION_CHECK_BY_ADDRESS = "checkByAddress";
    public static final String ACTION_REGISTRATION = "signupLite";

	public static final String REQUESTED_SERVICE_TYPE_DLV_STATUS = "requestedServiceTypeDlvStatus";
	
	public static final String AVAILABLE_SERVICE_TYPES = "availableServiceTypes";

    public SiteAccessControllerTagWrapper(FDUserI user) {
        super(new SiteAccessControllerTag(), user);
    }

    @Override
    protected void setResult() {
        ((SiteAccessControllerTag) wrapTarget).setResult(ACTION_RESULT);
    }

    @Override
    protected Object getResult() throws FDException {
        // TODO Auto-generated method stub
        return null;
    }
    
    public ResultBundle checkByZipcode(ZipCheck zipcheck) throws FDException {
        addExpectedSessionValues(new String[] {SESSION_PARAM_APPLICATION,SESSION_PARAM_CUSTOMER_SERVICE_REP, SESSION_PARAM_CRM_AGENT,
        		SESSION_PARAM_SS_PREV_RECOMMENDATIONS, SESSION_PARAM_SAVINGS_FEATURE_LOOK_UP_TABLE,
        		SESSION_PARAM_PREV_SAVINGS_VARIANT, SESSION_PARAM_USER, SESSION_PARAM_SITEACCESS_MOREPAGE, STORE_CONTEXT}
        		, new String[] {SESSION_PARAM_SS_PREV_RECOMMENDATIONS, 
        		SESSION_PARAM_SAVINGS_FEATURE_LOOK_UP_TABLE,SESSION_PARAM_PREV_SAVINGS_VARIANT, SESSION_PARAM_USER, SESSION_PARAM_SITEACCESS_MOREPAGE, STORE_CONTEXT}); //gets,sets
    	
        addExpectedRequestValues(new String[] {EnumUserInfoName.DLV_ZIPCODE.getCode(),EnumUserInfoName.DLV_CORP_ZIPCODE.getCode(),
        		REQ_PARAM_SERVICE_TYPE, REQ_PARAM_CORP_SERVICE_TYPE, EnumUserInfoName.DLV_ADDRESS_1.getCode(),
        		EnumUserInfoName.DLV_APARTMENT.getCode(), EnumUserInfoName.DLV_CITY.getCode(),EnumUserInfoName.DLV_STATE.getCode()
        		,REQ_PARAM_DELIVERY_STATUS, REQ_PARAM_REFERRAL_REGISTRATION }
        		, new String[] {REQ_PARAM_DELIVERY_STATUS, "failed"});//gets,sets
        
        if(EnumServiceType.CORPORATE.getName().equals(zipcheck.getServiceType())){
        	addRequestValue(EnumUserInfoName.DLV_CORP_ZIPCODE.getCode(), zipcheck.getZipCode());	
        	addRequestValue(REQ_PARAM_CORP_SERVICE_TYPE, zipcheck.getServiceType());
        } else {
        	addRequestValue(EnumUserInfoName.DLV_ZIPCODE.getCode(), zipcheck.getZipCode());	
        	addRequestValue(REQ_PARAM_SERVICE_TYPE, zipcheck.getServiceType());
        }
        addRequestValue(EnumUserInfoName.DLV_ADDRESS_1.getCode(), zipcheck.getAddress1());
        addRequestValue(EnumUserInfoName.DLV_APARTMENT.getCode(), zipcheck.getApartment());
        addRequestValue(EnumUserInfoName.DLV_CITY.getCode(), zipcheck.getCity());
        addRequestValue(EnumUserInfoName.DLV_STATE.getCode(), zipcheck.getState());
        
        ((SiteAccessControllerTag) getWrapTarget()).setAction(ACTION_CHECK_BY_ZIP);
        setMethodMode(true);
        ResultBundle resultBundle = new ResultBundle(executeTagLogic(), this);
        resultBundle.addExtraData(REQUESTED_SERVICE_TYPE_DLV_STATUS,  
        		((SiteAccessControllerTag) getWrapTarget()).getRequestedServiceTypeDlvStatus());
        return resultBundle;
        
    }
    public ResultBundle checkByAddress(ZipCheck zipcheck) throws FDException {
        addExpectedSessionValues(new String[] {SESSION_PARAM_APPLICATION,SESSION_PARAM_CUSTOMER_SERVICE_REP, SESSION_PARAM_CRM_AGENT,
        		SESSION_PARAM_SS_PREV_RECOMMENDATIONS, SESSION_PARAM_SAVINGS_FEATURE_LOOK_UP_TABLE,
        		SESSION_PARAM_PREV_SAVINGS_VARIANT, SESSION_PARAM_USER, SESSION_PARAM_SITEACCESS_MOREPAGE, STORE_CONTEXT}, new String[] {SESSION_PARAM_SS_PREV_RECOMMENDATIONS, 
        		SESSION_PARAM_SAVINGS_FEATURE_LOOK_UP_TABLE,SESSION_PARAM_PREV_SAVINGS_VARIANT, SESSION_PARAM_USER, SESSION_PARAM_SITEACCESS_MOREPAGE, STORE_CONTEXT}); //gets,sets
    	
        addExpectedRequestValues(new String[] {EnumUserInfoName.DLV_ZIPCODE.getCode(),EnumUserInfoName.DLV_CORP_ZIPCODE.getCode(),
        		REQ_PARAM_SERVICE_TYPE, REQ_PARAM_CORP_SERVICE_TYPE, EnumUserInfoName.DLV_ADDRESS_1.getCode(),
        		EnumUserInfoName.DLV_APARTMENT.getCode(), EnumUserInfoName.DLV_CITY.getCode(),EnumUserInfoName.DLV_STATE.getCode()
        		,REQ_PARAM_DELIVERY_STATUS, REQ_PARAM_REFERRAL_REGISTRATION, REQ_PARAM_LITE_SIGNUP }
        		, new String[] {REQ_PARAM_DELIVERY_STATUS});//gets,sets
        
        if(EnumServiceType.CORPORATE.getName().equals(zipcheck.getServiceType())){
        	addRequestValue(EnumUserInfoName.DLV_CORP_ZIPCODE.getCode(), zipcheck.getZipCode());	
        	addRequestValue(REQ_PARAM_CORP_SERVICE_TYPE, zipcheck.getServiceType());
        } else {
        	addRequestValue(EnumUserInfoName.DLV_ZIPCODE.getCode(), zipcheck.getZipCode());	
        	addRequestValue(REQ_PARAM_SERVICE_TYPE, zipcheck.getServiceType());
        	//addRequestValue(REQ_PARAM_AVAILABLE_SERVICE_TYPE, zipcheck.getAvailableServiceTypes());
        }
        addRequestValue(EnumUserInfoName.DLV_ADDRESS_1.getCode(), zipcheck.getAddress1());
        addRequestValue(EnumUserInfoName.DLV_APARTMENT.getCode(), zipcheck.getApartment());
        addRequestValue(EnumUserInfoName.DLV_CITY.getCode(), zipcheck.getCity());
        addRequestValue(EnumUserInfoName.DLV_STATE.getCode(), zipcheck.getState());
        
        ((SiteAccessControllerTag) getWrapTarget()).setAction(ACTION_CHECK_BY_ADDRESS);
        setMethodMode(true);
        ResultBundle resultBundle = new ResultBundle(executeTagLogic(), this);
        resultBundle.addExtraData(REQUESTED_SERVICE_TYPE_DLV_STATUS,  
        		((SiteAccessControllerTag) getWrapTarget()).getRequestedServiceTypeDlvStatus());
        
        resultBundle.addExtraData(AVAILABLE_SERVICE_TYPES,  
        		((SiteAccessControllerTag) getWrapTarget()).getAvailableServiceTypes());
        return resultBundle;

    }
    

	
	public ResultBundle registerSocial() throws FDException{
		addExpectedSessionValues(new String[]{}, new String[]{});
		addExpectedRequestValues(new String[]{}, new String[]{});
		 ((SiteAccessControllerTag) getWrapTarget()).setAction(ACTION_REGISTRATION);
	    setMethodMode(true);
	    ResultBundle resultBundle = new ResultBundle(executeTagLogic(), this);
		return resultBundle;
	}
	
        
}
