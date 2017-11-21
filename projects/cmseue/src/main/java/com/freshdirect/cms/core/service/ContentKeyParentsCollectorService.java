package com.freshdirect.cms.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.domain.Relationship;
import com.freshdirect.cms.core.domain.RelationshipCardinality;
import com.google.common.collect.Sets;

/**
 * Service to provide content key to parents relationship
 *
 * @author segabor
 *
 */
@Service
public class ContentKeyParentsCollectorService {

    @Autowired
    private ContentTypeInfoService contentTypeInfoService;

    @Autowired(required = false)
    private ContentProvider contentProvider;

    public Map<ContentKey, Set<ContentKey>> createParentKeysMap() {
        Assert.notNull(contentProvider, "Content Provider must be wired in order to use this method!");

        Map<ContentKey, Set<ContentKey>> result = new HashMap<ContentKey, Set<ContentKey>>();

        final Map<ContentType, List<Relationship>> relationshipsOfTypes = contentTypeInfoService.selectAllNavigableRelationships();

        for (ContentType type : relationshipsOfTypes.keySet()) {
            final List<Relationship> relationships = relationshipsOfTypes.get(type);
            final Set<ContentKey> contentKeys = contentProvider.getContentKeysByType(type);

            Map<ContentKey, Map<Attribute, Object>> map = contentProvider.getAttributesForContentKeys(new ArrayList<ContentKey>(contentKeys), relationships);
            for (Map.Entry<ContentKey, Map<Attribute, Object>> entry : map.entrySet()) {
                Set<ContentKey> childKeys = findChildKeys(entry.getValue());
                assignParentsToContentKey(entry.getKey(), childKeys, result);

            }
        }

        return result;
    }

    /**
     * Creates the parentKeysMap from the given nodes
     *
     * @param nodes
     * @return
     */
    public Map<ContentKey, Set<ContentKey>> createParentKeysMap(Map<ContentKey, Map<Attribute, Object>> nodes) {
        Map<ContentKey, Set<ContentKey>> result = new HashMap<ContentKey, Set<ContentKey>>();
        final Map<ContentType, List<Relationship>> relationshipsOfTypes = contentTypeInfoService.selectAllNavigableRelationships();

        for (ContentType type : relationshipsOfTypes.keySet()) {
            final List<Relationship> relationships = relationshipsOfTypes.get(type);
            final List<ContentKey> contentKeysByType = new ArrayList<ContentKey>();
            for (ContentKey contentKey : nodes.keySet()) {
                if (contentKey.type == type) {
                    contentKeysByType.add(contentKey);
                }
            }
            for (ContentKey contentKey : contentKeysByType) {
                Set<ContentKey> childKeys = findChildKeys(getAttributeValues(nodes.get(contentKey), relationships));
                assignParentsToContentKey(contentKey, childKeys, result);
            }

        }

        return result;
    }

    private Map<Attribute, Object> getAttributeValues(Map<Attribute, Object> node, List<Relationship> relationships) {
        Map<Attribute, Object> selectedRelationships = new HashMap<Attribute, Object>();
        for (Attribute attribute : node.keySet()) {
            if (relationships.contains(attribute)) {
                selectedRelationships.put(attribute, node.get(attribute));
            }
        }
        return selectedRelationships;
    }

    /**
     *
     *
     * @param parentKey
     *            content key the parent keys assigned to
     * @param childKeys
     *            child keys found in relationship values bound to parentKey
     * @param parentKeysMap
     *            child to parent keys mapping
     */
    private void assignParentsToContentKey(ContentKey parentKey, Set<ContentKey> childKeys, Map<ContentKey, Set<ContentKey>> parentKeysMap) {
        for (ContentKey childKey : childKeys) {
            if (parentKeysMap.keySet().contains(childKey)) {
                parentKeysMap.get(childKey).add(parentKey);
            } else {
                parentKeysMap.put(childKey, Sets.newHashSet(parentKey));
            }
        }
    }

    /**
     * Collect all reachable keys of a content node from the given attribute values map
     *
     * @param attributeValues
     * @return
     */
    @SuppressWarnings("unchecked")
    private Set<ContentKey> findChildKeys(Map<Attribute, Object> attributeValues) {
        Set<ContentKey> contentKeys = new HashSet<ContentKey>();
        for (Attribute attribute : attributeValues.keySet()) {
            Object value = attributeValues.get(attribute);
            if (value != null) {
                if (RelationshipCardinality.ONE == ((Relationship) attribute).getCardinality()) {
                    contentKeys.add((ContentKey) value);
                } else {
                    contentKeys.addAll((List<ContentKey>) value);
                }
            }
        }

        return contentKeys;
    }
}
