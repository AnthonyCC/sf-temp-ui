package com.freshdirect.smartstore.filter;

import java.util.Collection;

import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * Filter out items which are on the cart already.
 * 
 * @author zsombor
 *
 */
public final class ExclusionFilter extends ContentFilter {
    private static final Logger LOGGER = LoggerFactory.getInstance(ExclusionFilter.class);
    
	private final Collection<ContentKey> cartItems;

    public ExclusionFilter(Collection<ContentKey> cartItems) {
        this.cartItems = cartItems;
    }

    public ContentKey filter(ContentKey key) {
    	boolean exclude = key == null || cartItems.contains(key);
    	if (exclude)
    		LOGGER.debug("excluded: " + key);
    		
		return exclude ? null : key;
    }
}