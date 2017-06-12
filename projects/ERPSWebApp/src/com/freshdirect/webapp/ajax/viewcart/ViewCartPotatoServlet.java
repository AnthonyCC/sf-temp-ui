package com.freshdirect.webapp.ajax.viewcart;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.rollout.EnumRolloutFeature;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.recommendation.RecommendationRequestObject;
import com.freshdirect.webapp.ajax.reorder.service.QuickShopCarouselService;
import com.freshdirect.webapp.ajax.reorder.service.QuickShopCrazyQuickshopRecommendationService;
import com.freshdirect.webapp.ajax.viewcart.data.RecommendationTab;
import com.freshdirect.webapp.ajax.viewcart.service.RecommenderPotatoService;
import com.freshdirect.webapp.ajax.viewcart.service.ViewCartCarouselService;
import com.freshdirect.webapp.features.service.FeaturesService;

public class ViewCartPotatoServlet extends BaseJsonServlet {

	private static final long serialVersionUID = 8935579121624355769L;

	@Override
	protected int getRequiredUserLevel() {
		return FDUserI.GUEST;
	}

	@Override
	protected boolean synchronizeOnUser() {
		return false;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {
		RecommendationRequestObject requestData = parseRequestData(request, RecommendationRequestObject.class, true);
		
		if (QuickShopCarouselService.QUICKSHOP_VIRTUAL_SITE_FEATURE.equals(requestData.getFeature())) {
			HttpSession session = request.getSession();
			Map<String, Object> crazyQuickshopResult = QuickShopCrazyQuickshopRecommendationService.defaultService().populateCrazyQuickshopRecommendation(session, requestData);
			writeResponseData(response, crazyQuickshopResult);
		} else if (
			ViewCartCarouselService.CAROUSEL_PRODUCT_DONATIONS_SITE_FEATURE.equals(requestData.getFeature()) ||
			EnumSiteFeature.PRODUCT_SAMPLE.getName().equals(requestData.getFeature())
		) {

			try {
			    RecommendationTab productSamplesTab = null;
				Map<String, Object> result = new HashMap<String, Object>();
				Map<String, Object> recommenderResult = new HashMap<String, Object>();
				
	            //APPDEV-5516 If the property is true, populate the Donation Carousel , else fall back to Product Sample Carousel
				//set the tabTitle here to allow for dynamic updates if the prop changes
				if(FDStoreProperties.isPropDonationProductSamplesEnabled()){
	            	productSamplesTab = ViewCartCarouselService.defaultService().populateViewCartPageDonationProductSampleCarousel(user);
	            	recommenderResult.put("tabTitle", ViewCartCarouselService.CAROUSEL_PRODUCT_DONATIONS_TAB_TITLE.toString());
					recommenderResult.put("tabSiteFeature", ViewCartCarouselService.CAROUSEL_PRODUCT_DONATIONS_SITE_FEATURE.toString());
	            } else {
	                boolean isCheckout2FeatureActive = FeaturesService.defaultService().isFeatureActive(EnumRolloutFeature.checkout2_0, request.getCookies(), user);
	                productSamplesTab = ViewCartCarouselService.defaultService().populateViewCartPageProductSampleCarousel(user, isCheckout2FeatureActive);
	                recommenderResult.put("tabTitle", EnumSiteFeature.PRODUCT_SAMPLE.getTitle());
					recommenderResult.put("tabSiteFeature", EnumSiteFeature.PRODUCT_SAMPLE.getName());
	            }
	            
				if (productSamplesTab != null && productSamplesTab.getCarouselData() != null) {
					recommenderResult.put("title", productSamplesTab.getTitle());
					recommenderResult.put("items", productSamplesTab.getCarouselData().getProducts());
				}
				recommenderResult.put("siteFeature", requestData.getFeature());
				result.put("recommenderResult", recommenderResult);
				
				writeResponseData(response, result);
			} catch (Exception e) {
				returnHttpError(500, "Error while setting samples tab. exception:" + e);
			}
		} else {
			try {
			    RecommendationTab recommendationTab = RecommenderPotatoService.getDefaultService().getRecommendationTab(request, user, requestData);
			    //TODO
				if (recommendationTab.getCarouselData() == null || recommendationTab.getCarouselData().getProducts() ==  null || recommendationTab.getCarouselData().getProducts().isEmpty()) {
					writeResponseData(response, "No recommendations found.");
				} else {
					Map<String, Object> result = new HashMap<String, Object>();
					Map<String, Object> recommenderResult = new HashMap<String, Object>();
					recommenderResult.put("siteFeature", requestData.getFeature());
					recommenderResult.put("items", recommendationTab.getCarouselData().getProducts());
					result.put("recommenderResult", recommenderResult);
					writeResponseData(response, result);
				}
			} catch (Exception e) {
				returnHttpError(500, "Cannot collect recommendations. exception:" + e);
			}
		}
	}
}
