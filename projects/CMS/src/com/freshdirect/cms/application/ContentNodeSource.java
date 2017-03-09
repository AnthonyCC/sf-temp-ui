package com.freshdirect.cms.application;

import java.util.Map;
import java.util.Set;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;

/**
 * Basic content node provider interface
 * 
 * @author segabor
 *
 */
public interface ContentNodeSource {

    /**
     * Get keys that have a navigable relationship TO the given key.
     * 
     * @param key
     *            ContentKey (never null)
     * @return Set of ContentKey (never null)
     */
    Set<ContentKey> getParentKeys(ContentKey contentKey, DraftContext draftContext);

    /**
     * @return ContentNodeI (or null if not found, or type is not supported)
     */
    ContentNodeI getContentNode(final ContentKey contentKey, final DraftContext draftContext);

    /**
     * Retrieve multiple nodes.
     * 
     * @param keys
     *            Set of ContentKey
     * @return Map of ContentKey -> ContentNodeI (never null)
     */
    public Map<ContentKey, ContentNodeI> getContentNodes(Set<ContentKey> keys, DraftContext draftContext);

}
