package com.freshdirect.smartstore.filter;

import java.util.Collection;

import com.freshdirect.cms.ContentKey;

public class FilterFactory {
    private final static ContentFilter AVAILABLE_ITEMS_W_ALTS = new ProductAvailabilityFilterEx();
    private final static ContentFilter AVAILABLE_ITEMS = new ProductAvailabilityFilter();
    
    private static FilterFactory INSTANCE = null;
    
    public synchronized static final FilterFactory getInstance() {
    	if (INSTANCE == null) {
    		INSTANCE = new FilterFactory();
    	}
    	
    	return INSTANCE;
    }
	
	public synchronized static void mockInstance(FilterFactory newInstance) {
		INSTANCE = newInstance;
	}
    
    protected FilterFactory() {
    }
    
	public ContentFilter createFilter(Collection<ContentKey> exclusions, boolean useAlternatives) {
		ArrayFilter filter = new ArrayFilter();
		
		if (exclusions != null)
			filter.addFilter(new ExclusionFilter(exclusions));
		filter.addFilter(useAlternatives ? AVAILABLE_ITEMS_W_ALTS : AVAILABLE_ITEMS);
		filter.addFilter(new UnicityFilter());
		
		return filter;
	}
}
