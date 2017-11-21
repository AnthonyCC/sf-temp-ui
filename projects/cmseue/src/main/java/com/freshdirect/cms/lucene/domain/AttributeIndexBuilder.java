package com.freshdirect.cms.lucene.domain;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentType;

/**
 * Builder class for AttributeIndex, to make life easier
 *
 * @author tsoltesz
 *
 */
public class AttributeIndexBuilder {

    private AttributeIndex attributeIndex;

    public AttributeIndexBuilder(ContentType contentType, Attribute attribute) {
        attributeIndex = new AttributeIndex(contentType, attribute);
    }

    public AttributeIndexBuilder withText(boolean text) {
        attributeIndex.setText(text);
        return this;
    }

    public AttributeIndexBuilder withSpelled(boolean spelled) {
        attributeIndex.setSpelled(spelled);
        return this;
    }

    public AttributeIndexBuilder withRecurseParent(boolean recurseParent) {
        attributeIndex.setRecurseParent(recurseParent);
        return this;
    }

    public AttributeIndexBuilder withRelationshipAttributeName(String relationshipAttributeName) {
        attributeIndex.setRelationshipAttributeName(relationshipAttributeName);
        return this;
    }

    public AttributeIndex build() {
        return attributeIndex;
    }
}
