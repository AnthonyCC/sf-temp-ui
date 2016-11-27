package com.freshdirect.webapp.ajax.reorder;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.freshdirect.cms.ContentKey.InvalidContentKeyException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.webapp.ajax.AbstractRecommenderServlet;
import com.freshdirect.webapp.ajax.reorder.service.QuickShopCarouselService;
import com.freshdirect.webapp.ajax.reorder.service.QuickShopCrazyQuickshopRecommendationService;
import com.freshdirect.webapp.ajax.recommendation.RecommendationRequestObject;
import com.freshdirect.webapp.ajax.viewcart.data.RecommendationTab;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;

/**
 * AJAX back-end for QS bottom carousel
 * 
 * Mapping: /api/reorder/recommendation
 *
 * @ticket APPDEV-3901
 * 
 * @author segabor
 *
 */
public class QuickShopRecommendationServlet extends AbstractRecommenderServlet {

	private static final long serialVersionUID = -7026106275198161858L;

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
		} else {
			super.doGet(request, response, user);
		}
	}
	
	@Override
	protected void doRecommendation(HttpServletRequest request,
			FDSessionUser user, HttpSession session, Variant variant,
			String parentImpressionId, String parentVariantId,
			RecommendationTab recommendationTab) throws FDResourceException,
			InvalidContentKeyException {

		QuickShopCarouselService.defaultService().doGenericRecommendation(
				session, request, user, recommendationTab, variant,
				parentImpressionId, parentVariantId);
	}
}
