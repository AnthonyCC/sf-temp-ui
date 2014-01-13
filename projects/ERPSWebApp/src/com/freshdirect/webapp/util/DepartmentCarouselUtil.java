package com.freshdirect.webapp.util;

import java.util.StringTokenizer;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.util.EnumSiteFeature;

public class DepartmentCarouselUtil {

	public static EnumSiteFeature getCarousel(String deptId) {

		String brandNameDealdeptIds[] = { "dai", "gro", "fro" };

		String meatDeptIds[] = { "mea" };

		if (deptId != null && !deptId.trim().isEmpty()) {
			deptId = deptId.toUpperCase();
			// For fruit, vegetables and seafood we should show 'great right now' stuff
			String _skuPrefixes = FDStoreProperties.getRatingsSkuPrefixes();

			if (_skuPrefixes != null && !"".equals(_skuPrefixes)) {
				StringTokenizer st = new StringTokenizer(_skuPrefixes, ",");
				String curPrefix = ""; // holds prefix to check against
				String spacer = "* "; // spacing for sysOut calls

				// loop and check each prefix
				while (st.hasMoreElements()) {
					curPrefix = st.nextToken();
					// if prefix matches get product info
					if (deptId.startsWith(curPrefix)) {
						return EnumSiteFeature.PEAK_PRODUCE;
					}
					spacer = spacer + "   ";
				}
			}
			// 'Brand Name Deals' recommendations
			for (String _tmpDeptId : brandNameDealdeptIds) {
				if (_tmpDeptId.equalsIgnoreCase(deptId)) {
					return EnumSiteFeature.BRAND_NAME_DEALS;
				}
			}
			
			// For meat we should display 'meat deals' 
			// which is actually a manually maintained category, also called as "This week's best deals on meat" in "what's good" department
			if (FDStoreProperties.isDeptEDLPEnabled()) {
				for (String _tmpDeptId : meatDeptIds) {
					if (_tmpDeptId.equalsIgnoreCase(deptId)) {
						return EnumSiteFeature.WEEKS_MEAT_BEST_DEALS;
					}
				}
			}

		}

		return null;
	}

}
