package com.freshdirect.smartstore.fdstore;


import java.util.Map;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.fdstore.FDResourceException;

import com.freshdirect.fdstore.content.ContentNodeModelUtil;
import com.freshdirect.fdstore.content.SkuModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.smartstore.RecommendationService;

/**
 * General utilities for SmartStore.
 * 
 * @author istvan
 *
 */
public class SmartStoreUtil {

	/**
	 * Deduce the product content key.
	 * 
	 * If the argument is a product, it will return the argument,
	 * if it is a sku, it returns the corresponding product.
	 * Otherwise, and in case of any problems, it returns null.
	 * @param key query content key
	 * @return corresponding product
	 */
	public static ContentKey getProductContentKey(ContentKey key) {
		try {
			if (key.getType().equals(FDContentTypes.PRODUCT)) return key;
			else if (key.getType().equals(FDContentTypes.SKU)) {
				SkuModel skuModel = (SkuModel)ContentNodeModelUtil.constructModel(key, true);
				return skuModel.getParentNode().getContentKey();
			} else return null;
		} catch(Exception e) {
			return null;
		}
	}
	
	/**
	 * Get the product corresponding to a SKU code.
	 * @param skuCode
	 * @return product
	 */
	public static ContentKey getProductContentKey(String skuCode) {
		return getProductContentKey(new ContentKey(FDContentTypes.SKU,skuCode));
	}


	/**
	 * Get the recommendation service for the user.
	 * 
	 * This method checks if the variant was overridden for the user in his profile (otherwise
	 * it returns the service).
	 * 
	 * @param user
	 * @param feature
	 * @return selected recommendation service
	 * @throws FDResourceException
	 */
	public static RecommendationService getRecommendationService(FDUserI user, EnumSiteFeature feature) throws FDResourceException {
		RecommendationService svc = null;
		
		// lookup overridden variant
		if (EnumSiteFeature.DYF.equals(feature)) {
			String value = user.getFDCustomer().getProfile().getAttribute("DYF.VariantID");
			
			// try to find the appropriate RecommendationService (variant implementation) by entered value
			if (value != null) {
				Map svcMap = SmartStoreServiceConfiguration.getInstance().getServices(feature);
				svc = (RecommendationService)svcMap.get(value);	
			}
		}

		// default case - use the basic facility
		if (svc == null) {
			svc = VariantSelectorFactory.getInstance(feature).select(user.getIdentity().getErpCustomerPK());
		}
		
		return svc;
	}

	
}
