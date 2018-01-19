package com.freshdirect.storeapi;

import java.util.Map;
import java.util.Set;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
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
    public Attribute getAttribute(String name) {
        return typeInfoService.findAttributeByName(this.contentKey.type, name).orNull();
    }

    @Override
    public Object getAttributeValue(Attribute definition) {
        return payload.get(definition);
    }

    @Override
    public Object getAttributeValue(String name) {
        Optional<Attribute> selectedAttribute = typeInfoService.findAttributeByName(this.contentKey.type, name);
        if (selectedAttribute.isPresent()) {
            return payload.get(selectedAttribute.get());
        }
        return null;
    }

    @Override
    public boolean setAttributeValue(String name, Object value) {
        return false;
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
}
