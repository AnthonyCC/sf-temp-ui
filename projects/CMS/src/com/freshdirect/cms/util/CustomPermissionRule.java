package com.freshdirect.cms.util;

import com.freshdirect.cms.ContentType;

public class CustomPermissionRule {

    private String contentKey;
    private ContentType nodeType;
    private ContentType relatedNodeType;

    public CustomPermissionRule(String contentKey, ContentType nodeType, ContentType relatedNodeType) {
        this.contentKey = contentKey;
        this.nodeType = nodeType;
        this.relatedNodeType = relatedNodeType;
    }

    public String getContentKey() {
        return contentKey;
    }

    public void setContentKey(String contentKey) {
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
