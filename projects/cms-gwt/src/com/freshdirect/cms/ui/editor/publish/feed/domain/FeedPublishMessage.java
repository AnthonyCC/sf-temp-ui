package com.freshdirect.cms.ui.editor.publish.feed.domain;

import java.util.Date;

import com.freshdirect.cms.core.domain.ContentKey;

public class FeedPublishMessage {

    private String message;
    private Date timestamp;
    private FeedPublishMessageLevel messageLevel;
    private ContentKey contentKey;
    private String storeId;
    private String task;

    public FeedPublishMessage(FeedPublishMessageLevel messageLevel, String message) {
        this.message = message;
        this.messageLevel = messageLevel;
        timestamp = new Date();
    }

    public FeedPublishMessage(FeedPublishMessageLevel messageLevel, String message, String storeId) {
        this(messageLevel, message);
        this.storeId = storeId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public FeedPublishMessageLevel getMessageLevel() {
        return messageLevel;
    }

    public void setMessageLevel(FeedPublishMessageLevel messageLevel) {
        this.messageLevel = messageLevel;
    }

    public ContentKey getContentKey() {
        return contentKey;
    }

    public void setContentKey(ContentKey contentKey) {
        this.contentKey = contentKey;
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
}
