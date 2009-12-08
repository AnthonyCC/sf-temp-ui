package com.freshdirect.mobileapi.controller;

import java.io.IOException;
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
import com.freshdirect.fdstore.customer.FDAuthenticationException;
import com.freshdirect.framework.util.log.LoggerFactory;
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
import com.freshdirect.webapp.taglib.fdstore.CookieMonster;

public class LoginController extends BaseController {

    public static final String ACTION_LOGIN = "login";

    public static final String ACTION_LOGOUT = "logout";

    public static final String ACTION_PING = "ping";

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
            model = login(model, requestMessage, request, response);
        } else if (ACTION_PING.equals(action)) {
            model = ping(model, request, response);
        } else if (ACTION_LOGOUT.equals(action)) {
            model = logout(model, user, request, response);
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
        user.touch();
        HttpSession session = request.getSession();

        // clear session
        Enumeration e = session.getAttributeNames();
        while (e.hasMoreElements()) {
            String name = (String) e.nextElement();
            session.removeAttribute(name);
        }
        // end session
        session.invalidate();
        // remove cookie
        CookieMonster.clearCookie(response);
        resetMobileSessionData(request);

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
        Message responseMessage = null;
        SessionUser user = null;
        try {

            //Log in user and store in session
            createUserSession(User.login(username, password), request, response);
            user = getUserFromSession(request, response);
            responseMessage = formatLoginMessage(user);
            resetMobileSessionData(request);

        } catch (FDAuthenticationException ex) {
            responseMessage = getErrorMessage(ERR_AUTHENTICATION, "Invalid username and/or password");
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

        if (cutoffMessage != null) {
            responseMessage.addNoticeMessage(MessageCodes.NOTICE_DELIVERY_CUTOFF, cutoffMessage);
        }

        //Check payment methods
        if ((user.getPaymentMethods() == null) || (user.getPaymentMethods().size() == 0)) {
            responseMessage.addWarningMessage(ERR_NO_PAYMENT_METHOD, ERR_NO_PAYMENT_METHOD_MSG);
        }
        return responseMessage;
    }
}
