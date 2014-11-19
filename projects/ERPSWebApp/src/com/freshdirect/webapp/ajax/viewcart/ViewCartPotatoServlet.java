package com.freshdirect.webapp.ajax.viewcart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.fdstore.VariantSelectorFactory;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.product.data.ProductData;
import com.freshdirect.webapp.ajax.recommendation.RecommendationRequestObject;
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
