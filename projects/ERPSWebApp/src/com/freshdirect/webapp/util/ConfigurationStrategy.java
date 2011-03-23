package com.freshdirect.webapp.util;

import com.freshdirect.fdstore.content.ProductModel;

/**
 * Get a {@link ProductImpression} of a {@link ProductModel product}.
 * 
 * @author istvan
 */
public interface ConfigurationStrategy {

	/**
	 * Get a {@link ProductImpression} of the product.
	 * 
	 * 
	 * @param productModel product
	 * @param context configuration context
	 * @return a product impression
	 */
	public ProductImpression configure(ProductModel productModel, ConfigurationContext context);
	
}
