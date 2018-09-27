package com.freshdirect.backoffice.selfcredit.servlet;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.freshdirect.backoffice.selfcredit.data.SelfCreditOrderHistoryData;
import com.freshdirect.backoffice.selfcredit.service.SelfCreditOrderHistoryService;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.ajax.BaseJsonServlet;

public class SelfCreditOrderHistoryServlet extends BaseJsonServlet {

    private static final Logger LOGGER = LoggerFactory.getInstance(SelfCreditOrderHistoryServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {

        SelfCreditOrderHistoryData orders = new SelfCreditOrderHistoryData();
        try {
            orders = SelfCreditOrderHistoryService.defaultService().collectSelfCreditOrders(user);
        } catch (FDResourceException e) {
            returnHttpError(500, "Failed to load orders for self-credit process", e);
        }

        Map<String, Object> responseData = new HashMap<String, Object>();
        responseData.put("orderhistory", orders);
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
