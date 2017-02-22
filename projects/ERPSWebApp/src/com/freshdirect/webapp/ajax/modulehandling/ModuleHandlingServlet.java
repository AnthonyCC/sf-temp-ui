package com.freshdirect.webapp.ajax.modulehandling;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.filtering.InvalidFilteringArgumentException;
import com.freshdirect.webapp.ajax.modulehandling.data.WelcomePageData;
import com.freshdirect.webapp.ajax.modulehandling.service.ModuleHandlingService;

public class ModuleHandlingServlet extends BaseJsonServlet {

    private static final long serialVersionUID = 5274282114835055937L;

    @Override
    protected boolean synchronizeOnUser() {
        return false;
    }

    @Override
    protected int getRequiredUserLevel() {
        return FDUserI.GUEST;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {
        try {
            HttpSession session = request.getSession();
            String moduleId = "Module:" + request.getParameter("moduleId");
            WelcomePageData result = ModuleHandlingService.getDefaultService().loadModuleforViewAll(moduleId, user, session);

            writeResponseData(response, result);
        } catch (InvalidFilteringArgumentException e) {
            returnHttpError(400, "JSON contains invalid arguments", e); // 400 Bad Request
        } catch (FDResourceException e) {
            returnHttpError(500, "Unable to load Module", e);
        }
    }
}
