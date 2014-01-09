package com.freshdirect.mobileapi.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Category;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.controller.data.SearchResult;
import com.freshdirect.mobileapi.controller.data.request.SearchQuery;
import com.freshdirect.mobileapi.controller.data.response.SmartStoreRecommendations;
import com.freshdirect.mobileapi.exception.JsonException;
import com.freshdirect.mobileapi.exception.NoSessionException;
import com.freshdirect.mobileapi.model.Product;
import com.freshdirect.mobileapi.model.ResultBundle;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.model.SmartStore;
import com.freshdirect.mobileapi.model.SmartStore.SmartStoreRecommendationContainer;
import com.freshdirect.mobileapi.model.tagwrapper.SessionParamName;
import com.freshdirect.mobileapi.service.ServiceException;
import com.freshdirect.mobileapi.util.ListPaginator;
import com.freshdirect.smartstore.fdstore.Recommendations;
import com.freshdirect.webapp.util.DepartmentCarouselUtil;

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
    
    private static String ACTION_GET_BRANDNAME_DEAL_CAROUSEL = "getBrandNameDealCarousel";
    
    private static String ACTION_GET_YOUR_FAVORITE_CAROUSEL = "getYourFavoriteCarousel";
    
    private static String ACTION_GET_PEAKPRODUCE_CAROUSEL = "getPeakProduceCarousel";
    
    private static String ACTION_GET_MEAT_BESTDEAL_CAROUSEL = "getMeatBestDealCarousel";
    
    private static String ACTION_GET_DEPARTMENT_CAROUSEL = "getDepartmentCarousel";
    
    private static final String PARAM_DEPT_ID = "departmentId";

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
        } else if (ACTION_GET_BRANDNAME_DEAL_CAROUSEL.equals(action)) {        	
            model = getRecommendations(EnumSiteFeature.BRAND_NAME_DEALS.getName(), model, user, request);
        } else if (ACTION_GET_YOUR_FAVORITE_CAROUSEL.equals(action)) {        	
            model = getRecommendations(EnumSiteFeature.DYF.getName(), model, user, request);
        } else if (ACTION_GET_PEAKPRODUCE_CAROUSEL.equals(action)) {
        	model = getPeakProduce(model, user, request, response);
        } else if (ACTION_GET_MEAT_BESTDEAL_CAROUSEL.equals(action)) {
        	model = getMeatBestDeals(model, user, request, response);
        } else if (ACTION_GET_DEPARTMENT_CAROUSEL.equals(action)) {        
        	String deptId = request.getParameter(PARAM_DEPT_ID);
        	EnumSiteFeature siteFeature = DepartmentCarouselUtil.getCarousel(deptId);
        	
        	if(siteFeature != null) {
        		if (EnumSiteFeature.BRAND_NAME_DEALS.equals(siteFeature)) {        	
                    model = getRecommendations(EnumSiteFeature.BRAND_NAME_DEALS.getName(), model, user, request);
        		} else if (EnumSiteFeature.DYF.equals(siteFeature)) {        	
                    model = getRecommendations(EnumSiteFeature.DYF.getName(), model, user, request);
                } else if (EnumSiteFeature.PEAK_PRODUCE.equals(siteFeature)) {        	
                    model = getPeakProduce(model, user, request, response);
                } else if (EnumSiteFeature.WEEKS_MEAT_BEST_DEALS.equals(siteFeature)) {        	
                    model = getMeatBestDeals(model, user, request, response);
                }   
        	}
        }
        return model;
    }
        
   	private ModelAndView getMeatBestDeals(ModelAndView model,
   			SessionUser user, HttpServletRequest request, HttpServletResponse response) throws JsonException {
       	
           SearchResult data = new SearchResult();
           
           SmartStore smartStore = new SmartStore(user);
           
           List<com.freshdirect.mobileapi.model.Product> products = smartStore.getMeatBestDeals();
         
           data.setProductsFromModel(products);
           data.setTotalResultCount(products.size());
           data.setSuccessMessage(EnumSiteFeature.WEEKS_MEAT_BEST_DEALS.getTitle() + " have been retrieved successfully.");
           setResponseMessage(model, data, user);
           return model;
    }
       	
 	private ModelAndView getPeakProduce(ModelAndView model,
			SessionUser user, HttpServletRequest request, HttpServletResponse response) throws JsonException {
		
    	String deptId = request.getParameter(PARAM_DEPT_ID);
    	    	
        Message responseMessage = null;
    	
        SearchResult data = new SearchResult();
        
        SmartStore smartStore = new SmartStore(user);
        
        ResultBundle resultBundle = smartStore.getPeakProduceProductList(deptId);

        ActionResult result = resultBundle.getActionResult();
        List<com.freshdirect.mobileapi.model.Product> products = (List<Product>) resultBundle.getExtraData(SmartStore.PEAKPRODUCE);
        if (result.isSuccess() && products.size() > 0) {
            data.setProductsFromModel(products);
            data.setTotalResultCount(products.size());
            data.setSuccessMessage(EnumSiteFeature.PEAK_PRODUCE.getTitle() + " have been retrieved successfully.");
            setResponseMessage(model, data, user);
        } else if(result.isSuccess() && products.size() == 0) {
        	model = getRecommendations(EnumSiteFeature.DYF.getName(), model, user, request);
        } else {
            responseMessage = getErrorMessage(result, request);
            responseMessage.addWarningMessages(result.getWarnings());
            setResponseMessage(model, responseMessage, user);
        }
        
        return model;
	}

    private ModelAndView getRecommendations(String siteFeature, ModelAndView model,
			SessionUser user, HttpServletRequest request) throws JsonException {
		
    	String deptId = request.getParameter(PARAM_DEPT_ID);
    	
    	String page = request.getParameter("page");
        Message responseMessage = null;
    	
    	SmartStore smartStore = new SmartStore(user);
       
        ResultBundle resultBundle = smartStore.getRecommendations(siteFeature, deptId, qetRequestData(request), (Recommendations) request
                .getSession().getAttribute(SessionParamName.SESSION_PARAM_PREVIOUS_RECOMMENDATIONS), (String) request.getSession()
                .getAttribute(SessionParamName.SESSION_PARAM_PREVIOUS_IMPRESSION), page);
        
        SmartStoreRecommendations ssResult = new SmartStoreRecommendations((SmartStoreRecommendationContainer) resultBundle
                .getExtraData(SmartStore.RECOMMENDATION));
        
        if (ssResult != null && ssResult.getProducts().size() == 0
        		&& EnumSiteFeature.DYF.getName().equals(siteFeature)) {
        	resultBundle = smartStore.getRecommendations(EnumSiteFeature.FAVORITES.getName(), deptId, qetRequestData(request), (Recommendations) request
                    .getSession().getAttribute(SessionParamName.SESSION_PARAM_PREVIOUS_RECOMMENDATIONS), (String) request.getSession()
                    .getAttribute(SessionParamName.SESSION_PARAM_PREVIOUS_IMPRESSION), page);
        }

        ActionResult result = resultBundle.getActionResult();
        propogateSetSessionValues(request.getSession(), resultBundle);

        if (result.isSuccess()) {
            responseMessage = new SmartStoreRecommendations((SmartStoreRecommendationContainer) resultBundle
                    .getExtraData(SmartStore.RECOMMENDATION));
            responseMessage.setSuccessMessage(siteFeature + " recommendations have been retrieved successfully.");
        } else {
            responseMessage = getErrorMessage(result, request);
        }
        responseMessage.addWarningMessages(result.getWarnings());
        setResponseMessage(model, responseMessage, user);
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
