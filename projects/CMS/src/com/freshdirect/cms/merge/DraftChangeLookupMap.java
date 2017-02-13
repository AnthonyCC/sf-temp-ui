package com.freshdirect.cms.merge;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cmsadmin.domain.DraftChange;
import com.freshdirect.framework.util.log.LoggerFactory;

public class DraftChangeLookupMap {

    private static final Logger LOGGER = LoggerFactory.getInstance(DraftChangeLookupMap.class);
    
    /**
     * Compound key
     * @author segabor
     */
    private static class LookupKey {

        private final ContentKey contentKey;
        private final String attributeName;

        public LookupKey(final ContentKey key, final String attributeName) {
            this.contentKey = key;
            this.attributeName = attributeName;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((attributeName == null) ? 0 : attributeName.hashCode());
            result = prime * result + ((contentKey == null) ? 0 : contentKey.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            LookupKey other = (LookupKey) obj;
            if (attributeName == null) {
                if (other.attributeName != null) {
                    return false;
                }
            } else if (!attributeName.equals(other.attributeName)) {
                return false;
            }
            if (contentKey == null) {
                if (other.contentKey != null) {
                    return false;
                }
            } else if (!contentKey.equals(other.contentKey)) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return "['" + contentKey.getEncoded() + "'; '" + attributeName + "']";
        }
    }

    private final Map<LookupKey, List<DraftChange>> lookupMap = new HashMap<LookupKey, List<DraftChange>>();

    public DraftChangeLookupMap(final Collection<DraftChange> draftChanges) {
        // build lookup map
        
        Set<ContentKey> keys = new HashSet<ContentKey>();
        Set<String> attributeNames = new HashSet<String>();
        int numberOfLists = 0;
        
        for (final DraftChange change : draftChanges) {
            final LookupKey key = new LookupKey(ContentKey.getContentKey(change.getContentKey()), change.getAttributeName());
            
            List<DraftChange> list = lookupMap.get(key);
            if (list == null) {
                list = new ArrayList<DraftChange>();
                lookupMap.put(key, list);
                numberOfLists += 1;
            }
            list.add(change);
            
            // log for reporting
            keys.add(key.contentKey);
            attributeNames.add(key.attributeName);
        }
        
        // DEBUG
        LOGGER.info("Distributed " + draftChanges.size() + " into " + numberOfLists + " lists. Content keys=" + keys.size() + ", attributes=" + attributeNames.size() );
    }

    /**
     * Lookup draft change by content key and optionally by attribute name
     * 
     * @param contentKey content key, the primary selector
     * @param attributeName name of attribute (optional)
     * 
     * @return the first change found in the map
     */
    public DraftChange lookupDraftChange(final ContentKey contentKey, final String attributeName) {
        DraftChange change = null;
        if (attributeName != null) {
            // lookup change by content key and attribute
            change = getDraftChange(new LookupKey(contentKey, attributeName));
        } else {
            // no attribute is given, so pick the first list belonging to content key
            for (Map.Entry<LookupKey, List<DraftChange>> entry : lookupMap.entrySet()) {
                if ( contentKey.equals(entry.getKey())) {
                    change = getDraftChange(entry.getValue());
                    break;
                }
            }
        }
        return change;
    }

    /**
     * Select draft change from the particular change list
     * 
     * @param key
     * @return
     */
    private DraftChange getDraftChange(LookupKey key) {
        return getDraftChange(lookupMap.get(key));
    }

    /**
     * Select draft change from the given list.
     * Currently, it yields the first available item.
     * 
     * @param changes list of changes
     * @return
     */
    private DraftChange getDraftChange(Collection<DraftChange> changes) {
        return (changes != null) ? changes.iterator().next() : null;
    }

}
