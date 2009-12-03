package com.freshdirect.mobileapi.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Category;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.controller.data.response.SmartStoreRecommendations;
import com.freshdirect.mobileapi.exception.JsonException;
import com.freshdirect.mobileapi.exception.NoSessionException;
import com.freshdirect.mobileapi.model.ResultBundle;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.model.SmartStore;
import com.freshdirect.mobileapi.model.SmartStore.SmartStoreRecommendationContainer;
import com.freshdirect.mobileapi.model.tagwrapper.SessionParamName;
import com.freshdirect.mobileapi.service.ServiceException;
import com.freshdirect.smartstore.fdstore.Recommendations;

/**
 * @author rsung, csongor
 *
 */
public class SmartStoreController extends BaseController {

    private static Category LOGGER = LoggerFactory.getInstance(CustomerCreatedListController.class);

    private static String ACTION_GET_YMAL = "getymal";

    private static String ACTION_GET_YOUR_FAVORITE = "getyourfavorite";

    private static String ACTION_GET_FRESHDIRECT_FAVORITE = "getfdfavorite";

    private static String ACTION_GET_FAVORITE = "favorite";

    @Override
    protected ModelAndView processRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView model, String action,
            SessionUser user) throws ServiceException, NoSessionException, JsonException {

        if (ACTION_GET_YMAL.equals(action)) {
            model = getRecommendations(EnumSiteFeature.YMAL, model, user, request);
        } else if (ACTION_GET_YOUR_FAVORITE.equals(action)) {
            model = getRecommendations(EnumSiteFeature.DYF, model, user, request);
        } else if (ACTION_GET_FRESHDIRECT_FAVORITE.equals(action)) {
            model = getRecommendations(EnumSiteFeature.FAVORITES, model, user, request);
        } else if (ACTION_GET_FAVORITE.equals(action)) {
            model = getFavoritesRecommendations(model, user, request);
        }
        return model;
    }

    private ModelAndView getFavoritesRecommendations(ModelAndView model, SessionUser user, HttpServletRequest request) throws JsonException {
        String page = request.getParameter("page");
        Message responseMessage = null;

        SmartStore smartStore = new SmartStore(user);

        ResultBundle resultBundle = smartStore.getFavoritesRecommendations(qetRequestData(request), (Recommendations) request.getSession()
                .getAttribute(SessionParamName.SESSION_PARAM_PREVIOUS_RECOMMENDATIONS), (String) request.getSession().getAttribute(
                SessionParamName.SESSION_PARAM_PREVIOUS_IMPRESSION), page);

        ActionResult result = resultBundle.getActionResult();
        propogateSetSessionValues(request.getSession(), resultBundle);

        if (result.isSuccess()) {
            responseMessage = new SmartStoreRecommendations((SmartStoreRecommendationContainer) resultBundle
                    .getExtraData(SmartStore.RECOMMENDATION));
            responseMessage.setSuccessMessage("Favorite recommendations have been retrieved successfully.");
        } else {
            responseMessage = getErrorMessage(result, request);
        }
        responseMessage.addWarningMessages(result.getWarnings());
        setResponseMessage(model, responseMessage, user);
        return model;
    }

    private ModelAndView getRecommendations(EnumSiteFeature siteFeature, ModelAndView model, SessionUser user, HttpServletRequest request)
            throws JsonException {
        String page = request.getParameter("page");
        Message responseMessage = null;

        SmartStore smartStore = new SmartStore(user);

        ResultBundle resultBundle = smartStore.getRecommendations(siteFeature, qetRequestData(request), (Recommendations) request
                .getSession().getAttribute(SessionParamName.SESSION_PARAM_PREVIOUS_RECOMMENDATIONS), (String) request.getSession()
                .getAttribute(SessionParamName.SESSION_PARAM_PREVIOUS_IMPRESSION), page);

        ActionResult result = resultBundle.getActionResult();
        propogateSetSessionValues(request.getSession(), resultBundle);

        if (result.isSuccess()) {
            responseMessage = new SmartStoreRecommendations((SmartStoreRecommendationContainer) resultBundle
                    .getExtraData(SmartStore.RECOMMENDATION));
            responseMessage.setSuccessMessage(siteFeature.getTitle() + " recommendations have been retrieved successfully.");
        } else {
            responseMessage = getErrorMessage(result, request);
        }
        responseMessage.addWarningMessages(result.getWarnings());
        setResponseMessage(model, responseMessage, user);
        return model;
    }
}
