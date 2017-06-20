// package com.freshdirect.webapp.ajax.viewcart.service;
//
// import java.util.ArrayList;
// import java.util.Collections;
// import java.util.List;
//
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpSession;
//
// import org.apache.log4j.Logger;
//
// import com.freshdirect.cms.ContentKey.InvalidContentKeyException;
// import com.freshdirect.fdstore.FDResourceException;
// import com.freshdirect.fdstore.FDSkuNotFoundException;
// import com.freshdirect.fdstore.FDStoreProperties;
// import com.freshdirect.fdstore.customer.FDUserI;
// import com.freshdirect.fdstore.rollout.EnumRolloutFeature;
// import com.freshdirect.fdstore.util.EnumSiteFeature;
// import com.freshdirect.framework.event.EnumEventSource;
// import com.freshdirect.framework.util.log.LoggerFactory;
// import com.freshdirect.smartstore.Variant;
// import com.freshdirect.smartstore.fdstore.VariantSelectorFactory;
// import com.freshdirect.webapp.ajax.recommendation.RecommendationRequestObject;
// import com.freshdirect.webapp.ajax.viewcart.data.RecommendationTab;
// import com.freshdirect.webapp.ajax.viewcart.data.ViewCartCarouselData;
// import com.freshdirect.webapp.features.service.FeaturesService;
// import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
// import com.freshdirect.webapp.taglib.smartstore.Impression;
//
// public class RecommenderPotatoService {
//
// private static final Logger LOGGER = LoggerFactory.getInstance(RecommenderPotatoService.class);
//
// private static final RecommenderPotatoService INSTANCE = new RecommenderPotatoService();
//
// private RecommenderPotatoService() {
// }
//
// public static RecommenderPotatoService getDefaultService() {
// return INSTANCE;
// }
//
// public ViewCartCarouselData getViewCartPageCarousels(FDUserI user, HttpServletRequest request, CartCarouselType type, boolean isError) {
// ViewCartCarouselData carousels = new ViewCartCarouselData();
//
// try {
// boolean isUserAlreadyOrdered = isUserAlreadyOrdered(user);
// List<String> siteFeatures = getSiteFeatures(type, isUserAlreadyOrdered, request, user);
// String selectedSiteFeature = getDefaultSiteFeature(siteFeatures, isError, isUserAlreadyOrdered);
// List<RecommendationRequestObject> requestDatas = getRequestDatas(siteFeatures, selectedSiteFeature, type);
// getProductRecommenderTabs(user, request, carousels.getRecommendationTabs(), requestDatas);
// } catch (FDResourceException e) {
// LOGGER.error("recommendation failed", e);
// } catch (FDSkuNotFoundException e) {
// LOGGER.error("sku not found", e);
// } catch (InvalidContentKeyException e) {
// LOGGER.error("invalid content key", e);
// }
// return carousels;
// }
//
// private String getDefaultSiteFeature(List<String> siteFeatures, boolean isError, boolean isCurrentUser) {
// String selected;
// if (siteFeatures.isEmpty() || isError) {
// selected = isCurrentUser ? "DYF" : "FAVORITES";
// } else {
// selected = siteFeatures.get(0);
// }
// return selected;
// }
//
// private List<String> getSiteFeatures(CartCarouselType type, boolean isCurrentUser, HttpServletRequest request, FDUserI user) throws FDResourceException {
// List<String> siteFeatures = null;
// // TODO make service feature independent
// boolean isCartTabCarsFeatureActive = FeaturesService.defaultService().isFeatureActive(EnumRolloutFeature.carttabcars, request.getCookies(), user);
// if (isCartTabCarsFeatureActive) {
// switch (type) {
// case VIEWCART_PAGE:
// siteFeatures = isCurrentUser ? FDStoreProperties.getViewcartCurrentCustomerCarouselSiteFeatures()
// : FDStoreProperties.getViewcartNewCustomerCarouselSiteFeatures();
// break;
//
// case CHECKOUT_PAGE:
// siteFeatures = isCurrentUser ? FDStoreProperties.getCheckoutCurrentCustomerCarouselSiteFeatures()
// : FDStoreProperties.getCheckoutNewCustomerCarouselSiteFeatures();
// break;
//
// default:
// siteFeatures = Collections.emptyList();
// break;
// }
// }
// return siteFeatures;
// }
//
// private boolean isUserAlreadyOrdered(FDUserI user) throws FDResourceException {
// return user.getLevel() > FDUserI.RECOGNIZED && user.getAdjustedValidOrderCount() >= 3;
// }
//
// private List<RecommendationRequestObject> getRequestDatas(List<String> siteFeatures, String selectedSiteFeature, CartCarouselType type) {
// List<RecommendationRequestObject> requestDatas = new ArrayList<RecommendationRequestObject>();
// for (String siteFeature : siteFeatures) {
// RecommendationRequestObject requestData = new RecommendationRequestObject();
// requestData.setSelected(siteFeature.equals(selectedSiteFeature));
// requestData.setFeature(siteFeature);
// requestData.setEventSource(getEventSource(siteFeature, type));
// requestDatas.add(requestData);
// }
// return requestDatas;
// }
//
// private String getEventSource(String siteFeature, CartCarouselType type) {
// StringBuffer eventSource = new StringBuffer();
// switch (type) {
// case VIEWCART_PAGE:
// if (isSiteFeatureProductSample(siteFeature)) {
// eventSource.append(FDStoreProperties.isPropDonationProductSamplesEnabled() ? EnumEventSource.ps_carousel_view_cart.getName()
// : EnumEventSource.dn_carousel_view_cart.getName());
// } else {
// eventSource.append(EnumEventSource.view_cart.getName());
// }
// break;
//
// case CHECKOUT_PAGE:
// if (isSiteFeatureProductSample(siteFeature)) {
// eventSource.append(FDStoreProperties.isPropDonationProductSamplesEnabled() ? EnumEventSource.ps_carousel_checkout.getName()
// : EnumEventSource.dn_carousel_checkout.getName());
// } else {
// eventSource.append(EnumEventSource.checkout.getName());
// }
// break;
//
// case OTHER:
// if (isSiteFeatureProductSample(siteFeature)) {
// eventSource.append(FDStoreProperties.isPropDonationProductSamplesEnabled() ? EnumEventSource.ps_caraousal.getName() : EnumEventSource.dn_caraousal.getName());
// }
// break;
//
// default:
// break;
// }
// return eventSource.toString();
// }
//
// private boolean isSiteFeatureProductSample(String siteFeature) {
// return "PRODUCT_SAMPLES".equals(siteFeature);
// }
//
// private void getProductRecommenderTabs(FDUserI user, HttpServletRequest request, List<RecommendationTab> tabs, List<RecommendationRequestObject> requestDatas)
// throws FDSkuNotFoundException, FDResourceException, InvalidContentKeyException {
// for (RecommendationRequestObject requestData : requestDatas) {
// String siteFeature = requestData.getFeature();
// RecommendationTab tab = null;
// // TODO eliminates this
// if (isSiteFeatureProductSample(siteFeature)) {
// tab = getProductSamplesTab(user, request, requestData);
// } else {
// tab = getRecommendationTab(request, user, requestData);
// }
// tabs.add(tab);
// }
// }
//
// public RecommendationTab getProductSamplesTab(FDUserI user, HttpServletRequest request, RecommendationRequestObject requestData)
// throws FDSkuNotFoundException, FDResourceException {
// boolean isCheckout2FeatureActive = FeaturesService.defaultService().isFeatureActive(EnumRolloutFeature.checkout2_0, request.getCookies(), user);
// // APPDEV-5516 If the property is true, populate the Donation Carousel , else fall back to Product Sample Carousel
// RecommendationTab productSamplesTab = null;
// if (FDStoreProperties.isPropDonationProductSamplesEnabled()) {
// productSamplesTab = ViewCartCarouselService.defaultService().populateViewCartPageDonationProductSampleCarousel(user);
// } else {
// productSamplesTab = ViewCartCarouselService.defaultService().populateViewCartPageProductSampleCarousel(user, isCheckout2FeatureActive);
// }
// productSamplesTab.setSelected(requestData.isSelected());
// return productSamplesTab;
// }
//
// public RecommendationTab getRecommendationTab(HttpServletRequest request, FDUserI user, RecommendationRequestObject requestData)
// throws FDResourceException, InvalidContentKeyException {
// HttpSession session = request.getSession();
// String siteFeature = requestData.getFeature();
// EnumSiteFeature enumSiteFeature = EnumSiteFeature.getEnum(siteFeature);
// Variant variant = VariantSelectorFactory.getSelector(enumSiteFeature).select(user, false);
// String parentImpressionId = requestData.getParentImpressionId();
// String impressionId = requestData.getImpressionId();
// String parentVariantId = requestData.getParentVariantId();
//
// if (impressionId != null) {
// Impression.tabClick(impressionId);
// }
//
// String titleForVariant = ViewCartCarouselService.defaultService().getTitleForVariant(variant);
// RecommendationTab recommendationTab = new RecommendationTab(titleForVariant, enumSiteFeature.getName()).setParentImpressionId(parentImpressionId)
// .setImpressionId(impressionId).setParentVariantId(parentVariantId);
// recommendationTab.setSelected(requestData.isSelected());
// if (requestData.isSelected()) {
// ViewCartCarouselService.defaultService().doGenericRecommendation(session, request, (FDSessionUser) user, recommendationTab, variant, parentImpressionId,
// parentVariantId);
// }
// return recommendationTab;
// }
// }
