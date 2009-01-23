package com.freshdirect.webapp.util;

import com.freshdirect.fdstore.content.ProductModel;

public class SimpleConfigurationStrategy implements ConfigurationStrategy {

	public ProductImpression configure(ProductModel productModel,
			ConfigurationContext context) {
		return new ProductImpression(productModel);
	}

}
