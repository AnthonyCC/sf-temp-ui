package com.freshdirect.cms.draft.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.freshdirect.cms.core.converter.SerializedScalarValueToObjectConverter;
import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.Relationship;
import com.freshdirect.cms.core.domain.RelationshipCardinality;
import com.freshdirect.cms.core.domain.Scalar;
import com.freshdirect.cms.core.service.ContentTypeInfoService;
import com.freshdirect.cms.draft.domain.DraftChange;
import com.google.common.base.Optional;

@Service
public class DraftChangeToContentNodeApplicator {

    public static final String SEPARATOR = "|";

    @Autowired
    private ContentTypeInfoService contentTypeInfoService;

    @Autowired
    private SerializedScalarValueToObjectConverter serializedValueConverter;

    public Map<Attribute, Object> applyDraftChangeToNode(DraftChange draftChange, Map<Attribute, Object> nodeAttributesWithValues) {
        final String attributeName = draftChange.getAttributeName();
        ContentKey draftContentKey = ContentKeyFactory.get(draftChange.getContentKey());
        Optional<Attribute> optionalAttribute = contentTypeInfoService.findAttributeByName(draftContentKey.type, attributeName);

        if (optionalAttribute.isPresent()) {
            Attribute attributeDefinition = optionalAttribute.get();

            if (draftChange.getValue() == null) {
                nodeAttributesWithValues.put(attributeDefinition, null);
            } else {

                if (attributeDefinition instanceof Relationship) {
                    List<ContentKey> keys = getContentKeysFromRelationshipValue((Relationship) attributeDefinition, draftChange.getValue());
                    if (((Relationship) attributeDefinition).getCardinality() == RelationshipCardinality.ONE) {
                        nodeAttributesWithValues.put(attributeDefinition, keys.isEmpty() ? null : keys.get(0));
                    } else {
                        // This sets the value of OneToManyRelation fields, based on the draftchange attribute.
                        nodeAttributesWithValues.put(attributeDefinition, keys);
                    }
                } else if (attributeDefinition instanceof Scalar) {
                    Scalar scalarAttribute = (Scalar) attributeDefinition;
                    Object value = serializedValueConverter.convert(scalarAttribute, draftChange.getValue());
                    nodeAttributesWithValues.put(attributeDefinition, value);
                }
            }
        } else {
            throw new IllegalArgumentException("There is no attribute " + attributeName + " for type " + draftContentKey.type);
        }
        return nodeAttributesWithValues;
    }

    public List<ContentKey> getContentKeysFromRelationshipValue(Relationship relationship, String value) {
        List<ContentKey> result = new ArrayList<ContentKey>();
        if (relationship.getCardinality() == RelationshipCardinality.ONE) {
            result.add(ContentKeyFactory.get(value));
        } else {
            String[] changedKeys = StringUtils.split(value, SEPARATOR);

            if (changedKeys != null) {
                for (int i = 0; i < changedKeys.length; i++) {
                    result.add(ContentKeyFactory.get(changedKeys[i]));
                }
            }

        }
        return result;
    }

    public Map<ContentKey, Map<Attribute, Object>> createContentNodeFromDraftChange(DraftChange draftChange) {
        ContentKey key = ContentKeyFactory.get(draftChange.getContentKey());
        Map<ContentKey, Map<Attribute, Object>> node = new HashMap<ContentKey, Map<Attribute, Object>>();
        Map<Attribute, Object> attributesWithValues = new HashMap<Attribute, Object>();
        node.put(key, applyDraftChangeToNode(draftChange, attributesWithValues));
        return node;
    }
}
