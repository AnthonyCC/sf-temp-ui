package com.freshdirect.webapp.ajax.modulehandling;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.filtering.InvalidFilteringArgumentException;
import com.freshdirect.webapp.ajax.modulehandling.data.ModuleContainerData;
import com.freshdirect.webapp.ajax.modulehandling.service.ModuleHandlingService;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;

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
            String moduleContentKey = null;
            String moduleId = request.getParameter("moduleId");
            boolean viewAll = Boolean.parseBoolean(request.getParameter("viewAll"));

            if (moduleId != null) {
                moduleContentKey = "Module:" + moduleId;
            }

            ContentFactory.getInstance().setEligibleForDDPP(FDStoreProperties.isDDPPEnabled() || ((FDSessionUser) user).isEligibleForDDPP());

            ModuleContainerData result = ModuleHandlingService.getDefaultService().loadModuleforViewAll(moduleContentKey, user, session);

            if (viewAll) {
                result.getConfig().get(0).setSourceType(ModuleSourceType.PRODUCT_LIST_MODULE.toString());
            }

            Map<String, ModuleContainerData> moduleContent = new HashMap<String, ModuleContainerData>();

            moduleContent.put("moduleContent", result);

            writeResponseData(response, moduleContent);
        } catch (InvalidFilteringArgumentException e) {
            returnHttpError(500, "Unable to load Module", e);
        } catch (FDResourceException e) {
            returnHttpError(500, "Unable to load Module", e);
        }
    }
}
