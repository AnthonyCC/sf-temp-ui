package com.freshdirect.webapp.util.prodconf;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.util.ConfigurationContext;
import com.freshdirect.webapp.util.ConfigurationStrategy;
import com.freshdirect.webapp.util.ProductImpression;

public class SimpleConfigurationStrategy implements ConfigurationStrategy {
	private static Category LOGGER = LoggerFactory.getInstance(SimpleConfigurationStrategy.class);

	public ProductImpression configure(ProductModel productModel,
			ConfigurationContext context) {
		LOGGER.debug("configuring using simple configurer + "
				+ productModel.getContentKey().getId());
		return new ProductImpression(productModel);
	}
}
