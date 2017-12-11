package com.freshdirect.storeapi;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;

@CmsLegacy
public class ContentNode implements ContentNodeI {

    private static final long serialVersionUID = 4432339440407611251L;

    private final ContentKey contentKey;

    private final Map<Attribute, Object> payload;

    private final Map<String, AttributeI> legacyPayload = new HashMap<String, AttributeI>();

    private final Set<ContentKey> childKeys;

    public ContentNode(ContentKey contentKey, Map<Attribute, Object> values, Set<ContentKey> childKeys) {
        this.contentKey = contentKey;
        this.payload = values;
        this.childKeys = childKeys;

        buildLegacyPayload();
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
        return legacyPayload.get(name);
    }

    @Override
    public Object getAttributeValue(String name) {
        AttributeI legacyAttribute = legacyPayload.get(name);

        return legacyAttribute != null ? legacyAttribute.getValue() : null;
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

    private void buildLegacyPayload() {
        for (Map.Entry<Attribute, Object> payloadEntry : payload.entrySet()) {
            AttributeI legacyAttribute = buildLegacyAttribute(payloadEntry.getKey(), payloadEntry.getValue());
            legacyPayload.put(legacyAttribute.getName(), legacyAttribute);
        }
    }
}
