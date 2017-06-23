package com.freshdirect.webapp.ajax;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.webapp.ajax.product.data.BasicProductData;
import com.freshdirect.webapp.ajax.recommendation.RecommendationRequestObject;
import com.freshdirect.webapp.ajax.viewcart.data.RecommendationTab;
import com.freshdirect.webapp.ajax.viewcart.service.ViewCartCarouselService;

/**
 * Abstract class that supplies recommendations for tabbed carousels
 */
public class RecommenderServlet extends BaseJsonServlet {

    private static final long serialVersionUID = -414933801631213621L;

    @Override
    protected boolean synchronizeOnUser() {
        return false;
    }

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

    public static Map<String, Object> createRecommenderResult(RecommendationTab recommendationTab) {
        return createRecommenderResult(recommendationTab.getSiteFeature(), recommendationTab.getItemType(), recommendationTab.getDescription(),
                recommendationTab.getCarouselData().getProducts(), recommendationTab.getCarouselData().getCmEventSource());
    }

    public static Map<String, Object> createRecommenderResult(String siteFeature, String itemType, String title, Collection<? extends BasicProductData> items,
            String cmEventSource) {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> recommenderResult = new HashMap<String, Object>();
        recommenderResult.put("siteFeature", siteFeature);
        recommenderResult.put("itemType", itemType);
        recommenderResult.put("title", title);
        recommenderResult.put("items", items);
        recommenderResult.put("cmEventSource", cmEventSource);
        result.put("recommenderResult", recommenderResult);
        return result;
    }

}
