package com.freshdirect.webapp.util;

import com.freshdirect.fdstore.FDConfigurableI;
import com.freshdirect.fdstore.content.ConfiguredProduct;
import com.freshdirect.fdstore.content.ConfiguredProductGroup;
import com.freshdirect.fdstore.content.ProductModel;

/**
 *  A class helping the display of products as YMALs.
 */
public class YmalConfiguredProductGroupHelper extends YmalConfiguredProductHelper
                                         implements YmalHelper {
	/**
	 *  Constructor.
	 *  
	 *  @param product the product that needs to be displayed as YMAL.
	 */
	YmalConfiguredProductGroupHelper(ConfiguredProductGroup product) {
		super(product);
	}

	/**
	 *  Helper to get the wrapped configured product in a type-safe manner.
	 *  Here it's going to return the active configured product within
	 *  the configured product group.
	 *  
	 *  @return the active configured product from within the wrapped
	 *          configured product group. 
	 */
	protected ConfiguredProduct getConfiguredProduct() {
		ConfiguredProductGroup confProduct         = (ConfiguredProductGroup) getProduct();
		ConfiguredProduct      embeddedConfProduct = (ConfiguredProduct) confProduct.getProduct();
		
		return embeddedConfProduct;
	}

}
