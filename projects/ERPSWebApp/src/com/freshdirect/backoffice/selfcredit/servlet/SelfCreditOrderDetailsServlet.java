package com.freshdirect.backoffice.selfcredit.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.freshdirect.backoffice.selfcredit.data.SelfCreditOrderDetailsData;
import com.freshdirect.backoffice.selfcredit.data.SelfCreditOrderItemData;
import com.freshdirect.backoffice.selfcredit.service.SelfCreditOrderDetailsService;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.ajax.BaseJsonServlet;

public class SelfCreditOrderDetailsServlet extends BaseJsonServlet {

    private static final Logger LOGGER = LoggerFactory.getInstance(SelfCreditOrderDetailsServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {

        String orderId = request.getParameter("orderId");

        SelfCreditOrderDetailsData orderDetails = new SelfCreditOrderDetailsData();
        try {
            orderDetails = SelfCreditOrderDetailsService.defaultService().collectOrderDetails(user, orderId);
        } catch (FDResourceException e) {
            returnHttpError(500, "Failed to load order details: " + orderId, e);
        } catch (JsonParseException e) {
            returnHttpError(500, "Failed to load order details: " + orderId, e);
        } catch (JsonMappingException e) {
            returnHttpError(500, "Failed to load order details: " + orderId, e);
        } catch (IOException e) {
            returnHttpError(500, "Failed to load order details: " + orderId, e);
        } catch (FDSkuNotFoundException e) {
			returnHttpError(500, "Failed to load order details: " + orderId, e);
		}

        Map<String, Object> responseData = new HashMap<String, Object>();
        responseData.put("orderdetails", orderDetails);
        writeResponseData(response, responseData);
    }

    @Override
    protected boolean synchronizeOnUser() {
        return false;
    }

    @Override
    protected int getRequiredUserLevel() {
        return FDUserI.SIGNED_IN;
    }

}
