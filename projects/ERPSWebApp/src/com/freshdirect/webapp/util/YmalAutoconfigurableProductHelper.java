package com.freshdirect.webapp.util;

import java.util.Iterator;

import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SkuModel;

/**
 * Autoconfigurable ymal helper.
 * 
 * A product is autoconfigurable if it has exactly one available sku and exactly one from each
 * configurable options.
 * 
 * @author istvan
 *
 */
public class YmalAutoconfigurableProductHelper extends YmalProductHelper {

	public YmalAutoconfigurableProductHelper(ProductModel product, boolean transactional) {
		super(product, transactional);
	}
	
	/**
	 * Gets the default sku.
	 * 
	 * @return sku
	 */
	public SkuModel getSku() {
		return getProduct().getDefaultSku();
	}
	
	/**
	 * Get sku code.
	 * 
	 * @see #getSku()
	 * @return sku code
	 */
	public String getSkuCode() {
		SkuModel sku = getSku();
		return sku == null ? null : sku.getSkuCode();
	}
}
