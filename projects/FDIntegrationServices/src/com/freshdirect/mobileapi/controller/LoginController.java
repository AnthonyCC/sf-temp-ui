package com.freshdirect.mobileapi.controller;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Category;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.common.pricing.PricingException;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDAuthenticationException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.PasswordNotExpiredException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.controller.data.request.Login;
import com.freshdirect.mobileapi.controller.data.response.LoggedIn;
import com.freshdirect.mobileapi.controller.data.response.Timeslot;
import com.freshdirect.mobileapi.exception.JsonException;
import com.freshdirect.mobileapi.exception.NoSessionException;
import com.freshdirect.mobileapi.model.MessageCodes;
import com.freshdirect.mobileapi.model.OrderHistory;
import com.freshdirect.mobileapi.model.OrderInfo;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.model.User;
import com.freshdirect.mobileapi.service.ServiceException;
import com.freshdirect.mobileapi.util.MobileApiProperties;
import com.freshdirect.webapp.taglib.fdstore.CookieMonster;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;
import com.freshdirect.webapp.taglib.fdstore.UserUtil;

public class LoginController extends BaseController {

    public static final String ACTION_LOGIN = "login";

    public static final String ACTION_LOGOUT = "logout";
    
    public static final String ACTION_FORGOTPASSWORD = "forgotpassword";

    public static final String ACTION_PING = "ping";
    
    private final static String MSG_INVALID_EMAIL =
					"Invalid or missing email address. If you need assistance please call us at 1-866-283-7374.";
	private final static String MSG_EMAIL_NOT_EXPIRED = "An email was already sent. Please try again later.";

    private static Category LOGGER = LoggerFactory.getInstance(LoginController.class);

    protected boolean validateUser() {
        return false;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.mobileapi.controller.BaseController#processRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.web.servlet.ModelAndView, java.lang.String, com.freshdirect.mobileapi.model.SessionUser)
     */
    protected ModelAndView processRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView model, String action,
            SessionUser user) throws FDException, ServiceException, NoSessionException, JsonException {

        if (ACTION_LOGIN.equals(action)) {
            Login requestMessage = parseRequestObject(request, response, Login.class);
            try {
                //Check to see if user session exists
                getUserFromSession(request, response);
                logout(model, user, request, response);
            } catch (NoSessionException e) {
                //Do nothing
            }
            model = login(model, requestMessage, request, response);
        } else if (ACTION_PING.equals(action)) {
            model = ping(model, request, response);
        } else if (ACTION_LOGOUT.equals(action)) {
            model = logout(model, user, request, response);
        }  else if (ACTION_FORGOTPASSWORD.equals(action)) {
        	try {
        		Login requestMessage = parseRequestObject(request, response, Login.class);
				LOGGER.debug("Email is going to: " + requestMessage.getUsername());
				FDCustomerManager.sendPasswordEmail(requestMessage.getUsername(), false);
				Message responseMessage = Message.createSuccessMessage("Password sent successfully.");
	            setResponseMessage(model, responseMessage, user);

			} catch (FDResourceException ex) {
				Message responseMessage = getErrorMessage(ERR_FORGOTPASSWORD_INVALID_EMAIL, MSG_INVALID_EMAIL);				
				setResponseMessage(model, responseMessage, user);
			} catch (PasswordNotExpiredException pe) {
				Message responseMessage = getErrorMessage(ERR_FORGOTPASSWORD_EMAIL_NOT_EXPIRED, MSG_EMAIL_NOT_EXPIRED);
				setResponseMessage(model, responseMessage, user);
			}
        	
        }
        return model;
    }

    /**
     * @param request
     * @return
     * @throws JsonException 
     * @throws IOException 
     * @throws JsonMappingException 
     * @throws JsonGenerationException 
     */
    private ModelAndView logout(ModelAndView model, SessionUser user, HttpServletRequest request, HttpServletResponse response)
            throws JsonException {
    	
    	removeUserInSession(user, request, response);

        Message responseMessage = Message.createSuccessMessage("User logged out successfully.");
        setResponseMessage(model, responseMessage, user);
        return model;
    }

