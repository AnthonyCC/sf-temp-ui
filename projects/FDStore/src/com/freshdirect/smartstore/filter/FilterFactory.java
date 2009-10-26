package com.freshdirect.smartstore.filter;

import java.util.Collection;

import com.freshdirect.cms.ContentKey;

public class FilterFactory {
    private final static ProductFilter AVAILABLE_ITEMS_W_ALTS = new ProductAvailabilityFilterEx();
    private final static ProductFilter AVAILABLE_ITEMS = new ProductAvailabilityFilter();
    
    private final static ProductFilter EXCLUDED_ITEMS = new ExcludedItemFilter();

	public static ProductFilter createStandardFilter() {
		return createStandardFilter(null, true);
	}

	public static ProductFilter createStandardFilter(Collection<ContentKey> cartItems) {
		return createStandardFilter(cartItems, true);
	}

	public static ProductFilter createStandardFilter(boolean useAlternatives) {
		return createStandardFilter(null, useAlternatives);
	}


	public static ProductFilter createStandardFilter(Collection<ContentKey> cartItems, boolean useAlternatives) {
		ArrayFilter filter = new ArrayFilter();
		
		filter.addFilter(useAlternatives ? AVAILABLE_ITEMS_W_ALTS : AVAILABLE_ITEMS);
		filter.addFilter(EXCLUDED_ITEMS);
		filter.addFilter(new UnicityFilter());
		if (cartItems != null)
			filter.addFilter(new CartItemsFilter(cartItems));
		
		return filter;
	}
}
