package com.freshdirect.cms.ui.editor.publish.feed.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
@Table(name = "publishxmessages")
@IdClass(FeedPublishMessageId.class)
public class FeedPublishMessageEntity {

    @Id
    @Column(name = "sort_order")
    private int sortOrder;

    @Id
    @Column(name = "publish_id")
    private Long publishId;

    @Column(nullable = false)
    private Date timestamp;

    @Column(nullable = false)
    private int severity;
    private String message;

    @Column(name = "content_id")
    private String contentId;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "store_id")
    private String storeId;

    private String task;

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

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public FeedPublishMessageSeverity getSeverity() {
        return FeedPublishMessageSeverity.parse(severity);
    }

    public void setSeverity(FeedPublishMessageSeverity severity) {
        this.severity = severity.getLevel();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[Message=").append(message).append("]");
        stringBuilder.append("[Severity=").append(severity).append("]");
        stringBuilder.append("[ContentType=").append(contentType).append("]");
        stringBuilder.append("[ContentId=").append(contentId).append("]");
        stringBuilder.append("[Store=").append(storeId).append("]");
        stringBuilder.append("[Task=").append(task).append("]");
        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FeedPublishMessageEntity) {
            FeedPublishMessageEntity other = (FeedPublishMessageEntity) obj;
            EqualsBuilder equalsBuilder = new EqualsBuilder();
            equalsBuilder.append(message, other.getMessage());
            equalsBuilder.append(publishId, other.getPublishId());
            equalsBuilder.append(task, other.getTask());
            equalsBuilder.append(severity, other.getSeverity());
            equalsBuilder.append(storeId, other.getStoreId());
            equalsBuilder.append(sortOrder, other.getSortOrder());
            return equalsBuilder.isEquals();
        }
        return false;
    }

    @Override
    public int hashCode() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(publishId).append(storeId).append(task).append(message).append(severity).append(sortOrder).append(timestamp).append(contentId).append(contentType);
        return hashCodeBuilder.hashCode();
    }

}
