package com.freshdirect.smartstore.filter;

import com.freshdirect.cms.ContentKey;

/**
 * Filtering predicate for product models
 * 
 * @author zsombor
 */
public abstract class ContentFilter {
	protected ContentFilter() {
	}

    public abstract ContentKey filter(ContentKey key);
    
    public void reset() {
    }
}