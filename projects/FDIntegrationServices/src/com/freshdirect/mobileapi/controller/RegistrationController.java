package com.freshdirect.mobileapi.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Category;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.controller.data.request.DeliveryAddressRequest;
import com.freshdirect.mobileapi.controller.data.request.RegisterMessage;
import com.freshdirect.mobileapi.controller.data.response.LoggedIn;
import com.freshdirect.mobileapi.controller.data.response.OrderHistory;
import com.freshdirect.mobileapi.controller.data.response.Timeslot;
import com.freshdirect.mobileapi.exception.JsonException;
import com.freshdirect.mobileapi.exception.NoSessionException;
import com.freshdirect.mobileapi.model.ResultBundle;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.model.tagwrapper.RegistrationControllerTagWrapper;
import com.freshdirect.mobileapi.service.ServiceException;
import com.freshdirect.mobileapi.util.MobileApiProperties;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class RegistrationController extends BaseController {

	private static Category LOGGER = LoggerFactory
			.getInstance(SiteAccessController.class);

	public static final String ACTION_REGISTER_FROM_IPHONE = "register";
	
    private final static String ACTION_ADD_DELIVERY_ADDRESS = "adddeliveryaddress";

    private final static String ACTION_EDIT_DELIVERY_ADDRESS = "editdeliveryaddress";


	protected boolean validateUser() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet
	 * .http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ModelAndView processRequest(HttpServletRequest request,
			HttpServletResponse response, ModelAndView model, String action,
			SessionUser user) throws FDException, ServiceException,
			NoSessionException, JsonException {
		if (ACTION_REGISTER_FROM_IPHONE.equals(action)) {
			RegisterMessage requestMessage = parseRequestObject(request,
					response, RegisterMessage.class);
			model = register(model, requestMessage, request, response,user);
		} else if (ACTION_ADD_DELIVERY_ADDRESS.equals(action)) {
        	DeliveryAddressRequest requestMessage = parseRequestObject(request, response, DeliveryAddressRequest.class);
            model = addDeliveryAddress(model, user, requestMessage, request);
        }else if (ACTION_EDIT_DELIVERY_ADDRESS.equals(action)) {
        	DeliveryAddressRequest requestMessage = parseRequestObject(request, response, DeliveryAddressRequest.class);
            model = editDeliveryAddress(model, user, requestMessage, request);
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
	private ModelAndView register(ModelAndView model,
			RegisterMessage requestMessage, HttpServletRequest request,
			HttpServletResponse response, SessionUser user) throws FDException,
			NoSessionException, JsonException {
		if(user == null)
			throw new NoSessionException("No session");
		RegistrationControllerTagWrapper tagWrapper = new RegistrationControllerTagWrapper(user.getFDSessionUser());
		ResultBundle resultBundle = tagWrapper.register(requestMessage);
		ActionResult result = resultBundle.getActionResult();

		propogateSetSessionValues(request.getSession(), resultBundle);

		Message responseMessage = null;
		if (result.isSuccess()) {
			request.getSession().setAttribute(SessionName.APPLICATION,
					EnumTransactionSource.IPHONE_WEBSITE.getCode());
			user = getUserFromSession(request, response);
			user.setUserPricingContext();
			user.setEligibleForDDPP();
			// Create a new Visitor object.
			responseMessage = formatLoginMessage(user);
			resetMobileSessionData(request);

		} else {
			responseMessage = getErrorMessage(result, request);
		}
		responseMessage.addWarningMessages(result.getWarnings());
		setResponseMessage(model, responseMessage, user);
		return model;
	}

	private Message formatLoginMessage(SessionUser user) throws FDException {
		Message responseMessage = null;

		responseMessage = new LoggedIn();
		((LoggedIn) responseMessage).setChefTable(user.isChefsTable());
		((LoggedIn) responseMessage).setCustomerServicePhoneNumber(user
				.getCustomerServiceContact());
		if (user.getReservationTimeslot() != null) {
			((LoggedIn) responseMessage).setReservationTimeslot(new Timeslot(
					user.getReservationTimeslot()));
		}
		((LoggedIn) responseMessage).setFirstName(user.getFirstName());
        ((LoggedIn) responseMessage).setLastName(user.getLastName());
		((LoggedIn) responseMessage).setUsername(user.getUsername());
		((LoggedIn) responseMessage)
				.setSuccessMessage("User has been logged in successfully.");
		((LoggedIn) responseMessage).setItemsInCartCount(user
				.getItemsInCartCount());
		((LoggedIn) responseMessage).setOrderCount(user.getOrderHistory().getValidOrderCount());
        ((LoggedIn) responseMessage).setOrders(java.util.Collections.<OrderHistory.Order>emptyList());
		((LoggedIn) responseMessage).setFdUserId(user.getPrimaryKey());

		//With Mobile App having given ability to add/remove payment method this is removed
		/* if ((user.getPaymentMethods() == null)	|| (user.getPaymentMethods().size() == 0)) {
			responseMessage.addWarningMessage(ERR_NO_PAYMENT_METHOD, ERR_NO_PAYMENT_METHOD_MSG);
		}*/
		((LoggedIn) responseMessage).setBrowseEnabled(MobileApiProperties.isBrowseEnabled());
		
		//Added during Mobile Coremetrics Implementation
		((LoggedIn) responseMessage).setSelectedServiceType(user.getSelectedServiceType() != null ? user.getSelectedServiceType().toString() : "");
		((LoggedIn) responseMessage).setCohort(user.getCohort());
		((LoggedIn) responseMessage).setTotalOrderCount(user.getTotalOrderCount());
		
		return responseMessage;
	}
	
    private ModelAndView addDeliveryAddress(ModelAndView model, SessionUser user, DeliveryAddressRequest reqestMessage,
            HttpServletRequest request) throws FDException, JsonException {
    	//APPDEV-4315- Intermittent: Cannot Create Address -Start
    	try{
    	RegistrationControllerTagWrapper tagWrapper = new RegistrationControllerTagWrapper(user.getFDSessionUser());
        ResultBundle resultBundle = tagWrapper.addDeliveryAddress(reqestMessage);
        ActionResult result = resultBundle.getActionResult();

        propogateSetSessionValues(request.getSession(), resultBundle);

        Message responseMessage = null;
        if (result.isSuccess()) {
            responseMessage = Message.createSuccessMessage("Delivery Address added successfully.");
        } else {
            responseMessage = getErrorMessage(result, request);
        }
        responseMessage.addWarningMessages(result.getWarnings());
        setResponseMessage(model, responseMessage, user);
    	}
		catch (Exception e) {
    		Message responseMessage = getErrorMessage(ERR_SESSION_EXPIRED, "Session does not exist in the server.");
    		setResponseMessage(model, responseMessage, user);
    	}
        //APPDEV-4315- Intermittent: Cannot Create Address -End
        return model;
    }

    private ModelAndView editDeliveryAddress(ModelAndView model, SessionUser user, DeliveryAddressRequest reqestMessage,
            HttpServletRequest request) throws FDException, JsonException {
    	//APPDEV-4315- Intermittent: Cannot Create Address -Start
    	try{
    	RegistrationControllerTagWrapper tagWrapper = new RegistrationControllerTagWrapper(user.getFDSessionUser());
        ResultBundle resultBundle = tagWrapper.editDeliveryAddress(reqestMessage);
        ActionResult result = resultBundle.getActionResult();

        propogateSetSessionValues(request.getSession(), resultBundle);

        Message responseMessage = null;
        if (result.isSuccess()) {
            responseMessage = Message.createSuccessMessage("Delivery Address updated successfully.");
        } else {
            responseMessage = getErrorMessage(result, request);
        }
        responseMessage.addWarningMessages(result.getWarnings());
        setResponseMessage(model, responseMessage, user);
    	
    	}
    	catch(Exception e){
    		Message responseMessage = getErrorMessage(ERR_SESSION_EXPIRED, "Session does not exist in the server.");
    		setResponseMessage(model, responseMessage, user);  		
    	}
        //APPDEV-4315- Intermittent: Cannot Create Address -End
        return model;
    }
	
}
