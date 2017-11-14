package com.freshdirect.webapp.ajax.filtering;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.freshdirect.fdstore.FDNotFoundException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.coremetrics.builder.PageViewTagInput;
import com.freshdirect.fdstore.coremetrics.builder.SkipTagException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.rollout.EnumRolloutFeature;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.CoremetricsPopulator;
import com.freshdirect.webapp.ajax.DataPotatoField;
import com.freshdirect.webapp.ajax.JsonHelper;
import com.freshdirect.webapp.ajax.browse.FilteringFlowType;
import com.freshdirect.webapp.ajax.browse.data.BrowseData.SearchParams;
import com.freshdirect.webapp.ajax.browse.data.BrowseData.SearchParams.Tab;
import com.freshdirect.webapp.ajax.browse.data.CmsFilteringFlowResult;
import com.freshdirect.webapp.features.service.FeaturesService;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
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
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {

        try {
            final CmsFilteringNavigator navigator = CmsFilteringNavigator.createInstance(request, user);
            final CmsFilteringFlowResult result = CmsFilteringFlow.getInstance().doFlow(navigator, (FDSessionUser) user);

            writeResponseData(response, result);
        } catch (InvalidFilteringArgumentException e) {
            returnHttpError(400, "JSON contains invalid arguments", e); // 400 Bad Request
        } catch (FDNotFoundException e) {
            returnHttpError(404, "Node is not found in CMS", e); // 404 Bad Request
        } catch (FDResourceException e) {
            returnHttpError(500, "Unable to load Global Navigation", e);
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
     */
    @SuppressWarnings("unchecked")
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {

        try {
            CmsFilteringNavigator navigator = CmsFilteringNavigator.createInstance(request, user);
            ContentFactory.getInstance().setEligibleForDDPP(FDStoreProperties.isDDPPEnabled() || ((FDSessionUser) user).isEligibleForDDPP());
            final CmsFilteringFlowResult flow = CmsFilteringFlow.getInstance().doFlow(navigator, (FDSessionUser) user);
            final Map<String, ?> payload = DataPotatoField.digBrowse(flow);

            if (request.getParameterMap().keySet().contains("data")) {
                final Map<String, Object> clientInput;
                try {

                    //
                    // CoreMetrics reporting section
                    //

                    clientInput = JsonHelper.parseRequestData(request, Map.class);

                    final BrowseEvent cmEvent = clientInput.get("browseEvent") != null ? BrowseEvent.valueOf(((String) clientInput.get("browseEvent")).toUpperCase())
                            : BrowseEvent.NOEVENT;

                    // handle event
                    switch (cmEvent) {
                        case PAGEVIEW:
                            SearchParams searchParams = flow.getBrowseDataPrototype().getSearchParams();
                            if (searchParams.getPageType() == FilteringFlowType.SEARCH) {
                                List<Tab> tabs = searchParams.getTabs();

                                String searchTerm = searchParams.getSearchParams();
                                String suggestedTerm = searchParams.getSearchTerm();
                                Integer searchResultsSize = tabs.size() > 0 ? tabs.get(0).getHits() : 0;
                                Integer recipeSearchResultsSize = tabs.size() > 1 ? tabs.get(1).getHits() : 0;

                                new CoremetricsPopulator().appendPageViewTag((Map<String, Object>) payload,
                                        PageViewTagInput.populateFromJSONInput(request.getRequestURI(), clientInput), searchTerm, suggestedTerm, searchResultsSize,
                                        recipeSearchResultsSize, user);
                            } else {
                                new CoremetricsPopulator().appendPageViewTag((Map<String, Object>) payload,
                                        PageViewTagInput.populateFromJSONInput(request.getRequestURI(), clientInput), user);
                            }
                            break;
                        case ELEMENT:
                            new CoremetricsPopulator().appendFilterElementTag((Map<String, Object>) payload, (Map<String, Object>) clientInput.get("requestFilterParams"), user);
                            break;
                        case SORT:
                            new CoremetricsPopulator().appendSortElementTag((Map<String, Object>) payload, navigator.getSortBy(), user);
                            break;
                        case PAGE:
                        case NOEVENT:
                        default:
                            // do nothing
                            break;
                    }

                } catch (JsonParseException e) {
                    returnHttpError(400, "Cannot read client input", e); // 400 Bad Request
                } catch (JsonMappingException e) {
                    returnHttpError(400, "Cannot read client input", e); // 400 Bad Request
                } catch (IOException e) {
                    returnHttpError(400, "Cannot read client input", e); // 400 Bad Request
                } catch (SkipTagException e) {
                    returnHttpError(400, "Cannot read client input", e); // 400 Bad Request
                }
            }

            // UNBXD analytics reporting
            if (FeaturesService.defaultService().isFeatureActive(EnumRolloutFeature.unbxdanalytics2016, request.getCookies(), user)) {

                if (!navigator.getPageType().isSearchLike() && !navigator.isPdp()) {
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
        }
    }

    @Override
    protected int getRequiredUserLevel() {
        return FDUserI.GUEST;
    }
}
