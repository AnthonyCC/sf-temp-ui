package com.freshdirect.cms.ui.editor.publish.entity;

import java.io.Serializable;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class StorePublishMessageId implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 8780247120753629100L;

    private int sortOrder;
    private Long publishId;
    
    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Long getPublishId() {
        return publishId;
    }

    public void setPublishId(Long publishId) {
        this.publishId = publishId;
    }
    
    @Override
    public int hashCode() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(sortOrder).append(publishId);
        return hashCodeBuilder.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof StorePublishMessageId) {
            StorePublishMessageId other = (StorePublishMessageId) obj;
            return (this.publishId == other.getPublishId()) && (this.sortOrder == other.sortOrder);
        }
        return false;
    }

    @Override
    public String toString() {
        return "StorePublishMessageId [publishId=" + publishId + ", sortOrder=" + sortOrder + "]";
    }

}
