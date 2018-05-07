package com.freshdirect.webapp.ajax.search;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.google.common.collect.ImmutableMap;

public class MultiSearchListServlet extends BaseJsonServlet {

    private static final long serialVersionUID = -7266513625234467962L;
    
    private static final String PARAM_NAME = "searchTermList";
    
    @Override
    protected boolean synchronizeOnUser() {
        return true;
    }
    
    @Override
    protected int getRequiredUserLevel() {
        return FDUserI.GUEST;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {
        writeResponseData(response, ImmutableMap.of(PARAM_NAME, user.getMultiSearchList()));
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {
        try {
            Map<String, String> requestBody = new ObjectMapper().<Map<String, String>>readValue(request.getReader(), new TypeReference<Map<String,String>>(){});
            user.setMultiSearchList(requestBody.get(PARAM_NAME));
            if (user.getLevel() != FDUserI.GUEST) {
                saveUser(user);
            }
        } catch (IOException e) {
            returnHttpError(400, "Cannot read JSON", e);  // 400 Bad Request
        }
    }
}
