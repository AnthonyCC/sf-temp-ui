package com.freshdirect.cms.ui.editor.publish.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.ui.editor.publish.domain.StorePublishMessageSeverity;

@Entity
@Table(name = "publishmessages")
@IdClass(StorePublishMessageId.class)
public class StorePublishMessage {

    @Id
    @Column(name = "sort_order")
    private int sortOrder;

    @Id
    @Column(name = "publish_id")
    private Long publishId;
    /**
     * The time the message was created or last modified.
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    /**
     * The severity level.
     */
    @Convert(converter=StorePublishMessageSeverityConverter.class)
    private StorePublishMessageSeverity severity;

    /**
     * The message.
     */
    private String message;

    /**
     * The id of the key of the content object related to the message.
     */
    @Column(name="CONTENT_ID")
    private String contentId;

    /**
     * The type of the key of the content object related to the message.
     */
    @Column(name="CONTENT_TYPE")
    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    @Column(name="STORE_ID")
    private String storeId;

    @Column(name="TASK")
    private String task;

    /**
     * Constructor. Will initialize the timestamp to the current time.
     *
     * @param severity
     *            the severity of the message
     * @param message
     *            the message text
     * @param contentKey
     *            the key of the content related to the message.
     */
    public StorePublishMessage(StorePublishMessageSeverity severity, String message, ContentKey contentKey) {
        this(severity, message, contentKey, null);
    }

    public StorePublishMessage(StorePublishMessageSeverity severity, String message, ContentKey contentKey, String task) {
        this(severity, message, task);
        if (contentKey != null) {
            this.contentId = contentKey.id;
            this.contentType = contentKey.type;
        }
    }

    /**
     *
     * @param severity
     *            severity of the message
     * @param message
     *            the message
     * @param storeId
     *            the related storeId
     * @param task
     *            which publish task is sending the message
     */
    public StorePublishMessage(StorePublishMessageSeverity severity, String message, String storeId, String task) {
        this(severity, message, task);
        this.storeId = storeId;
    }

    public StorePublishMessage(StorePublishMessageSeverity severity, String message, String task) {
        this(severity, message);
        this.task = task;
    }

    /**
     * Constructor. Will initialize the timestamp to the current time.
     *
     * @param severity
     *            the severity of the message
     * @param message
     *            the message text
     */
    public StorePublishMessage(StorePublishMessageSeverity severity, String message) {
        this();
        this.severity = severity;
        this.message = message;
    }

    /**
     * Default constructor, setting only the timestampt to current date
     */
    public StorePublishMessage() {
        timestamp = new Date();
    }

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

    /**
     * @return Returns the timestamp.
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * @param timestamp
     *            The timestamp to set.
     */
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Get the severity level of the message. 0 is very bad, higher numbers are OK.
     *
     * @return the severity level
     */
    public StorePublishMessageSeverity getSeverity() {
        return severity;
    }

    /**
     * Return a string representation of the messages severity.
     *
     * @return a string representation of the messages severity.
     */
    public String getSeverityString() {
        return severity != null ? severity.toString() : StorePublishMessageSeverity.UNKNOWN.toString();
    }

    /**
     * Set the severity level of the message. 0 is very bad, higher numbers are OK.
     *
     * @param level
     *            The level to set.
     */
    public void setSeverity(StorePublishMessageSeverity severity) {
        this.severity = severity;
    }

    /**
     * Get the message string.
     *
     * @return the message string.
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message
     *            The message to set.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Return the key of the content related to this message.
     *
     * @return the key to the content related to this message, or null if not available.
     */
    public ContentKey getContentKey() {
        return contentType != null && contentId != null ? ContentKeyFactory.get(contentType, contentId) : null;
    }

    /**
     * Set the key of the content related to this message.
     *
     * @param contentKey
     *            the key to the content related to this message, or null if not available.
     */
    public void setContentKey(ContentKey contentKey) {
        contentId = contentKey.id;
        contentType = contentKey.type;
    }

    /**
     * Get the id of the key of the content related to the message.
     *
     * @return the id of the key of the content related to the message.
     */
    public String getContentId() {
        return contentId;
    }

    /**
     * Set the id of the key of the content related to the message.
     *
     * @param contentId
     *            the id of the key of the content related to the message.
     */
    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    /**
     * get the type of the key of the content related to the message.
     *
     * @return the type of the key of the content related to the message.
     */
    public ContentType getContentType() {
        return contentType;
    }

    /**
     * Set the type of the key of the content related to the message.
     *
     * @param contentType
     *            the type of the key of the content related to the message.
     */
    public void setContentType(ContentType contentType) {
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
        return "PublishMessage[storeId: " + storeId + ", task: " + task + ", contentId: " + contentId + ", severity: " + getSeverityString() + ", message: " + message + ']';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof StorePublishMessage) {
            StorePublishMessage other = (StorePublishMessage) obj;
            EqualsBuilder equalsBuilder = new EqualsBuilder();
            equalsBuilder.append(message, other.getMessage());
            equalsBuilder.append(publishId, other.getPublishId());
            equalsBuilder.append(task, other.getTask());
            equalsBuilder.append(severity, other.getSeverity());
            equalsBuilder.append(storeId, other.getStoreId());
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
