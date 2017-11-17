package com.freshdirect.cms.ui.editor.permission;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentType;

public class CustomPermissionRule {

    private ContentKey contentKey;
    private ContentType nodeType;
    private ContentType relatedNodeType;

    public CustomPermissionRule(ContentKey contentKey, ContentType nodeType, ContentType relatedNodeType) {
        this.contentKey = contentKey;
        this.nodeType = nodeType;
        this.relatedNodeType = relatedNodeType;
    }

    public ContentKey getContentKey() {
        return contentKey;
    }

    public void setContentKey(ContentKey contentKey) {
        this.contentKey = contentKey;
    }

    public ContentType getNodeType() {
        return nodeType;
    }

    public void setNodeType(ContentType nodeType) {
        this.nodeType = nodeType;
    }

    public ContentType getRelatedNodeType() {
        return relatedNodeType;
    }

    public void setRelatedNodeType(ContentType relatedNodeType) {
        this.relatedNodeType = relatedNodeType;
    }

}
