package com.freshdirect.webapp.util;

import com.freshdirect.fdstore.content.ConfiguredProduct;
import com.freshdirect.fdstore.content.ConfiguredProductGroup;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SkuModel;

/**
 * Decorate transactional YMAL impressions.
 * 
 * @author istvan
 *
 */
public class YmalConfigurationStrategy implements ConfigurationStrategy {
	
	/**
	 * Auto-configure "plain" products.
	 */
	protected static ConfigurationStrategy plainProductConfigurationUtil =
		new ConfigurationStrategy() {

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
	};
	
	/**
	 * Use the chosen configuration and sku for a configured product.
	 */
	protected static ConfigurationStrategy configuredProductConfigurationUtil = 
	
		new ConfigurationStrategy() {

		public ProductImpression configure(ProductModel productModel, ConfigurationContext context) {
			ConfiguredProduct configuredProduct = (ConfiguredProduct)productModel;
			SkuModel sku = 
				configuredProduct.getPreferredSku() == null ?
					configuredProduct.getDefaultSku() :
					configuredProduct.getPreferredSku();
					
			return sku == null ?
				new ProductImpression(productModel) :
				new TransactionalProductImpression(
					configuredProduct,
					sku.getSkuCode(),
					configuredProduct.getConfiguration()
					
				);
		}		
	};
	
	/**
	 * Use the "embedded" configured product's configuration and sku.
	 */
	protected static ConfigurationStrategy configuredProductGroupConfigurationUtil =
		
		new ConfigurationStrategy() {

			public ProductImpression configure(ProductModel productModel, ConfigurationContext context) {
				ConfiguredProduct configuredProduct = (ConfiguredProduct)productModel;
				ConfiguredProduct embeddedProduct = (ConfiguredProduct)configuredProduct.getProduct();
				SkuModel sku =
					embeddedProduct.getPreferredSku() == null ?
						embeddedProduct.getDefaultSku() :
						embeddedProduct.getPreferredSku();
						
				return sku == null ?
					new ProductImpression(embeddedProduct) :
					new TransactionalProductImpression(
						embeddedProduct,
						sku.getSkuCode(),
						embeddedProduct.getConfiguration()
						
					);
			}
		
	};
	
	/**
	 * Configure the given product.
	 * 
	 * Depending on the type of the product, the method will try
	 * to select a sku and a configuration.
	 * @param productModel the product
	 * @param context ignored
	 * @return possible configured product
	 */
	public ProductImpression configure(ProductModel productModel, ConfigurationContext context) {
		if (productModel instanceof ConfiguredProductGroup) {
			return configuredProductGroupConfigurationUtil.configure(productModel,context);	
		} else if (productModel instanceof ConfiguredProduct) {
			return configuredProductConfigurationUtil.configure(productModel,context);
		} else {
			return plainProductConfigurationUtil.configure(productModel,context);
		}		
	}

}
