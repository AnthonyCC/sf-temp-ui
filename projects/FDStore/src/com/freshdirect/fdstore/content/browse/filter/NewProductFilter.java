package com.freshdirect.fdstore.content.browse.filter;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.storeapi.content.AbstractProductItemFilter;
import com.freshdirect.storeapi.content.FilterCacheStrategy;
import com.freshdirect.storeapi.content.FilteringProductItem;
import com.freshdirect.storeapi.content.ProductFilterModel;

public class NewProductFilter extends AbstractProductItemFilter {

	public NewProductFilter(ProductFilterModel model, String parentId) {
		super(model, parentId);
	}
	
	public NewProductFilter(String id, String parentId, String name) { //'virtual' newProductFilter for search page 
		super(id, parentId, name);
	}
	
	@Override
	public boolean apply(FilteringProductItem ctx) throws FDResourceException {
		if (ctx == null || ctx.getProductModel() == null) {
			return false;
		}
		
		return invertChecker(ctx.getProductModel().isNew());
	}

	@Override
	public FilterCacheStrategy getCacheStrategy() {
		return FilterCacheStrategy.ERPS;
	}

}
