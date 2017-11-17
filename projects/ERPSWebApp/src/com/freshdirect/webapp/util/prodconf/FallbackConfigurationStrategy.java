package com.freshdirect.webapp.util.prodconf;

import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.webapp.util.ConfigurationContext;
import com.freshdirect.webapp.util.ConfigurationStrategy;
import com.freshdirect.webapp.util.ProductImpression;

public class FallbackConfigurationStrategy implements ConfigurationStrategy {
	ConfigurationStrategy fallback;

	public FallbackConfigurationStrategy(ConfigurationStrategy fallback) {
		super();
		this.fallback = fallback;
	}

	public ProductImpression configure(ProductModel productModel,
			ConfigurationContext context) {
		return fallback.configure(productModel, context);
	}
}
