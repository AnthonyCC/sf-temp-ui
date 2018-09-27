package com.freshdirect.backoffice.selfcredit.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.freshdirect.backoffice.selfcredit.data.IssueSelfCreditRequest;
import com.freshdirect.backoffice.selfcredit.data.IssueSelfCreditResponse;
import com.freshdirect.backoffice.selfcredit.service.BackOfficeSelfCreditService;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.ajax.BaseJsonServlet;

public class BackOfficeSelfCreditServlet extends BaseJsonServlet {

    private static final Logger LOGGER = LoggerFactory.getInstance(BackOfficeSelfCreditServlet.class);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {
        IssueSelfCreditRequest issueSelfCreditRequest = parseRequestData(request, IssueSelfCreditRequest.class);
        IssueSelfCreditResponse selfCreditResponse = BackOfficeSelfCreditService.defaultService().postSelfCreditRequest(issueSelfCreditRequest);
        Map<String, Object> responseData = new HashMap<String, Object>();
        responseData.put("selfcreditresponse", selfCreditResponse);
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
