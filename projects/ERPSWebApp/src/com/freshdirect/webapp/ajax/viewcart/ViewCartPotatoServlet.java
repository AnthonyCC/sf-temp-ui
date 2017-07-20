package com.freshdirect.webapp.ajax.viewcart;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.webapp.ajax.RecommenderServlet;
import com.freshdirect.webapp.ajax.recommendation.RecommendationRequestObject;
import com.freshdirect.webapp.ajax.reorder.service.QuickShopCrazyQuickshopRecommendationService;

public class ViewCartPotatoServlet extends RecommenderServlet {

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
		
        if (QuickShopCrazyQuickshopRecommendationService.QUICKSHOP_VIRTUAL_SITE_FEATURE.equals(requestData.getFeature())) {
			HttpSession session = request.getSession();
			Map<String, Object> crazyQuickshopResult = QuickShopCrazyQuickshopRecommendationService.defaultService().populateCrazyQuickshopRecommendation(session, requestData);
			writeResponseData(response, crazyQuickshopResult);
		} else {
            super.doGet(request, response, user);
		}
	}


}
