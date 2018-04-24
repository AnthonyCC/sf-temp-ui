package com.freshdirect.webapp.ajax;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.webapp.ajax.browse.service.CarouselService;
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
            RecommendationTab recommendationTab = null;
            
            final boolean isNewProductsCarousel = CarouselService.NEW_PRODUCTS_CAROUSEL_VIRTUAL_SITE_FEATURE.equals(requestData.getFeature());
            if (isNewProductsCarousel) {
                recommendationTab = createNewProductsCarouselRecommendationTab(user, requestData);
            } else {
                recommendationTab = ViewCartCarouselService.getDefaultService().getRecommendationTab(request, user, requestData);
            }

            Map<String, Object> result = createRecommenderResult(recommendationTab);
            writeResponseData(response, result);
        } catch (FDResourceException e) {
            returnHttpError(500, "Cannot collect recommendations. site feature:" + requestData.getFeature(), e);
        }
    }

    private RecommendationTab createNewProductsCarouselRecommendationTab(FDUserI user, RecommendationRequestObject requestData) {
        final boolean isNewProductsCarouselEnabled = FDStoreProperties.isReorderPageNewProductsCarouselEnabled();
        RecommendationTab recommendationTab = null;
        if (isNewProductsCarouselEnabled) {
            final boolean isRandomizeProductOrderEnabled = FDStoreProperties.isReorderPageNewProductsCarouselRandomizeProductOrderEnabled();
            recommendationTab = new RecommendationTab(CarouselService.NEW_PRODUCTS_CAROUSEL_NAME, requestData.getFeature());
            recommendationTab.setCarouselData(
                    CarouselService.defaultService().createNewProductsCarousel(user, isRandomizeProductOrderEnabled, false));
            recommendationTab.getCarouselData().setCmEventSource("Reorder");
        }
        return recommendationTab;
    }

    public static Map<String, Object> createRecommenderResult(RecommendationTab recommendationTab) {
        Map<String, Object> result = null;
        if (recommendationTab.getCarouselData() == null) {
            result = createRecommendationResult(Collections.<String, Object> emptyMap());
        } else {
            result = createRecommenderResult(recommendationTab.getSiteFeature(), recommendationTab.getItemType(), recommendationTab.getDescription(),
                    recommendationTab.getCarouselData().getProducts(), recommendationTab.getCarouselData().getCmEventSource());
        }
        return result;
    }

    public static Map<String, Object> createRecommenderResult(String siteFeature, String itemType, String title, Collection<? extends BasicProductData> items,
            String cmEventSource) {
        Map<String, Object> recommenderResult = new HashMap<String, Object>();
        recommenderResult.put("siteFeature", siteFeature);
        recommenderResult.put("itemType", itemType);
        recommenderResult.put("title", title);
        recommenderResult.put("items", items);
        recommenderResult.put("cmEventSource", cmEventSource);
        return createRecommendationResult(recommenderResult);
    }

    public static Map<String, Object> createRecommendationResult(Map<String, Object> recommenderResult) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("recommenderResult", recommenderResult);
        return result;
    }

}
