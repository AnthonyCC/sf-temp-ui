package com.freshdirect.cms.application;

import com.freshdirect.cms.ContentKey;

/**
 * Store extension to content node source
 * 
 * @author segabor
 *
 */
public interface StoreContentSource extends ContentNodeSource {
    
    /**
     * @return the content key of the root (store) node
     */
    ContentKey getStoreKey();
    
    /**
     * Returns the primary home (the 'default' parent category)
     * of the product node in the given store context.
     */
    ContentKey getPrimaryHomeKey(ContentKey aKey, final DraftContext draftContext);
}
