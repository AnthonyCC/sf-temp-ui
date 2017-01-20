package com.freshdirect.mobileapi.controller;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.logistics.delivery.model.EnumDeliveryStatus;
import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.controller.data.request.ZipCheck;
import com.freshdirect.mobileapi.controller.data.response.Visitor;
import com.freshdirect.mobileapi.exception.JsonException;
import com.freshdirect.mobileapi.exception.NoSessionException;
import com.freshdirect.mobileapi.model.ResultBundle;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.model.tagwrapper.SiteAccessControllerTagWrapper;
import com.freshdirect.mobileapi.service.ServiceException;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class SiteAccessController extends BaseController {

    private static final String ACTION_CHECK_BY_ZIP = "checkbyzipcode";
    private static final String ACTION_CHECK_OAS_ALERT = "globalalerts";
    private static final String ACTION_CHECK_BY_ADDRESS = "checkbyaddress";
    private static final String ACTION_CHECK_BY_ADDRESS_EX = "checkbyaddressEX";

    protected boolean validateUser() {
        return false;
    }
    
    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected ModelAndView processRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView model, String action,
            SessionUser user) throws FDException, ServiceException, NoSessionException, JsonException {
        Message responseMessage = null;
        if (ACTION_CHECK_BY_ZIP.equals(action)) {
    		ZipCheck requestMessage = parseRequestObject(request, response, ZipCheck.class);
    		responseMessage = checkByZipcode(requestMessage, request, response, user);
        } else if (ACTION_CHECK_BY_ADDRESS.equals(action)) {
        	ZipCheck requestMessage = parseRequestObject(request, response, ZipCheck.class);
        	responseMessage = checkByAddress(requestMessage, request, response);
        } else if (ACTION_CHECK_BY_ADDRESS_EX.equals(action)) {
        	ZipCheck requestMessage = parseRequestObject(request, response, ZipCheck.class);
        	responseMessage = checkByAddressEX(requestMessage, request, response, user);
        } else if (ACTION_CHECK_OAS_ALERT.equals(action)) {
            responseMessage = checkOasAlert(request, response);
        }
    	setResponseMessage(model, responseMessage, user);
        return model;
    }

    private Message checkByZipcode(ZipCheck requestMessage, HttpServletRequest request, HttpServletResponse response, SessionUser sessionUser)
            throws FDException, NoSessionException {
        FDUserI user = (sessionUser == null) ? null : sessionUser.getFDSessionUser();
        SiteAccessControllerTagWrapper tagWrapper = new SiteAccessControllerTagWrapper(user);
        ResultBundle resultBundle = tagWrapper.checkByZipcode(requestMessage);
        ActionResult result = resultBundle.getActionResult();

        propogateSetSessionValues(request.getSession(), resultBundle);

        Message responseMessage = null;
        if (isCheckLoginStatusEnable(request)) {
            if (result.isSuccess()) {
                AddressModel address = new AddressModel();
                address.setZipCode(requestMessage.getZipCode());
                address.setCustomerAnonymousAddress(true);
                sessionUser.setAddress(address);
                EnumServiceType serviceType = EnumServiceType.getEnum(requestMessage.getServiceType());
                Visitor messageResponse = formatVisitorMessage(requestMessage.getZipCode(), serviceType, requestMessage, resultBundle);
                messageResponse.setLogin(createLoginResponseMessage(sessionUser));
                messageResponse.setConfiguration(getConfiguration(sessionUser));
                messageResponse.setStatus(Message.STATUS_SUCCESS);
                responseMessage = messageResponse;
            } else {
                EnumServiceType serviceType = EnumServiceType.getEnum(requestMessage.getServiceType());
                Visitor messageResponse = formatVisitorMessage(requestMessage.getZipCode(), serviceType, requestMessage, resultBundle);
                messageResponse.setStatus(Message.STATUS_FAILED);
                messageResponse.addErrorMessages(result.getErrors(), sessionUser);
                responseMessage = messageResponse;
            }
        } else {
            if (result.isSuccess()) {
                request.getSession().setAttribute(SessionName.APPLICATION, getTransactionSourceCode(request, null));
                sessionUser = getUserFromSession(request, response);
                sessionUser.setUserContext();
                sessionUser.setEligibleForDDPP();
                // Create a new Visitor object.
                responseMessage = formatVisitorMessage(sessionUser, requestMessage, resultBundle);
                resetMobileSessionData(request);
            } else {
                responseMessage = getErrorMessage(result, request);
            }
        }
        responseMessage.addWarningMessages(result.getWarnings());

        return responseMessage;
    }

    private Message checkOasAlert(HttpServletRequest request, HttpServletResponse response) {
        Message responseMessage = new Message();
        return responseMessage;
    }

    private Message checkByAddress(ZipCheck requestMessage, HttpServletRequest request, HttpServletResponse response) 
			throws FDException, NoSessionException {
		SiteAccessControllerTagWrapper tagWrapper = new SiteAccessControllerTagWrapper(null);
		ResultBundle resultBundle = tagWrapper.checkByAddress(requestMessage);
		ActionResult result = resultBundle.getActionResult();
		
		propogateSetSessionValues(request.getSession(), resultBundle);
		
		Message responseMessage = null;
		SessionUser user = null;
		if (result.isSuccess()) {
		    request.getSession().setAttribute(SessionName.APPLICATION, getTransactionSourceCode(request, null));
		    user = getUserFromSession(request, response);
		    user.setUserContext();
		    user.setEligibleForDDPP();
		//    Create a new Visitor object.
		    responseMessage = formatVisitorMessage(user, requestMessage, resultBundle);
		    resetMobileSessionData(request);
		} else {
		    responseMessage = getErrorMessage(result, request);
		}
		responseMessage.addWarningMessages(result.getWarnings());
		return responseMessage;
    }

    private Message checkByAddressEX(ZipCheck requestMessage, HttpServletRequest request, HttpServletResponse response, SessionUser user) 
			throws FDException, NoSessionException {
		SiteAccessControllerTagWrapper tagWrapper = new SiteAccessControllerTagWrapper(user.getFDSessionUser());
		ResultBundle resultBundle = tagWrapper.checkByAddress(requestMessage, ACTION_CHECK_BY_ADDRESS_EX);
		ActionResult result = resultBundle.getActionResult();
		
		propogateSetSessionValues(request.getSession(), resultBundle);
		
		Message responseMessage = null;
		if (result.isSuccess()) {
			request.getSession().setAttribute(SessionName.APPLICATION, EnumTransactionSource.FDX_IPHONE.getCode());
		user = getUserFromSession(request, response);
		user.setUserContext();
		user.setEligibleForDDPP();
		//Create a new Visitor object.
		responseMessage = formatVisitorMessage(user, requestMessage, resultBundle);
		resetMobileSessionData(request);		
		} else {		
		responseMessage = formatVisitorMessageNone(result);	
		}		
		responseMessage.addWarningMessages(result.getWarnings());
		return responseMessage;
    }
    
    private Visitor formatVisitorMessage(SessionUser user, ZipCheck requestMessage, ResultBundle resultBundle) {
        return formatVisitorMessage(user.getZipCode(), user.getServiceType(), requestMessage, resultBundle);
    }

    private Visitor formatVisitorMessage(String zipCode, EnumServiceType serviceType, ZipCheck requestMessage, ResultBundle resultBundle) {
        Visitor responseMessage = new Visitor();
        responseMessage.setZipCode(zipCode);
        responseMessage.setServiceType(serviceType);
        EnumDeliveryStatus dlvStatus = (EnumDeliveryStatus)resultBundle.getExtraData(
        					SiteAccessControllerTagWrapper.REQUESTED_SERVICE_TYPE_DLV_STATUS);
        responseMessage.setDeliveryStatus(dlvStatus != null ? dlvStatus.getName() : "");
                
        responseMessage.setAvailableServiceTypes((Set)resultBundle.getExtraData(
				SiteAccessControllerTagWrapper.AVAILABLE_SERVICE_TYPES));

        if(requestMessage.getAddress1() != null){
        	responseMessage.setAddress1(requestMessage.getAddress1());
        	responseMessage.setApartment(requestMessage.getApartment());
        	responseMessage.setCity(requestMessage.getCity());
        	responseMessage.setState(requestMessage.getState());
        }
        return responseMessage;
    }

    private Message formatVisitorMessageNone(ActionResult result) {
        Visitor responseMessage = new Visitor();
        responseMessage.setStatus(Message.STATUS_FAILED);
        responseMessage.addErrorMessages(result.getErrors(), null);
        responseMessage.setServiceType(EnumServiceType.NONE);
        return responseMessage;
    }
}
