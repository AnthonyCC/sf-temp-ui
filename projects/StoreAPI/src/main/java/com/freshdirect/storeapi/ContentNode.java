package com.freshdirect.storeapi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.service.ContentTypeInfoService;
import com.google.common.base.Optional;

@CmsLegacy
public class ContentNode implements ContentNodeI {

    private static final long serialVersionUID = 4432339440407611251L;

    private final ContentKey contentKey;

    private final Map<Attribute, Object> payload;

    private final Set<ContentKey> childKeys;

    private final ContentTypeInfoService typeInfoService;

    public ContentNode(ContentKey contentKey, Map<Attribute, Object> values, Set<ContentKey> childKeys, ContentTypeInfoService typeInfoService) {
        this.contentKey = contentKey;
        this.payload = values;
        this.childKeys = childKeys;
        this.typeInfoService = typeInfoService;
    }

    @Override
    public ContentKey getKey() {
        return contentKey;
    }

    public Map<Attribute, Object> getPayload() {
        return payload;
    }

    @Override
    public AttributeI getAttribute(String name) {
        AttributeI result = null;
        final Attribute selectedAttribute = findAttributeByName(name);

        if (selectedAttribute != null) {
            final Object value = payload.get(selectedAttribute);
            result = buildLegacyAttribute(selectedAttribute, value);
        }

        return result;
    }

    @Override
    public Object getAttributeValue(String name) {
        Attribute selectedAttribute = findAttributeByName(name);
        return selectedAttribute != null ? payload.get(selectedAttribute) : null;
    }

    @Override
    public boolean setAttributeValue(String name, Object value) {
        return false;
    }

    @Override
    public Map<String, AttributeI> getAttributes() {
        Map<String, AttributeI> result = new HashMap<String, AttributeI>();

        for (Map.Entry<Attribute, Object> entry : payload.entrySet()) {
            final Attribute attribute = entry.getKey();
            final Object value = entry.getValue();
            AttributeI legacyAttribute = buildLegacyAttribute(attribute, value);
            result.put(attribute.getName(), legacyAttribute);
        }

        return result;
    }

    @Override
    public Set<ContentKey> getChildKeys() {
        return childKeys;
    }

    @Override
    public String getLabel() {
        return contentKey.id;
    }

    @Override
    public ContentNodeI copy() {
        return this;
    }

    @Override
    public String toString() {
        return "ContentNode [contentKey=" + contentKey + ", payload=" + payload + ", childKeys=" + childKeys + "]";
    }

    private Attribute findAttributeByName(String attributeName) {
        Optional<Attribute> selectedAttribute = Optional.absent();
        if (attributeName != null) {
            List<ContentType> typesToLookup = new ArrayList<ContentType>();
            typesToLookup.add(contentKey.type);
            typesToLookup.addAll(typeInfoService.getReachableContentTypes(contentKey.type));

            for (ContentType type : typesToLookup) {
                selectedAttribute = typeInfoService.findAttributeByName(type, attributeName);
                if (selectedAttribute.isPresent()) {
                    return selectedAttribute.get();
                }
            }
        }
        return null;
    }

    @SuppressWarnings("serial")
    private AttributeI buildLegacyAttribute(final Attribute attribute, final Object value) {
        return new AttributeI() {
            @Override
            public String getName() {
                return attribute.getName();
            }

            @Override
            public Object getValue() {
                return value;
            }

            @Override
            public void setValue(Object o) {
            }

            @Override
            public Attribute getDefinition() {
                return attribute;
            }
        };
    }
}
