package com.freshdirect.mobileapi.model.filter;

import com.freshdirect.mobileapi.util.Filter;
import com.freshdirect.storeapi.content.ProductModel;

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
