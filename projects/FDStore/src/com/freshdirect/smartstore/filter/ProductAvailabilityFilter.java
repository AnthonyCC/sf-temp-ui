package com.freshdirect.smartstore.filter;

import java.util.Iterator;

import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SkuModel;

/**
 * check availabilities
 * light version (no available alternatives check)
 * 
 * @author zsombor
 *
 */
public class ProductAvailabilityFilter extends ProductFilter {
    public ProductModel filter(ProductModel model) {
        if (model == null || available(model)) {
            return model;
        }

        return null;
    }

    protected boolean available(ProductModel model) {
   		return model.isDisplayable();
    }
}
