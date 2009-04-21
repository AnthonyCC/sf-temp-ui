package com.freshdirect.smartstore.filter;

import com.freshdirect.fdstore.content.ProductModel;

/**
 * Filtering predicate for product models
 * 
 * @author zsombor
 */
public abstract class ProductFilter {
	protected ProductFilter() {
	}

    public abstract ProductModel filter(ProductModel model);
}