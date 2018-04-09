package com.freshdirect.cms.core.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.Relationship;
import com.freshdirect.cms.core.domain.RelationshipCardinality;
import com.google.common.collect.Sets;

/**
 * Calculate 'parent index': child key to parent keys relationship
 */
public final class ParentIndexBuilder {

    private ParentIndexBuilder() {
    }

    /**
     * Creates the parentKeysMap from the given nodes
     *
     * @param nodes
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Map<ContentKey, Set<ContentKey>> createParentKeysMap(Map<ContentKey, Map<Attribute, Object>> nodes) {
        Map<ContentKey, Set<ContentKey>> parentIndex = new HashMap<ContentKey, Set<ContentKey>>();
        for (Map.Entry<ContentKey, Map<Attribute, Object>> node : nodes.entrySet()) {
            ContentKey parentKey = node.getKey();
            for (Map.Entry<Attribute, Object> attributes : node.getValue().entrySet()) {
                Attribute attribute = attributes.getKey();
                Object value = attributes.getValue();
                if (value != null && attribute instanceof Relationship && ((Relationship) attribute).isNavigable()) {
                    if (RelationshipCardinality.ONE == ((Relationship) attribute).getCardinality()) {
                        addParentToIndex(parentKey, (ContentKey) value, parentIndex);
                    } else {
                        for (ContentKey childKey : (List<ContentKey>) value) {
                            addParentToIndex(parentKey, childKey, parentIndex);
                        }
                    }
                }
            }
        }
        return parentIndex;
    }

    private static void addParentToIndex(ContentKey parentKey, ContentKey childKey, Map<ContentKey, Set<ContentKey>> parentIndex) {
        if (parentIndex.containsKey(childKey)) {
            parentIndex.get(childKey).add(parentKey);
        } else {
            parentIndex.put(childKey, Sets.newHashSet(parentKey));
        }
    }
}
