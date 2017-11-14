package com.freshdirect.fdstore.content.browse.filter;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.storeapi.content.AbstractProductItemFilter;
import com.freshdirect.storeapi.content.FilterCacheStrategy;
import com.freshdirect.storeapi.content.FilteringProductItem;

/**
 * Product Availability Filter
 * 
 * @author segabor
 *
 */
public class AvailabilityFilter extends AbstractProductItemFilter {
	public AvailabilityFilter() {
		super("builtin-product-availability-filter", "<orphan>", "Product Availability Filter", false);
	}

	@Override
	public boolean apply(FilteringProductItem prod) throws FDResourceException {
		return invertChecker(prod.getProductModel() != null && prod.getProductModel().isFullyAvailable());
	}

	@Override
	public FilterCacheStrategy getCacheStrategy() {
		return FilterCacheStrategy.ERPS;
	}

}
