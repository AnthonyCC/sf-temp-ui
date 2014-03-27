package com.freshdirect.fdstore.content.browse.filter;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.AbstractProductItemFilter;
import com.freshdirect.fdstore.content.BrandModel;
import com.freshdirect.fdstore.content.FilterCacheStrategy;
import com.freshdirect.fdstore.content.FilteringProductItem;
import com.freshdirect.fdstore.content.ProductFilterModel;

public class BrandFilter extends AbstractProductItemFilter {
	protected BrandModel brand;
	
	public BrandFilter(ProductFilterModel model, String parentId) {
		super(model, parentId);
		
		this.brand = model.getBrand();
	}

	@Override
	public boolean apply(FilteringProductItem ctx) throws FDResourceException {
		if (ctx == null || ctx.getProductModel() == null) {
			return false;
		}
		
		return invertChecker(ctx.getProductModel().getBrands().contains(brand));
	}

	@Override
	public FilterCacheStrategy getCacheStrategy() {
		return FilterCacheStrategy.CMS_ONLY;
	}

}
