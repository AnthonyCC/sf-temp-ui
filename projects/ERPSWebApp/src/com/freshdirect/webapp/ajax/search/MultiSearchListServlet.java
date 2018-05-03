package com.freshdirect.webapp.ajax.search;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    protected void doGet(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {
        writeResponseData(response, ImmutableMap.of(PARAM_NAME, user.getMultiSearchList()));
    }
    
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {
        user.setMultiSearchList(request.getParameter(PARAM_NAME));
        saveUser(user);
    }
}
