package com.freshdirect.smartstore.filter;

import java.util.Iterator;

import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SkuModel;

/**
 * check availabilities
 * 
 * @author zsombor
 *
 */
public final class ProductAvailabilityFilter extends ProductFilter {
    public ProductModel filter(ProductModel model) {
        if (model == null || available(model)) {
            return model;
        }

        for (Iterator i = model.getRecommendedAlternatives().iterator(); i.hasNext();) {
            ContentNodeModel alternativeModel = (ContentNodeModel) i.next();
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

    private boolean available(ProductModel model) {
   		return model.isDisplayable();
    }
}