    private ModelAndView ping(ModelAndView model, HttpServletRequest request, HttpServletResponse response) throws NoSessionException,
            FDException, JsonException {
        SessionUser user = getUserFromSession(request, response);
        Message responseMessage = formatLoginMessage(user);
        setResponseMessage(model, responseMessage, user);
        return model;

    }

    /**
     * @param requestMessage
     * @param request
     * @param response
     * @return
     * @throws FDException
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     * @throws PricingException
     * @throws NoSessionException 
     * @throws JsonException 
     */
    private ModelAndView login(ModelAndView model, Login requestMessage, HttpServletRequest request, HttpServletResponse response)
            throws FDException, NoSessionException, JsonException {
        String username = requestMessage.getUsername();
        String password = requestMessage.getPassword();
        String source = requestMessage.getSource();
        Message responseMessage = null;
        SessionUser user = null;
        try {

            //Log in user and store in session
            createUserSession(User.login(username, password), source, request, response);
            user = getUserFromSession(request, response);
            user.setUserPricingContext();
            user.setEligibleForDDPP();
            responseMessage = formatLoginMessage(user);
            resetMobileSessionData(request);

        } catch (FDAuthenticationException ex) {
        	if("Account disabled".equals(ex.getMessage())) {
        		responseMessage = getErrorMessage(ERR_AUTHENTICATION, MessageFormat.format(SystemMessageList.MSG_DEACTIVATED, 
	            		new Object[] { UserUtil.getCustomerServiceContact(request)}));
        	} else {
        		responseMessage = getErrorMessage(ERR_AUTHENTICATION, MessageCodes.MSG_AUTHENTICATION_FAILED);
        	}
        }
        setResponseMessage(model, responseMessage, user);
        return model;
    }

    private Message formatLoginMessage(SessionUser user) throws FDException {
        Message responseMessage = null;

        OrderHistory history = user.getOrderHistory();
        String cutoffMessage = user.getCutoffInfo();

        OrderInfo closestPendingOrder = history.getClosestPendingOrderInfo(new Date());
        List<OrderInfo> orderHistoryInfo = new ArrayList<OrderInfo>();
        if (closestPendingOrder != null) {
            orderHistoryInfo.add(closestPendingOrder);
        }

        responseMessage = new LoggedIn();
        ((LoggedIn) responseMessage).setChefTable(user.isChefsTable());
        ((LoggedIn) responseMessage).setCustomerServicePhoneNumber(user.getCustomerServiceContact());
        if (user.getReservationTimeslot() != null) {
            ((LoggedIn) responseMessage).setReservationTimeslot(new Timeslot(user.getReservationTimeslot()));
        }
        ((LoggedIn) responseMessage).setFirstName(user.getFirstName());
        ((LoggedIn) responseMessage).setUsername(user.getUsername());
        ((LoggedIn) responseMessage).setOrders(com.freshdirect.mobileapi.controller.data.response.OrderHistory.Order
                .createOrderList(orderHistoryInfo));
        ((LoggedIn) responseMessage).setSuccessMessage("User has been logged in successfully.");
        ((LoggedIn) responseMessage).setItemsInCartCount(user.getItemsInCartCount());
        ((LoggedIn) responseMessage).setOrderCount(user.getOrderHistory().getValidOrderCount());
        ((LoggedIn) responseMessage).setFdUserId(user.getPrimaryKey());
        
        if (cutoffMessage != null) {
            responseMessage.addNoticeMessage(MessageCodes.NOTICE_DELIVERY_CUTOFF, cutoffMessage);
        }

      //With Mobile App having given ability to add/remove payment method this is removed
       /*if ((user.getPaymentMethods() == null) || (user.getPaymentMethods().size() == 0)) {
            responseMessage.addWarningMessage(ERR_NO_PAYMENT_METHOD, ERR_NO_PAYMENT_METHOD_MSG);
        }*/
        ((LoggedIn) responseMessage).setBrowseEnabled(MobileApiProperties.isBrowseEnabled());
        return responseMessage;
    }
}
