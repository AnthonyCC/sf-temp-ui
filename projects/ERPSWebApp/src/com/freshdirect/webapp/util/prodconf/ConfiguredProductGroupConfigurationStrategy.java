/**
 * 
 */
package com.freshdirect.webapp.util.prodconf;

import java.util.Map;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.content.ConfiguredProduct;
import com.freshdirect.fdstore.content.ConfiguredProductGroup;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SkuModel;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.smartstore.fdstore.SmartStoreUtil;
import com.freshdirect.webapp.util.ConfigurationContext;
import com.freshdirect.webapp.util.ConfigurationStrategy;
import com.freshdirect.webapp.util.ProductImpression;
import com.freshdirect.webapp.util.TransactionalProductImpression;

public final class ConfiguredProductGroupConfigurationStrategy
		extends FallbackConfigurationStrategy {
	private static Logger LOGGER = LoggerFactory.getInstance(ConfiguredProductGroupConfigurationStrategy.class);

	public ConfiguredProductGroupConfigurationStrategy(ConfigurationStrategy fallback) {
		super(fallback);
	}

	public ProductImpression configure(ProductModel productModel, ConfigurationContext context) {
		Map<String, ProductModel> cfgProds = SmartStoreUtil.getConfiguredProductCache();
		ConfiguredProduct associateProduct = (ConfiguredProduct)
				cfgProds.get(productModel.getContentKey().getId());
		if (associateProduct != null && associateProduct instanceof ConfiguredProductGroup) {
			ConfiguredProductGroup configuredProduct = (ConfiguredProductGroup) associateProduct;
			ConfiguredProduct embeddedProduct = (ConfiguredProduct) configuredProduct.getProduct();
			
			SkuModel sku = embeddedProduct.getDefaultSku();
					
			if (sku == null) {
				return super.configure(productModel, context);
			} else {
				LOGGER.debug("configuring using configured product group configurer + "
						+ productModel.getContentKey().getId());
				return new TransactionalProductImpression(embeddedProduct,
						sku.getSkuCode(), embeddedProduct.getConfiguration());
			}
		} else
			return super.configure(productModel, context);	
	}
}