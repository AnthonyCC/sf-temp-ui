package com.freshdirect.webapp.util;

import java.util.StringTokenizer;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.util.EnumSiteFeature;

public class DepartmentCarouselUtil {
	
	// WARNING: Following parts are pure craziness, never do this at home.
		//
		// I am truly sorry about all this.
		// This never should have happened.
		// I told them several times. 
		// They did not listen. 
		// They made me do this.
	    
	    public static final String DEPT_FRUIT = "fru";
	    public static final String DEPT_VEG = "veg";
	    public static final String DEPT_MEAT = "mea";
	    public static final String DEPT_SEAFOOD = "sea";
	    public static final String DEPT_DELI = "del";
	    public static final String DEPT_CHEESE = "che";
	    public static final String DEPT_DAIRY = "dai";
	    public static final String DEPT_GROCERY = "gro";
	    public static final String DEPT_FROZEN = "fro";
	    public static final String DEPT_4MM = "fdi";
	    public static final String DEPT_RTC = "rtc";
	    public static final String DEPT_HEAT = "hmr";
	    public static final String DEPT_BAKERY = "bak";
	    public static final String DEPT_CATERING = "cat";
	    public static final String DEPT_FLOWERS = "flo";
	    public static final String DEPT_PET = "pet";
	    public static final String DEPT_PASTA = "pas";
	    public static final String DEPT_COFFEE = "cof";
	    public static final String DEPT_HBA = "hba";
	    public static final String DEPT_BUYBIG = "big";

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
					if (deptId.startsWith(curPrefix) && !"HMR".equalsIgnoreCase(deptId)) {
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
	
	public static String getCarouselTitle(String deptId) {
		if (deptId != null && !deptId.trim().isEmpty()) {
			if (DEPT_FRUIT.equals(deptId) || DEPT_VEG.equals(deptId)
					|| DEPT_SEAFOOD.equals(deptId)) {

				return "Great Right Now";

			} else if (DEPT_MEAT.equals(deptId)) {

				return "This Week's Best Deals on Meat";

			} else if (DEPT_DAIRY.equals(deptId)) {

				return "Brand Name Deals in Dairy";

			} else if (DEPT_GROCERY.equals(deptId)) {

				return "Brand Name Deals in Grocery";

			} else if (DEPT_FROZEN.equals(deptId)) {

				return "Brand Name Deals in Frozen";

			}
		}

		// There are no rules for all other departments, just use the default
		return "Customer Favorites";
	}

}
