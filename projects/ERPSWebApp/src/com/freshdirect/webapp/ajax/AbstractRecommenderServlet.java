package com.freshdirect.webapp.ajax;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.webapp.ajax.recommendation.RecommendationRequestObject;
import com.freshdirect.webapp.ajax.viewcart.data.RecommendationTab;
import com.freshdirect.webapp.ajax.viewcart.service.ViewCartCarouselService;

/**
 * Abstract class that supplies recommendations for tabbed carousels
 *
 */
public abstract class AbstractRecommenderServlet extends BaseJsonServlet {

    private static final long serialVersionUID = -414933801631213621L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {
        RecommendationRequestObject requestData = parseRequestData(request, RecommendationRequestObject.class, true);
        try {
            RecommendationTab recommendationTab = ViewCartCarouselService.getDefaultService().getRecommendationTab(request, user, requestData);
            Map<String, Object> result = createRecommenderResult(recommendationTab);
            writeResponseData(response, result);
        } catch (FDResourceException e) {
            returnHttpError(500, "Cannot collect recommendations. site feature:" + requestData.getFeature(), e);
        }
    }

    protected Map<String, Object> createRecommenderResult(RecommendationTab recommendationTab) {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> recommenderResult = new HashMap<String, Object>();
        recommenderResult.put("siteFeature", recommendationTab.getSiteFeature());
        recommenderResult.put("items", recommendationTab.getCarouselData().getProducts());
        recommenderResult.put("itemType", recommendationTab.getItemType());
        recommenderResult.put("cmEventSource", recommendationTab.getCarouselData().getCmEventSource());
        recommenderResult.put("description", recommendationTab.getDescription());
        result.put("recommenderResult", recommenderResult);
        return result;
    }
}
