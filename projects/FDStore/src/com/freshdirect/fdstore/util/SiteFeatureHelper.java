package com.freshdirect.fdstore.util;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDUserI;

/**
 * Integrated Site Feature detection helper
 * 
 * @author segabor
 */
public class SiteFeatureHelper {
	public static boolean isEnabled(EnumSiteFeature feature, FDUserI user) {
		if (user == null)
			return false;
		
		if (EnumSiteFeature.CCL.equals(feature)) {
			return FDStoreProperties.isCclEnabled() || feature.isEnabled(user);
		} else if (EnumSiteFeature.DYF.equals(feature)) {
			return FDStoreProperties.isDYFEnabled() || feature.isEnabled(user);
		} else if (EnumSiteFeature.RATING.equals(feature)) {
			return FDStoreProperties.IsProduceRatingEnabled() || feature.isEnabled(user);
		} else if (EnumSiteFeature.NEW_SEARCH.equals(feature)) {
			return FDStoreProperties.isSmartSearchEnabled() || feature.isEnabled(user);
		} else if (EnumSiteFeature.GIFT_CARDS.equals(feature)) {
			return FDStoreProperties.isGiftCardEnabled() || feature.isEnabled(user);
		}else if (EnumSiteFeature.ZONE_PRICING.equals(feature)) {
			return FDStoreProperties.isZonePricingEnabled() || feature.isEnabled(user);
		}else if (EnumSiteFeature.PAYMENTECH_GATEWAY.equals(feature)) {
			return FDStoreProperties.isPaymentechGatewayEnabled()  || feature.isEnabled(user);
		}

		return feature.isEnabled(user);
	}
}
