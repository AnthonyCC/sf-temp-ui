package com.freshdirect.cms.persistence.entity;

import com.freshdirect.cms.core.domain.ContentKey;

public class NavTreeElement {

    private ContentKey parentContentKey;
    private ContentKey childContentKey;

    public NavTreeElement(ContentKey childContentKey, ContentKey parentContentKey) {
        this.childContentKey = childContentKey;
        this.parentContentKey = parentContentKey;
    }

    public ContentKey getChildContentKey() {
        return childContentKey;
    }

    public void setChildContentKey(ContentKey childContentKey) {
        this.childContentKey = childContentKey;
    }

    public ContentKey getParentContentKey() {
        return parentContentKey;
    }

    public void setParentContentKey(ContentKey parentContentKey) {
        this.parentContentKey = parentContentKey;
    }
}
