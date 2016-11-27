/**
 * 
 */
package com.freshdirect.webapp.util.prodconf;

import java.util.Map;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.content.ConfiguredProduct;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SkuModel;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.smartstore.fdstore.SmartStoreUtil;
import com.freshdirect.webapp.util.ConfigurationContext;
import com.freshdirect.webapp.util.ConfigurationStrategy;
import com.freshdirect.webapp.util.ProductImpression;
import com.freshdirect.webapp.util.TransactionalProductImpression;

public final class ConfiguredProductConfigurationStrategy extends FallbackConfigurationStrategy {
	private static Logger LOGGER = LoggerFactory.getInstance(ConfiguredProductConfigurationStrategy.class);

	public ConfiguredProductConfigurationStrategy(ConfigurationStrategy fallback) {
		super(fallback);
	}

	public ProductImpression configure(ProductModel productModel, ConfigurationContext context) {
		Map<String, ProductModel> cfgProds = SmartStoreUtil.getConfiguredProductCache();
		ConfiguredProduct configuredProduct = (ConfiguredProduct)
				cfgProds.get(productModel.getContentKey().getId());
		if (configuredProduct != null) {
			SkuModel sku = configuredProduct.getDefaultSku();
					
			if (sku == null) {
				return super.configure(productModel, context);
			} else {
				LOGGER.debug("configuring using configured product configurer + "
						+ productModel.getContentKey().getId());
				return new TransactionalProductImpression(configuredProduct,
						sku.getSkuCode(), configuredProduct.getConfiguration());
			}
		} else {
			return super.configure(productModel, context);
		}
	}
}