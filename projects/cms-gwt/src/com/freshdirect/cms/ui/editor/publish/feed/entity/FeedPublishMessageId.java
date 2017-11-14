package com.freshdirect.cms.ui.editor.publish.feed.entity;

import java.io.Serializable;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class FeedPublishMessageId implements Serializable {

    private static final long serialVersionUID = 7403296321979851158L;

    private int sortOrder;
    private Long publishId;

    public FeedPublishMessageId() {
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setPublishId(Long publishId) {
        this.publishId = publishId;
    }

    public Long getPublishId() {
        return publishId;
    }

    @Override
    public int hashCode() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(sortOrder).append(publishId);
        return hashCodeBuilder.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FeedPublishMessageId) {
            FeedPublishMessageId other = (FeedPublishMessageId) obj;
            return (this.publishId.equals(other.getPublishId())) && (this.sortOrder == other.sortOrder);
        }
        return false;
    }

    @Override
    public String toString() {
        return "FeedPublishMessageId [publishId=" + publishId + ", sortOrder=" + sortOrder + "]";
    }

}
