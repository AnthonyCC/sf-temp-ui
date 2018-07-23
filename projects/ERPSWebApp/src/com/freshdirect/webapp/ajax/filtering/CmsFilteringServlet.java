package com.freshdirect.webapp.ajax.filtering;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDNotFoundException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDAuthenticationException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.rollout.EnumRolloutFeature;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.DataPotatoField;
import com.freshdirect.webapp.ajax.browse.data.CmsFilteringFlowResult;
import com.freshdirect.webapp.features.service.FeaturesService;
import com.freshdirect.webapp.taglib.unbxd.BrowseEventTag;

public class CmsFilteringServlet extends BaseJsonServlet {

    private static final long serialVersionUID = -3643980667721343751L;
    private static final Logger LOGGER = LoggerFactory.getInstance(CmsFilteringServlet.class);

    @Override
    protected boolean synchronizeOnUser() {
        return false; // no need to synchronize
    }

    /**
     * Processing json post data
     * @throws UnsupportedEncodingException 
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse, UnsupportedEncodingException {

        try {
        	boolean isOAuth = isOAuthTokenInHeader(request);
        	
        	if (isOAuth && user.getIdentity() != null) {
				user = FDCustomerManager.recognize(user.getIdentity(), null, null, null, false, false);
			}
        	
            final CmsFilteringNavigator navigator = CmsFilteringNavigator.createInstance(request, user, false);
            final CmsFilteringFlowResult flow = CmsFilteringFlow.getInstance().doFlow(navigator, user);
            final Map<String, ?> payload = DataPotatoField.digBrowse(flow);
            writeResponseData(response, payload);
        } catch (InvalidFilteringArgumentException e) {
            returnHttpError(400, "JSON contains invalid arguments", e); // 400 Bad Request
        } catch (FDNotFoundException e) {
            returnHttpError(404, "Node is not found in CMS", e); // 404 Bad Request
        } catch (FDResourceException e) {
            returnHttpError(500, "Unable to load Global Navigation", e);
        } catch (FDAuthenticationException e) {
        	LOGGER.error("Failed to recognize user", e);
			returnHttpError(500, "Failed to get user.");
		}

    }

    public enum BrowseEvent {
        NOEVENT,
        PAGEVIEW,
        ELEMENT,
        PAGE,
        SORT;
    }

    /**
     * Processing query from direct http call
     * @throws UnsupportedEncodingException 
     */
    @SuppressWarnings("unchecked")
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse, UnsupportedEncodingException {

        try {
        	boolean isOAuthRequest = isOAuthTokenInHeader(request);
			if (isOAuthRequest && user.getIdentity() != null) {
				user = FDCustomerManager.recognize(user.getIdentity(), null, null, null, false, false);
			}
            CmsFilteringNavigator navigator = CmsFilteringNavigator.createInstance(request, user, true);
            ContentFactory.getInstance().setEligibleForDDPP(FDStoreProperties.isDDPPEnabled() || ((FDUser) user).isEligibleForDDPP());
            final CmsFilteringFlowResult flow = CmsFilteringFlow.getInstance().doFlow(navigator, user);
            final Map<String, ?> payload = DataPotatoField.digBrowse(flow);

            // UNBXD analytics reporting
            if (FeaturesService.defaultService().isFeatureActive(EnumRolloutFeature.unbxdanalytics2016, request.getCookies(), user)) {

                if (navigator.getPageType().isBrowseType() && !navigator.isPdp()) {
                    BrowseEventTag.doSendEvent(navigator.getId(), user, request);
                }
            }

            writeResponseData(response, payload);

        } catch (FDResourceException e) {
            returnHttpError(400, "Cannot read JSON", e); // 400 Bad Request
        } catch (FDNotFoundException e) {
            returnHttpError(404, "Node is not found in CMS", e); // 404 Bad Request
        } catch (InvalidFilteringArgumentException e) {

            switch (e.getType()) {
                case NODE_IS_RECIPE_DEPARTMENT:
                case SPECIAL_LAYOUT:
                case NODE_HAS_REDIRECT_URL: {
                    Map<String, String> resp = new HashMap<String, String>();
                    resp.put("redirectUrl", e.getRedirectUrl());
                    writeResponseData(response, resp);
                    break;
                }
                case TERMINATE:
                    LOGGER.error(e.getMessage());
                    break;

                default:
                    returnHttpError(400, "JSON contains invalid arguments", e); // 400 Bad Request
                    break;
            }
        } catch (FDAuthenticationException e) {
        	LOGGER.error("Failed to recognize user", e);
			returnHttpError(500, "Failed to get user.");
		}
    }

    @Override
    protected int getRequiredUserLevel() {
        return FDUserI.GUEST;
    }
    
    @Override
    protected boolean isOAuthEnabled() {
    	return true;
    }
}
