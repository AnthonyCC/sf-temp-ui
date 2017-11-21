package com.freshdirect.fdstore.content.browse.filter;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.storeapi.content.AbstractProductItemFilter;
import com.freshdirect.storeapi.content.FilterCacheStrategy;
import com.freshdirect.storeapi.content.FilteringProductItem;
import com.freshdirect.storeapi.content.ProductFilterModel;

public class BackInStockFilter extends AbstractProductItemFilter {

	public BackInStockFilter(ProductFilterModel model, String parentId) {
		super(model, parentId);
	}

	@Override
	public boolean apply(FilteringProductItem ctx) throws FDResourceException {
		if (ctx == null || ctx.getProductModel() == null) {
			return false;
		}

		return invertChecker(ctx.getProductModel().isBackInStock());
	}

	@Override
	public FilterCacheStrategy getCacheStrategy() {
		return FilterCacheStrategy.ERPS;
	}


}
