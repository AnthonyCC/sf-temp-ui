package com.freshdirect.mobileapi.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Category;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.freshdirect.backoffice.selfcredit.data.IssueSelfCreditRequest;
import com.freshdirect.backoffice.selfcredit.data.IssueSelfCreditResponse;
import com.freshdirect.backoffice.selfcredit.data.SelfCreditOrderHistoryData;
import com.freshdirect.backoffice.selfcredit.service.BackOfficeSelfCreditService;
import com.freshdirect.backoffice.selfcredit.service.SelfCreditOrderDetailsService;
import com.freshdirect.backoffice.selfcredit.service.SelfCreditOrderHistoryService;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.framework.template.TemplateException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.controller.data.response.SelfCreditOrderDetailsData;
import com.freshdirect.mobileapi.exception.JsonException;
import com.freshdirect.mobileapi.exception.NoSessionException;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.service.ServiceException;
import com.freshdirect.webapp.ajax.BaseJsonServlet.HttpErrorResponse;

public class SelfCreditController extends BaseController {

    private static Category LOGGER = LoggerFactory.getInstance(SelfCreditController.class);

    private static final String ACTION_GET_ORDER_HISTORY = "getSelfCreditOrderHistory";
    private static final String ACTION_GET_ORDER_DETAILS = "getSelfCreditOrderDetails";
    private static final String ACTION_POST_ISSUE_SELF_CREDIT = "postIssueSelfCredit";

    @Override
    protected ModelAndView processRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView model, String action, SessionUser user)
            throws JsonException, FDException, ServiceException, NoSessionException, IOException, TemplateException {
        Message responseMessage = null;

        if (ACTION_GET_ORDER_HISTORY.equals(action)) {
            responseMessage = getOrderHistory(user);
        } else if (ACTION_GET_ORDER_DETAILS.equals(action)) {
            String orderId = request.getParameter("orderId");
            responseMessage = getOrderDetails(request, user, orderId);
        } else if (ACTION_POST_ISSUE_SELF_CREDIT.equals(action)) {
            responseMessage = issueSelfCredit(request, response, user);
        }

        setResponseMessage(model, responseMessage, user);

        return model;
    }

    private Message issueSelfCredit(HttpServletRequest request, HttpServletResponse response, SessionUser user) throws JsonException {
        IssueSelfCreditRequest issueSelfCreditRequest = parseRequestObject(request, response, IssueSelfCreditRequest.class);
        IssueSelfCreditResponse issueSelfCreditResponse = BackOfficeSelfCreditService.defaultService()
                .postSelfCreditRequest(issueSelfCreditRequest,user.getFDSessionUser());
        
        Message message = new Message();
        if (issueSelfCreditResponse.isSuccess()) {
        	message.setSuccessMessage(issueSelfCreditResponse.getMessage());
		} else {
			message.setFailureMessage(issueSelfCreditResponse.getMessage());
		}
        return message;
    }

    private Message getOrderHistory(SessionUser user) throws FDResourceException {
        SelfCreditOrderHistoryData orders = SelfCreditOrderHistoryService.defaultService().collectSelfCreditOrders(user.getFDSessionUser());
        return new com.freshdirect.mobileapi.controller.data.response.SelfCreditOrderHistoryData(orders);
    }

    private Message getOrderDetails(HttpServletRequest request, SessionUser user, String orderId) {
        com.freshdirect.backoffice.selfcredit.data.SelfCreditOrderDetailsData selfCreditOrderDetailsData = null;
        Message response = new Message();
        try {
            selfCreditOrderDetailsData = SelfCreditOrderDetailsService.defaultService().collectOrderDetails(user.getFDSessionUser(), orderId);
        } catch (FDResourceException e) {
            LOGGER.error("Failed to load details of order: " + orderId, e);
            response.setFailureMessage("Failed to load details of order: " + orderId);
        } catch (JsonParseException e) {
            LOGGER.error("Failed to load details of order: " + orderId, e);
            response.setFailureMessage("Failed to load details of order: " + orderId);
        } catch (JsonMappingException e) {
            LOGGER.error("Failed to load details of order: " + orderId, e);
            response.setFailureMessage("Failed to load details of order: " + orderId);
        } catch (FDSkuNotFoundException e) {
            LOGGER.error("Failed to load details of order: " + orderId, e);
            response.setFailureMessage("Failed to load details of order: " + orderId);
        } catch (IOException e) {
            LOGGER.error("Failed to load details of order: " + orderId, e);
            response.setFailureMessage("Failed to load details of order: " + orderId);
        } catch (HttpErrorResponse e) {
            LOGGER.error("Failed to load details of order: " + orderId, e);
            response.setFailureMessage("Failed to load details of order: " + orderId);
        }
        response = new SelfCreditOrderDetailsData(selfCreditOrderDetailsData);
        return response;
    }
}
