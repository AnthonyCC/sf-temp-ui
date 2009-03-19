/**
 * 
 */
package com.freshdirect.webapp.util.prodconf;

import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.webapp.util.ConfigurationContext;
import com.freshdirect.webapp.util.ConfigurationStrategy;
import com.freshdirect.webapp.util.ProductImpression;
import com.freshdirect.webapp.util.TransactionalProductImpression;

public final class ProductConfigurationStrategy implements
		ConfigurationStrategy {
	public ProductImpression configure(ProductModel productModel, ConfigurationContext context) {
		
		if (productModel.isAutoconfigurable() && productModel.getDefaultSku() != null) {
			return new TransactionalProductImpression(
				productModel,
				productModel.getDefaultSku().getSkuCode(),
				productModel.getAutoconfiguration());
		} else {
			return new ProductImpression(productModel);
		}
	}
}