package com.freshdirect.webapp.util;

import com.freshdirect.fdstore.customer.FDInvalidConfigurationException;
import com.freshdirect.fdstore.customer.FDProductSelection;
import com.freshdirect.fdstore.customer.FDProductSelectionI;
import com.freshdirect.fdstore.customer.OrderLineUtil;
import com.freshdirect.storeapi.content.ProductModel;

/**
 * Configuration utilities.
 * @author istvan
 *
 */
public class ConfigurationUtil {
		
	/**
	 * Generate a product description for a a transactional (thus configured) product impression.
	 * 
	 * @param impression
	 * @return description or null
	 */
	public static String getConfigurationDescription(TransactionalProductImpression impression) {
		FDProductSelectionI selection = 
			new FDProductSelection(
				impression.getFDProduct(),
				((ProductModel)impression.getSku().getParentNode()),
				impression.getConfiguration(), impression.getProductModel().getUserContext());
		
		try {
			OrderLineUtil.describe(selection);
		} catch (FDInvalidConfigurationException e) {
			return null;
		}
		String description = selection.getConfigurationDesc();
		return description == null || "".equals(description) ? null : description;
	}
}
