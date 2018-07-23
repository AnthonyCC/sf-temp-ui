package com.freshdirect.cms.draft.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.freshdirect.cms.core.converter.ScalarValueConverter;
import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.domain.Relationship;
import com.freshdirect.cms.core.domain.RelationshipCardinality;
import com.freshdirect.cms.core.domain.Scalar;
import com.freshdirect.cms.core.service.ContentTypeInfoService;
import com.freshdirect.cms.draft.domain.DraftChange;
import com.google.common.base.Optional;
import com.google.common.base.Splitter;

@Service
public class DraftApplicatorService {

    public static final char SEPARATOR = '|';

    public static final String EMPTY_LIST_TOKEN = "[]";

    private static final Splitter SPLITTER = Splitter.on(SEPARATOR);

    @Autowired
    private ContentTypeInfoService contentTypeInfoService;

    /**
     * Converts a list of DraftChange-s to ContentNode style Map-s (Map<ContentKey, Map<Attribute, Object>>)
     *
     * Only contains the 'diff' of the draft change, it has to be 'merged' with data from MAIN to get a complete contentnode.
     */
    public Map<ContentKey, Map<Attribute, Object>> convertDraftChanges(List<DraftChange> draftChanges) {
        Assert.notNull(draftChanges);

        Map<ContentKey, Map<Attribute, Object>> nodeMap = new HashMap<ContentKey, Map<Attribute, Object>>();

        for (DraftChange draftChange : draftChanges) {
            ContentKey contentKey = ContentKeyFactory.get(draftChange.getContentKey());
            final String value = draftChange.getValue();

            Map<Attribute, Object> attributeMap = nodeMap.get(contentKey);
            if (attributeMap == null) {
                attributeMap = new HashMap<Attribute, Object>();
                nodeMap.put(contentKey, attributeMap);
            }

            final Attribute attributeDefinition = getAttributeDefinition(contentKey.type, draftChange.getAttributeName());
            if (value == null) {
                attributeMap.put(attributeDefinition, null);
            } else if (attributeDefinition instanceof Relationship) {
                final Relationship relationship = (Relationship) attributeDefinition;
                if (relationship.getCardinality() == RelationshipCardinality.ONE) {
                    List<ContentKey> keys = getContentKeysFromRelationshipValue(value);
                    if (!keys.isEmpty()) {
                        attributeMap.put(relationship, keys.get(0));
                    }
                } else {
                    // LP-226 edge case
                    if (EMPTY_LIST_TOKEN.equals(value)) {
                        attributeMap.put(relationship, new ArrayList<ContentKey>());
                    } else {
                        List<ContentKey> keys = getContentKeysFromRelationshipValue(value);
                        attributeMap.put(relationship, keys);
                    }
                }
            } else if (attributeDefinition instanceof Scalar) {
                Scalar scalarAttribute = (Scalar) attributeDefinition;
                attributeMap.put(scalarAttribute, ScalarValueConverter.deserializeToObject(scalarAttribute, value));
            }
        }
        return nodeMap;
    }

    private Attribute getAttributeDefinition(ContentType type, String attributeName) {
        final Optional<Attribute> optionalAttribute = contentTypeInfoService.findAttributeByName(type, attributeName);
        if (!optionalAttribute.isPresent()) {
            throw new IllegalArgumentException("There is no attribute " + attributeName + " for type " + type);
        }
        return optionalAttribute.get();
    }

    public static List<ContentKey> getContentKeysFromRelationshipValue(String value) {
        if (value == null) {
            return null;
        }

        List<ContentKey> result = new ArrayList<ContentKey>();
        if (!EMPTY_LIST_TOKEN.equals(value)) {
            for (String key : SPLITTER.split(value)) {
                result.add(ContentKeyFactory.get(key));
            }
        }
        return result;
    }
}
