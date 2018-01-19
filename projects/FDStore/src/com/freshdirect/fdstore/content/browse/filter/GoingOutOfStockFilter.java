package com.freshdirect.fdstore.content.browse.filter;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.storeapi.content.AbstractProductItemFilter;
import com.freshdirect.storeapi.content.FilterCacheStrategy;
import com.freshdirect.storeapi.content.FilteringProductItem;
import com.freshdirect.storeapi.content.ProductFilterModel;

public class GoingOutOfStockFilter extends AbstractProductItemFilter {

    public GoingOutOfStockFilter(ProductFilterModel model, String parentId) {
        super(model, parentId);
    }

    @Override
    public boolean apply(FilteringProductItem ctx) throws FDResourceException {
        if (ctx == null || ctx.getProductModel() == null) {
            return false;
        }

        return invertChecker(ctx.getProductModel().isGoingOutOfStock());
    }

    @Override
    public FilterCacheStrategy getCacheStrategy() {
        return FilterCacheStrategy.ERPS;
    }

}
