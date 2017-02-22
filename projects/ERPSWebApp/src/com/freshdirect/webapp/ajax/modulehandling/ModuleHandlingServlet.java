package com.freshdirect.webapp.ajax.modulehandling;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.webapp.ajax.BaseJsonServlet;

public class ModuleHandlingServlet extends BaseJsonServlet {

    private static final long serialVersionUID = 5274282114835055937L;

    @Override
    protected boolean synchronizeOnUser() {
        return false;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {

    }

}
