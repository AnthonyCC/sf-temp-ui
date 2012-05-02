package com.freshdirect.mobileapi.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Category;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.delivery.EnumDeliveryStatus;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.controller.data.request.DeliveryAddressRequest;
import com.freshdirect.mobileapi.controller.data.request.DeliveryAddressSelection;
import com.freshdirect.mobileapi.controller.data.request.DeliverySlotReservation;
import com.freshdirect.mobileapi.controller.data.request.Login;
import com.freshdirect.mobileapi.controller.data.request.PaymentMethodRequest;
import com.freshdirect.mobileapi.controller.data.request.PaymentMethodSelection;
import com.freshdirect.mobileapi.controller.data.request.ZipCheck;
import com.freshdirect.mobileapi.controller.data.response.DeliveryAddresses;
import com.freshdirect.mobileapi.controller.data.response.LoggedIn;
import com.freshdirect.mobileapi.controller.data.response.OrderReceipt;
import com.freshdirect.mobileapi.controller.data.response.PaymentMethods;
import com.freshdirect.mobileapi.controller.data.response.Visitor;
import com.freshdirect.mobileapi.exception.JsonException;
import com.freshdirect.mobileapi.exception.NoSessionException;
import com.freshdirect.mobileapi.model.Checkout;
import com.freshdirect.mobileapi.model.DeliveryAddress;
import com.freshdirect.mobileapi.model.DeliveryTimeslots;
import com.freshdirect.mobileapi.model.Depot;
import com.freshdirect.mobileapi.model.OrderHistory;
import com.freshdirect.mobileapi.model.OrderInfo;
import com.freshdirect.mobileapi.model.PaymentMethod;
import com.freshdirect.mobileapi.model.ResultBundle;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.model.ShipToAddress;
import com.freshdirect.mobileapi.model.User;
import com.freshdirect.mobileapi.model.DeliveryAddress.DeliveryAddressType;
import com.freshdirect.mobileapi.model.DeliveryTimeslots.TimeSlotCalculationResult;
import com.freshdirect.mobileapi.model.tagwrapper.RequestParamName;
import com.freshdirect.mobileapi.model.tagwrapper.SiteAccessControllerTagWrapper;
import com.freshdirect.mobileapi.service.ServiceException;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class SiteAccessController extends BaseController {

    private static Category LOGGER = LoggerFactory.getInstance(SiteAccessController.class);

    public static final String ACTION_CHECK_BY_ZIP = "checkbyzipcode";

    public static final String ACTION_CHECK_BY_ADDRESS = "checkbyaddress";

    
    protected boolean validateUser() {
        return false;
    }
    
    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected ModelAndView processRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView model, String action,
            SessionUser user) throws FDException, ServiceException, NoSessionException, JsonException {
    	if (ACTION_CHECK_BY_ZIP.equals(action)) {
    		ZipCheck requestMessage = parseRequestObject(request, response, ZipCheck.class);
            model = checkByZipcode(model, requestMessage, request, response);
        }else if (ACTION_CHECK_BY_ADDRESS.equals(action)) {
        	ZipCheck requestMessage = parseRequestObject(request, response, ZipCheck.class);
            model = checkByAddress(model, requestMessage, request, response);
        }
        return model;
    }


    /**
     * @param model
     * @param user
     * @param request
     * @return
     * @throws FDException
     * @throws JsonException
     */
    private ModelAndView checkByZipcode(ModelAndView model, ZipCheck requestMessage, HttpServletRequest request, HttpServletResponse response) 
    			throws FDException, NoSessionException, JsonException  {
        SiteAccessControllerTagWrapper tagWrapper = new SiteAccessControllerTagWrapper(null);
        ResultBundle resultBundle = tagWrapper.checkByZipcode(requestMessage);
        ActionResult result = resultBundle.getActionResult();

        propogateSetSessionValues(request.getSession(), resultBundle);

        Message responseMessage = null;
        SessionUser user = null;
        if (result.isSuccess()) {
       		request.getSession().setAttribute(SessionName.APPLICATION, EnumTransactionSource.IPHONE_WEBSITE.getCode());
            user = getUserFromSession(request, response);
            user.setUserPricingContext();
            user.setEligibleForDDPP();
        	//Create a new Visitor object.
            responseMessage = formatVisitorMessage(user, requestMessage, resultBundle);
            resetMobileSessionData(request);
            
        } else {
            responseMessage = getErrorMessage(result, request);
        }
        responseMessage.addWarningMessages(result.getWarnings());
        setResponseMessage(model, responseMessage, user);
        return model;
    }

    private ModelAndView checkByAddress(ModelAndView model, ZipCheck requestMessage, HttpServletRequest request, HttpServletResponse response) 
			throws FDException, NoSessionException, JsonException  {
		SiteAccessControllerTagWrapper tagWrapper = new SiteAccessControllerTagWrapper(null);
		ResultBundle resultBundle = tagWrapper.checkByAddress(requestMessage);
		ActionResult result = resultBundle.getActionResult();
		
		propogateSetSessionValues(request.getSession(), resultBundle);
		
		Message responseMessage = null;
		SessionUser user = null;
		if (result.isSuccess()) {
			request.getSession().setAttribute(SessionName.APPLICATION, EnumTransactionSource.IPHONE_WEBSITE.getCode());
		user = getUserFromSession(request, response);
		user.setUserPricingContext();
		user.setEligibleForDDPP();
		//Create a new Visitor object.
		responseMessage = formatVisitorMessage(user, requestMessage, resultBundle);
		resetMobileSessionData(request);
		
		} else {
		responseMessage = getErrorMessage(result, request);
		}
		responseMessage.addWarningMessages(result.getWarnings());
		setResponseMessage(model, responseMessage, user);
		return model;
    }
    
    private Message formatVisitorMessage(SessionUser user, ZipCheck requestMessage, ResultBundle resultBundle) throws FDException {
        Message responseMessage = null;

        responseMessage = new Visitor();
        ((Visitor) responseMessage).setZipCode(user.getZipCode());
        ((Visitor) responseMessage).setServiceType(user.getServiceType());
        EnumDeliveryStatus dlvStatus = (EnumDeliveryStatus)resultBundle.getExtraData(
        					SiteAccessControllerTagWrapper.REQUESTED_SERVICE_TYPE_DLV_STATUS);
        ((Visitor) responseMessage).setDeliveryStatus(dlvStatus != null ? dlvStatus.getName() : "");
        //AddressModel address = user.getAddress();
        if(requestMessage.getAddress1() != null){
        	((Visitor) responseMessage).setAddress1(requestMessage.getAddress1());
        	((Visitor) responseMessage).setApartment(requestMessage.getApartment());
        	((Visitor) responseMessage).setCity(requestMessage.getCity());
        	((Visitor) responseMessage).setState(requestMessage.getState());
        	
        }
        return responseMessage;
    }
}
