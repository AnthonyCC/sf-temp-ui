package com.freshdirect.cms.util;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.DraftContext;

/**
 * Utility that collects all available root keys for a content node
 * 
 * @author segabor
 *
 */
public final class TopKeyCollectorUtility {
    private TopKeyCollectorUtility() {};

    /**
     * Collect topmost parent nodes of the given node
     * by traversing through navigable relationships upwards
     * 
     * @param node
     * @param svc
     * @return
     */
    public static Set<ContentKey> collect(final ContentKey key, final ContentServiceI svc, DraftContext draftContext) {
        if (key == null) {
            return Collections.emptySet();
        }

        final Set<ContentKey> rootKeys = new HashSet<ContentKey>();
        final Set<ContentKey> visitedKeys = new HashSet<ContentKey>();
        
        collectParentKeys(key, rootKeys, visitedKeys, svc, draftContext );
        
        return rootKeys;
    }


    /**
     * Collect parent keys of a node recursively
     * 
     * @param key Key of content node
     * @param rootKeys Result of collection
     * @param visitedKeys Collection that holds already visited keys
     * @param svc content service
     */
    private static void collectParentKeys(final ContentKey key, final Set<ContentKey> rootKeys, final Set<ContentKey> visitedKeys, final ContentServiceI svc, DraftContext draftContext ) {
        if (visitedKeys.contains(key)) {
            return;
        } else {
            visitedKeys.add(key);
        }

        // check parent keys
        Set<ContentKey> parentKeys = svc.getParentKeys(key, draftContext);

        // key is a root
        if (parentKeys == null || parentKeys.isEmpty()) {
            rootKeys.add(key);
            return;
        }

        // climb the navigation tree
        for (final ContentKey pKey : parentKeys) {
            collectParentKeys(pKey, rootKeys, visitedKeys, svc, draftContext);
        }
    }
}
