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
import com.freshdirect.mobileapi.controller.data.request.RegisterMessage;
import com.freshdirect.mobileapi.controller.data.request.ZipCheck;
import com.freshdirect.mobileapi.controller.data.response.DeliveryAddresses;
import com.freshdirect.mobileapi.controller.data.response.LoggedIn;
import com.freshdirect.mobileapi.controller.data.response.OrderReceipt;
import com.freshdirect.mobileapi.controller.data.response.PaymentMethods;
import com.freshdirect.mobileapi.controller.data.response.Timeslot;
import com.freshdirect.mobileapi.controller.data.response.Visitor;
import com.freshdirect.mobileapi.exception.JsonException;
import com.freshdirect.mobileapi.exception.NoSessionException;
import com.freshdirect.mobileapi.model.Checkout;
import com.freshdirect.mobileapi.model.DeliveryAddress;
import com.freshdirect.mobileapi.model.DeliveryTimeslots;
import com.freshdirect.mobileapi.model.Depot;
import com.freshdirect.mobileapi.model.MessageCodes;
import com.freshdirect.mobileapi.model.OrderHistory;
import com.freshdirect.mobileapi.model.OrderInfo;
import com.freshdirect.mobileapi.model.PaymentMethod;
import com.freshdirect.mobileapi.model.ResultBundle;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.model.ShipToAddress;
import com.freshdirect.mobileapi.model.User;
import com.freshdirect.mobileapi.model.DeliveryAddress.DeliveryAddressType;
import com.freshdirect.mobileapi.model.DeliveryTimeslots.TimeSlotCalculationResult;
import com.freshdirect.mobileapi.model.tagwrapper.RegistrationControllerTagWrapper;
import com.freshdirect.mobileapi.model.tagwrapper.RequestParamName;
import com.freshdirect.mobileapi.model.tagwrapper.SiteAccessControllerTagWrapper;
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
		((LoggedIn) responseMessage).setUsername(user.getUsername());
		((LoggedIn) responseMessage)
				.setSuccessMessage("User has been logged in successfully.");
		((LoggedIn) responseMessage).setItemsInCartCount(user
				.getItemsInCartCount());

		// Check payment methods
		if ((user.getPaymentMethods() == null)
				|| (user.getPaymentMethods().size() == 0)) {
			responseMessage.addWarningMessage(ERR_NO_PAYMENT_METHOD,
					ERR_NO_PAYMENT_METHOD_MSG);
		}
		((LoggedIn) responseMessage).setBrowseEnabled(MobileApiProperties
				.isBrowseEnabled());
		return responseMessage;
	}
	
    private ModelAndView addDeliveryAddress(ModelAndView model, SessionUser user, DeliveryAddressRequest reqestMessage,
            HttpServletRequest request) throws FDException, JsonException {
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
        return model;
    }

    private ModelAndView editDeliveryAddress(ModelAndView model, SessionUser user, DeliveryAddressRequest reqestMessage,
            HttpServletRequest request) throws FDException, JsonException {
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
        return model;
    }
	
}
