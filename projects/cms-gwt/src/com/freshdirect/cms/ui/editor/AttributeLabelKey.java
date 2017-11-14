package com.freshdirect.cms.ui.editor;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentType;

public class AttributeLabelKey {

    private ContentType type;
    private Attribute attribute;

    public AttributeLabelKey(ContentType type, Attribute attribute) {
        this.type = type;
        this.attribute = attribute;
    }

    public static AttributeLabelKey keyOf(ContentType type, Attribute attribute) {
        return new AttributeLabelKey(type, attribute);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((attribute == null) ? 0 : attribute.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
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
        AttributeLabelKey other = (AttributeLabelKey) obj;
        if (attribute == null) {
            if (other.attribute != null) {
                return false;
            }
        } else if (!attribute.equals(other.attribute)) {
            return false;
        }
        if (type != other.type) {
            return false;
        }
        return true;
    }
}
