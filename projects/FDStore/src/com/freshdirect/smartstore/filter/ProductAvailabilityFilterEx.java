package com.freshdirect.smartstore.filter;

import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SkuModel;

/**
 * Check whether product or one of its alternatives is available
 * 
 * @author segabor
 *
 */
public class ProductAvailabilityFilterEx extends ProductAvailabilityFilter {
	@Override
	public ProductModel filter(ProductModel model) {
        if (model == null || available(model)) {
            return model;
        }

		for (ContentNodeModel alternativeModel : model.getRecommendedAlternatives()) {
            if (alternativeModel instanceof ProductModel) {
	            if (available((ProductModel) alternativeModel)) {
	            	return (ProductModel) alternativeModel;
	            } else if (alternativeModel instanceof SkuModel) {
	                return filter((ProductModel) alternativeModel.getParentNode());
	            }
            }
        }
        return null;
	}
}
