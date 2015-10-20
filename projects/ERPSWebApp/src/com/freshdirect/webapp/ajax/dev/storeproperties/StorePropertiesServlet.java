package com.freshdirect.webapp.ajax.dev.storeproperties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.webapp.ajax.BaseJsonServlet;

public class StorePropertiesServlet extends BaseJsonServlet {

    private static final long serialVersionUID = 1405970267430521377L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {
        StorePropertiesData propData = StorePropertiesService.defaultService().loadStoreProperties();
        writeResponseData(response, propData);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {
        StorePropertiesService propService = StorePropertiesService.defaultService();
        propService.refreshStoreProperties();
        writeResponseData(response, propService.loadStoreProperties());
    }

    @Override
    protected boolean synchronizeOnUser() {
        return false;
    }
}
