/**
 * 
 */
package com.freshdirect.smartstore.scoring;

import java.util.Collection;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.smartstore.filter.ArrayFilter;
import com.freshdirect.smartstore.filter.ContentFilter;
import com.freshdirect.smartstore.filter.FilterFactory;

/**
 * This filter factory returns empty filters, which doesn't filter anything out.
 *  
 * @author zsombor
 *
 */
public final class MockFilterFactory extends FilterFactory {
    @Override
    public ContentFilter createFilter(Collection<ContentKey> exclusions, boolean useAlternatives, boolean showTempUnavailable) {
    	return new ArrayFilter() {
    	    
    	};
    }
}