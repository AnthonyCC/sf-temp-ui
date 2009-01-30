package com.freshdirect.webapp.util;

import java.util.Map;

import javassist.bytecode.DeprecatedAttribute;

import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.smartstore.RecommendationService;
import com.freshdirect.smartstore.fdstore.OverriddenVariantsHelper;
import com.freshdirect.smartstore.fdstore.ProductStatisticsProvider;
import com.freshdirect.smartstore.fdstore.SmartStoreUtil;

public class DYFUtil {
	/**
	 * This method verifies whether customer is eligible for DYF Recommendation Service
	 * 
	 * @author segabor
	 * 
	 * @param user <{@link FDUserI}> current customer
	 * @return The result of test
	 * @throws FDResourceException 
	 * 
	 */
	public static boolean isCustomerEligible(FDUserI user) throws FDResourceException {
		if (user.getLevel()==FDUserI.GUEST || !user.isDYFEnabled()) {
			return false;
		}

		// throw customers having only one or two orders
		if (user.getOrderHistory().getValidOrderCount() < 2) {
			return false;
		}
		
		return true;
	}


	public static boolean isFavorite(ProductModel product, FDUserI user) {
		if (user == null || user.getIdentity() == null
				|| user.getIdentity().getErpCustomerPK() == null) {
			return false;
		}

		String customerId = user.getIdentity().getErpCustomerPK();
		final ProductStatisticsProvider psp = ProductStatisticsProvider.getInstance();
		final Map userProductScores = psp.getUserProductScores(customerId);
		return userProductScores.get(product.getContentKey()) != null;
	}
}
