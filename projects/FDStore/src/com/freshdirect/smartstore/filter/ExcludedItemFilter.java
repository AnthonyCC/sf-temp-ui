/**
 * 
 */
package com.freshdirect.smartstore.filter;

import com.freshdirect.fdstore.content.ProductModel;

public final class ExcludedItemFilter extends ProductFilter {
    public ProductModel filter(ProductModel model) {
    	return model == null || model.isExcludedRecommendation() ? null : model;
    }
}