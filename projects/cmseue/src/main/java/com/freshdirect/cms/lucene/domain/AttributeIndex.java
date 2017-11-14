package com.freshdirect.cms.lucene.domain;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentType;

/**
 * Configuration object to instruct the indexer to index a named attribute of nodes with a particular {@link com.freshdirect.cms.core.domain.ContentType}.
 */
public class AttributeIndex extends ContentIndex {

    private static final long serialVersionUID = 3283380751457964164L;

    private Attribute attribute;

    private String relationshipAttributeName;

    private boolean text;

    private boolean spelled;

    private boolean recurseParent;

    public AttributeIndex(ContentType contentType, Attribute attribute) {
        super(contentType);
        this.attribute = attribute;
    }

    public Attribute getAttribute() {
        return this.attribute;
    }

    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
    }

    public String getRelationshipAttributeName() {
        return relationshipAttributeName;
    }

    public void setRelationshipAttributeName(String relationshipAttributeName) {
        this.relationshipAttributeName = relationshipAttributeName;
    }

    public boolean isText() {
        return text;
    }

    public void setText(boolean text) {
        this.text = text;
    }

    public boolean isSpelled() {
        return spelled;
    }

    public void setSpelled(boolean spelled) {
        this.spelled = spelled;
    }

    public boolean isRecurseParent() {
        return recurseParent;
    }

    public void setRecurseParent(boolean recurseParent) {
        this.recurseParent = recurseParent;
    }

    public String getCanonicalName() {
        StringBuilder buf = new StringBuilder();
        buf.append(getContentType());
        buf.append(".");
        buf.append(attribute.getName());
        if (relationshipAttributeName != null) {
            buf.append("#");
            buf.append(relationshipAttributeName);
        }
        return buf.toString();
    }

    @Override
    public String toString() {
        return "AttributeIndex [contentType=" + getContentType()
                + ", attributeName=" + attribute.getName()
                + (relationshipAttributeName != null ? ", relationshipAttributeName=" + relationshipAttributeName : "")
                + ", text=" + text
                + ", spelled=" + spelled
                + ", recurseParent=" + recurseParent + "]";
    }
}
