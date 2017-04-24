package com.freshdirect.webapp.ajax.viewcart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.fdstore.VariantSelectorFactory;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.product.data.ProductData;
import com.freshdirect.webapp.ajax.recommendation.RecommendationRequestObject;
import com.freshdirect.webapp.ajax.reorder.service.QuickShopCarouselService;
import com.freshdirect.webapp.ajax.reorder.service.QuickShopCrazyQuickshopRecommendationService;
import com.freshdirect.webapp.ajax.viewcart.data.ProductSamplesCarousel;
import com.freshdirect.webapp.ajax.viewcart.data.RecommendationTab;
import com.freshdirect.webapp.ajax.viewcart.data.ViewCartCarouselData;
import com.freshdirect.webapp.ajax.viewcart.service.ViewCartCarouselService;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.smartstore.Impression;

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
			ViewCartCarouselService.CAROUSEL_PRODUCT_SAMPLES_SITE_FEATURE.equals(requestData.getFeature())
		) {

			try {
		        ProductSamplesCarousel productSamplesTab = null;
				Map<String, Object> result = new HashMap<String, Object>();
				Map<String, Object> recommenderResult = new HashMap<String, Object>();
				
	            //APPDEV-5516 If the property is true, populate the Donation Carousel , else fall back to Product Sample Carousel
				//set the tabTitle here to allow for dynamic updates if the prop changes
				if(FDStoreProperties.isPropDonationProductSamplesEnabled()){
	            	productSamplesTab = ViewCartCarouselService.defaultService().populateViewCartPageDonationProductSampleCarousel(request);
	            	recommenderResult.put("tabTitle", ViewCartCarouselService.CAROUSEL_PRODUCT_DONATIONS_TAB_TITLE.toString());
					recommenderResult.put("tabSiteFeature", ViewCartCarouselService.CAROUSEL_PRODUCT_DONATIONS_SITE_FEATURE.toString());
	            } else {
	                productSamplesTab = ViewCartCarouselService.defaultService().populateViewCartPageProductSampleCarousel(request);
	                recommenderResult.put("tabTitle", ViewCartCarouselService.CAROUSEL_PRODUCT_SAMPLES_TAB_TITLE.toString());
					recommenderResult.put("tabSiteFeature", ViewCartCarouselService.CAROUSEL_PRODUCT_SAMPLES_SITE_FEATURE.toString());
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
			HttpSession session = request.getSession();
			String siteFeature = requestData.getFeature();
			EnumSiteFeature enumSiteFeature = EnumSiteFeature.getEnum(siteFeature);
			Variant variant = VariantSelectorFactory.getSelector(enumSiteFeature).select(user, false);
			String parentImpressionId = requestData.getParentImpressionId();
			String impressionId = requestData.getImpressionId();
			String parentVariantId = requestData.getParentVariantId();
	
			if (impressionId != null) {
				Impression.tabClick(impressionId);
			}
	
			try {
				ViewCartCarouselData viewCartCarouselData = new ViewCartCarouselData();
				String titleForVariant = ViewCartCarouselService.defaultService().getTitleForVariant(variant);
				RecommendationTab recommendationTab = new RecommendationTab(titleForVariant, enumSiteFeature.getName(), parentImpressionId, impressionId, parentVariantId);
				viewCartCarouselData.getRecommendationTabs().add(recommendationTab);
				ViewCartCarouselService.defaultService().doGenericRecommendation(session, request, (FDSessionUser) user, recommendationTab, variant, parentImpressionId, parentVariantId);
				List<ProductData> recommendations = recommendationTab.getCarouselData().getProducts();
				if (recommendations.isEmpty()) {
					writeResponseData(response, "No recommendations found.");
				} else {
					Map<String, Object> result = new HashMap<String, Object>();
					Map<String, Object> recommenderResult = new HashMap<String, Object>();
					recommenderResult.put("siteFeature", siteFeature);
					recommenderResult.put("items", recommendations);
					result.put("recommenderResult", recommenderResult);
					writeResponseData(response, result);
				}
			} catch (Exception e) {
				returnHttpError(500, "Cannot collect recommendations. exception:" + e);
			}
		}
	}
}
