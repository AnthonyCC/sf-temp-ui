package com.freshdirect.smartstore.fdstore;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;

import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModelUtil;
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

	
	private static Map vpMap = null;

	
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
		// HACK: initialize map lazily
		if (vpMap == null) {
			Map services = SmartStoreServiceConfiguration.getInstance().getServices(v.getSiteFeature());
			Set variantIds = services.keySet();
			Map prez = FDStoreProperties.getServicePresentations(v.getSiteFeature().getName());
			
			vpMap = new HashMap();
			
			final String[] def_val = {FDStoreProperties.PIP_DEFAULT_LABEL, FDStoreProperties.PIP_DEFAULT_INNERTEXT};
			
			for (Iterator vit = variantIds.iterator(); vit.hasNext();) {
				String variantId = (String) vit.next();
				if (prez.get(variantId) != null) {
					vpMap.put(variantId, prez.get(variantId));
				} else {
					vpMap.put(variantId, def_val);
				}
			}
		}

		return (String[]) vpMap.get(v.getId());
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
