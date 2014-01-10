package com.freshdirect.webapp.util;

import java.util.StringTokenizer;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.util.EnumSiteFeature;

public class DepartmentCarouselUtil {
	
	public static EnumSiteFeature getCarousel(String deptId) {
		
		EnumSiteFeature siteFeature = EnumSiteFeature.FAVORITES;
						
		String brandNameDealdeptIds[] = { "dai", "gro", "fro" };
		
		String meatDeptIds[] = { "mea" };
				
		if (!"".equals(deptId) && deptId != null) {
			deptId = deptId.toUpperCase();
			
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
			
			for(String _tmpDeptId : brandNameDealdeptIds) {
				if(_tmpDeptId.equalsIgnoreCase(deptId)) {
					return EnumSiteFeature.BRAND_NAME_DEALS;					
				}
			}
			
			if (FDStoreProperties.isDeptEDLPEnabled()) { 
				for(String _tmpDeptId : meatDeptIds) {
					if(_tmpDeptId.equalsIgnoreCase(deptId)) {
						return EnumSiteFeature.WEEKS_MEAT_BEST_DEALS;					
					}
				}
			}
				
		}
		
		return siteFeature;
	}

}
