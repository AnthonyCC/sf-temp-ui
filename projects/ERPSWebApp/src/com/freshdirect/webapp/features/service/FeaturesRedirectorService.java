package com.freshdirect.webapp.features.service;

import javax.servlet.http.Cookie;

import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.rollout.EnumRolloutFeature;

public class FeaturesRedirectorService {

	private static final FeaturesRedirectorService INSTANCE = new FeaturesRedirectorService();

	private FeaturesRedirectorService() {
	}

	public static FeaturesRedirectorService defaultService() {
		return INSTANCE;
	}

	public String getRedirectUrl(EnumRolloutFeature feature, String originalUrl, FDUserI user, Cookie[] cookies, String checkout ) {
		String redirectUrl = null;
		if (FeaturesService.defaultService().isFeatureActive(feature, cookies, user)) {
			boolean standingOrderModeActive = user.getCheckoutMode() != null && user.getCheckoutMode().isStandingOrderMode() 
					&& !user.isNewSO3Enabled();
			switch (feature) {
			case checkout2_0:
				if (!standingOrderModeActive) {
					if ("/view_cart.jsp".equals(originalUrl)) {
						redirectUrl = "/expressco/view_cart.jsp";
					} else if ("/checkout/view_cart.jsp".equals(originalUrl) && "true".equals(checkout) && null==user.getMasqueradeContext()) {
						redirectUrl = "/expressco/checkout.jsp";
					} else if ("/checkout/view_cart.jsp".equals(originalUrl)) {
						redirectUrl = "/expressco/view_cart.jsp";
					}else if ("/checkout/step_1_choose.jsp".equals(originalUrl)) {
						redirectUrl = "/expressco/checkout.jsp";
					}
				}
				break;
			default:
				redirectUrl = null;
				break;
			}
        }
		return redirectUrl;
	}
}
