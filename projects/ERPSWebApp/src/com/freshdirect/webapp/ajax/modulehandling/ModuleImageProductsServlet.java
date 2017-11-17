package com.freshdirect.webapp.ajax.modulehandling;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.fdstore.FDNotFoundException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.filtering.InvalidFilteringArgumentException;
import com.freshdirect.webapp.ajax.modulehandling.data.ModuleContainerData;
import com.freshdirect.webapp.ajax.modulehandling.service.ModuleHandlingService;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;

public class ModuleImageProductsServlet extends BaseJsonServlet {

    private static final long serialVersionUID = -2265070298582024696L;

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
            String imageBannerContentKey = null;
            String iconId = request.getParameter("iconId");
            String moduleVirtualCategory = request.getParameter("moduleVirtualCategory");

            if (iconId != null) {
                imageBannerContentKey = FDContentTypes.IMAGE_BANNER.getName() + ":" + iconId;
            }

            // Checklogin status is not applicable for AJAX calls so we need this.
            ContentFactory.getInstance().setEligibleForDDPP(FDStoreProperties.isDDPPEnabled() || ((FDSessionUser) user).isEligibleForDDPP());

            ModuleContainerData result = ModuleHandlingService.getDefaultService().loadModuleContainerForImageBanners(imageBannerContentKey, user, moduleVirtualCategory);

            Map<String, ModuleContainerData> moduleContent = new HashMap<String, ModuleContainerData>();

            moduleContent.put("moduleContent", result);

            writeResponseData(response, moduleContent);
        } catch (InvalidFilteringArgumentException e) {
            returnHttpError(500, "Unable to load Module", e);
        } catch (FDResourceException e) {
            returnHttpError(500, "Unable to load Module", e);
        } catch (FDNotFoundException e) {
            returnHttpError(404, "Unable to load Module", e);
        }
    }
}
