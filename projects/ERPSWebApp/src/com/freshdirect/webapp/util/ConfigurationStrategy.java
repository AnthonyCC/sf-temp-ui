package com.freshdirect.webapp.util;

import java.util.Iterator;

import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SkuModel;

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
	
	/**
	 * Implementation of a random configuration util.
	 * 
	 */
	public static ConfigurationStrategy RANDOM = new ConfigurationStrategy() {

		/**
		 * Configure the product (if possible) without much thought.
		 * 
		 * Goes through the list of skus and stops at the first one
		 * that has auto configuration.
		 * 
		 * @param productModel product
		 * @param context ignored
		 * @return a product impression of the product
		 */
		public ProductImpression configure(ProductModel productModel, ConfigurationContext context) {
			for (SkuModel skuModel : productModel.getSkus()) {
				if (skuModel.isUnavailable()) continue;
				
				
				ProductModel selectedModel = skuModel.getProductModel();
				if (selectedModel.isAutoconfigurable()) {
					return new TransactionalProductImpression(selectedModel,skuModel.getSkuCode(),selectedModel.getAutoconfiguration());
				}
			
			}
			return new ProductImpression(productModel);
		}
		
	};
}
