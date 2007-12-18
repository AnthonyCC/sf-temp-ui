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
	 * Gets the first available sku, which must be the only one.
	 * @return sku
	 */
	public SkuModel getSku() {
		for(Iterator i = getProduct().getSkus().iterator(); i.hasNext();) {
			SkuModel sku = (SkuModel)i.next();
			if (!sku.isUnavailable()) return sku;
		}
		return null;
	}
	
	/**
	 * Get sku code.
	 * @see #getSku()
	 * @return sku code
	 */
	public String getSkuCode() {
		SkuModel sku = getSku();
		return sku == null ? null : sku.getSkuCode();
	}
	
	

}
