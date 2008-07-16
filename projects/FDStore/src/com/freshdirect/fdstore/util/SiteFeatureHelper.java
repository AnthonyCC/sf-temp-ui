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
		}

		return feature.isEnabled(user);
	}
}
