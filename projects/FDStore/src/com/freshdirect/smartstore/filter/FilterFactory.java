package com.freshdirect.smartstore.filter;

import java.util.Collection;

public class FilterFactory {
    private final static ProductFilter AVAILABLE_ITEMS = new ProductAvailabilityFilter();
    
    private final static ProductFilter EXCLUDED_ITEMS = new ExcludedItemFilter();

	public static ProductFilter createStandardFilter() {
		return createStandardFilter(null);
	}
    
	public static ProductFilter createStandardFilter(Collection cartItems) {
		ArrayFilter filter = new ArrayFilter();
		
		filter.addFilter(AVAILABLE_ITEMS);
		filter.addFilter(EXCLUDED_ITEMS);
		filter.addFilter(new UnicityFilter());
		if (cartItems != null)
			filter.addFilter(new CartItemsFilter(cartItems));
		
		return filter;
	}
}
