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

            Map<Attribute, Object> attributeMap = nodeMap.get(contentKey);
            if (attributeMap == null) {
                attributeMap = new HashMap<Attribute, Object>();
                nodeMap.put(contentKey, attributeMap);
            }

            final Attribute attributeDefinition = getAttributeDefinition(contentKey.type, draftChange.getAttributeName());
            if (attributeDefinition instanceof Relationship) {
                Relationship relationship = (Relationship) attributeDefinition;
                List<ContentKey> keys = getContentKeysFromRelationshipValue(draftChange.getValue());
                if (relationship.getCardinality() == RelationshipCardinality.ONE) {
                    attributeMap.put(relationship, keys.isEmpty() ? null : keys.get(0));
                } else {
                    attributeMap.put(relationship, keys);
                }
            } else if (attributeDefinition instanceof Scalar) {
                Scalar scalarAttribute = (Scalar) attributeDefinition;
                attributeMap.put(scalarAttribute, ScalarValueConverter.deserializeToObject(scalarAttribute, draftChange.getValue()));
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
        List<ContentKey> result = new ArrayList<ContentKey>();
        if (value != null) {
            for (String key : SPLITTER.split(value)) {
                result.add(ContentKeyFactory.get(key));
            }
        }
        return result;
    }
}
