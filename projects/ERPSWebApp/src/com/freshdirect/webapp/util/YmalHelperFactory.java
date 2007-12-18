package com.freshdirect.webapp.util;

import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.fdstore.content.ConfiguredProduct;
import com.freshdirect.fdstore.content.ConfiguredProductGroup;
import com.freshdirect.fdstore.content.ProductModel;

/**
 *  A class to manufacture YMAL helper instances, to help display YMALs
 */
public class YmalHelperFactory {

	/**
	 *  The single instance of this factory.
	 */
	private static YmalHelperFactory ymalHelperFactory;
	
	/**
	 *  Private constructor.
	 */
	private YmalHelperFactory() {
	}
	
	/**
	 *  Get the singleton instance of this factory.
	 *  
	 *  @return the singleton instance of this factory.
	 */
	public static YmalHelperFactory getInstance() {
		if (ymalHelperFactory == null) {
			ymalHelperFactory = new YmalHelperFactory();
		}
		
		return ymalHelperFactory;
	}
	
	/**
	 *  Get a product helper, corresponding to the specifc subclass of
	 *  the supplied ProductModel.
	 *  
	 *  @param product the product to get the helper object for.
	 *  @param tranasctional if auto-configurable products should be presented
	 *         in a transactional manner.
	 *  @return the YMAL helper for the supplied product
	 *  @throws IllegalArgumentException if no helper could be found for
	 *          the supplied product.
	 */
	public YmalHelper getYmalHelper(ProductModel product,
			                        boolean      transactional)
										throws IllegalArgumentException {
		if (product.getContentKey().getType().equals(FDContentTypes.CONFIGURED_PRODUCT)) {
			return new YmalConfiguredProductHelper((ConfiguredProduct) product);
		} else if (product.getContentKey().getType().equals(FDContentTypes.CONFIGURED_PRODUCT_GROUP)) {
			return new YmalConfiguredProductGroupHelper((ConfiguredProductGroup) product);
		} else if (product.getContentKey().getType().equals(FDContentTypes.PRODUCT)) {
			return (transactional && product.isAutoconfigurable()) ? new YmalAutoconfigurableProductHelper(product,transactional) :
				new YmalProductHelper(product, transactional);
		}
		
		throw new IllegalArgumentException();
	}
}
