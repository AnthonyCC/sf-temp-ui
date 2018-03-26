package com.freshdirect.webapp.cos.util;

import javax.servlet.http.Cookie;

import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.rollout.EnumRolloutFeature;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.webapp.features.service.FeaturesService;

public final class CosFeatureUtil {

	public static boolean isUnbxdCosAction(FDUserI user, Cookie[] cookies) {

		boolean cosAction = false;

		if (user != null) {
			cosAction = FeaturesService.defaultService().isFeatureActive(EnumRolloutFeature.cosRedesign2017, cookies,
					user) && user.isCorporateUser()
					&& "FreshDirect".equals(ContentFactory.getInstance().getStoreKey().getId());
		}

		return cosAction;
	}
}