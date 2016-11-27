package com.freshdirect.mobileapi.model.filter;

import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.mobileapi.util.Filter;

/**
 * Filter products by availability 
 * @author fgarcia
 *
 */
public class OnlyAvailableProductFilter implements Filter<ProductModel> {

    @Override
    public boolean isFiltered(ProductModel object) {
        return !object.isUnavailable();
    }

}
