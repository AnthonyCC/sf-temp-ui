package com.freshdirect.cms.ui.editor.domain;

import java.io.Serializable;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;

public class ContentAttributeKey implements Serializable {

    private static final long serialVersionUID = 8255293394994136573L;

    private final ContentKey contentKey;
    private final String attributeName;

    public ContentAttributeKey(ContentKey contentKey, String attributeName) {
        this.contentKey = contentKey;
        this.attributeName = attributeName;
    }

    public ContentAttributeKey(ContentKey contentKey, Attribute attributeDef) {
        this(contentKey, attributeDef.getName());
    }

    public ContentKey getContentKey() {
        return contentKey;
    }

    public String getAttributeName() {
        return attributeName;
    }

    @Override
    public String toString() {
        return contentKey.toString() + "|" + attributeName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((attributeName == null) ? 0 : attributeName.hashCode());
        result = prime * result + ((contentKey == null) ? 0 : contentKey.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ContentAttributeKey other = (ContentAttributeKey) obj;
        if (attributeName == null) {
            if (other.attributeName != null) {
                return false;
            }
        } else if (!attributeName.equals(other.attributeName)) {
            return false;
        }
        if (contentKey == null) {
            if (other.contentKey != null) {
                return false;
            }
        } else if (!contentKey.equals(other.contentKey)) {
            return false;
        }
        return true;
    }

}
