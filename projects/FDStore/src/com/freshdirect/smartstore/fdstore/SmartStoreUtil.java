package com.freshdirect.smartstore.fdstore;


import java.util.Iterator;
import java.util.Map;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.SkuModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.smartstore.RecommendationService;
import com.freshdirect.smartstore.Variant;

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
				SkuModel skuModel = (SkuModel)ContentFactory.getInstance().getContentNodeByKey(key);
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
	 * @param overridde variant id to use (forced)
	 * @return selected recommendation service
	 * @throws FDResourceException
	 */
	public static RecommendationService getRecommendationService(
		FDUserI user, EnumSiteFeature feature, String override) throws FDResourceException {
		RecommendationService svc = null;
		
		// lookup overridden variant
		OverriddenVariantsHelper helper = new OverriddenVariantsHelper(user);
		String v = helper.getOverriddenVariant(feature);
		if (v != null) {
			svc = (RecommendationService)SmartStoreServiceConfiguration.getInstance().getServices(feature).get(v);	
		}

		// default case - use the basic facility
		if (svc == null) {
		    String customerPK =	user != null ? user.getPrimaryKey() : null;
		    svc = VariantSelectorFactory.getInstance(feature).select(customerPK);
		}
		
		return svc;
	}
	
	/**
	 * Returns label-description couple for variant. This function is used by PIP
	 * 
	 * @param v {@link Variant variant}
	 * 
	 * @return String[label, inner text] couple
	 * 
	 * Tags: SmartStore, PIP
	 */
	public static synchronized String[] getVariantPresentation(Variant v) {
	   return new String[] { v.getServiceConfig().getPresentationTitle(), v.getServiceConfig().getPresentationDescription() };
	}
	


	/**
	 * Checks if 'anId' is a valid variant ID
	 * @param anId variant ID
	 * @param feat {@link EnumSiteFeature} site feature
	 * 
	 * @return result of check
	 */
	public static boolean checkVariantId(String anId, EnumSiteFeature feat) {
		if (anId == null)
			return false;

		if (feat == null)
			feat = EnumSiteFeature.DYF;
		
		Map services = SmartStoreServiceConfiguration.getInstance().getServices(feat);
		for (Iterator it=services.keySet().iterator(); it.hasNext();) {
			if (anId.equals( (String)it.next() )) {
				return true;
			}
		}
		
		return false;
	}
}
