package com.freshdirect.webapp.warmup.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.filters.WarmupMonitorFilter;
import com.freshdirect.webapp.warmup.WarmupService;

public class WarmupServlet extends BaseJsonServlet {

    private static final long serialVersionUID = -8075216627750602737L;

    private static final Logger LOGGER = LoggerFactory.getInstance(WarmupServlet.class);

    @Override
    protected boolean synchronizeOnUser() {
        return false;
    }

    @Override
    protected int getRequiredUserLevel() {
        return FDUserI.GUEST;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {
        WarmupService.defaultService().repeatWarmup();
        try {
            response.sendRedirect(WarmupMonitorFilter.WARMUP_PAGE_PATH);
        } catch (Exception e) { //Tomcat throws IllegalStateException and not IOException
            LOGGER.error("Redirect failed to warmup page.", e);
        }
    }

}
