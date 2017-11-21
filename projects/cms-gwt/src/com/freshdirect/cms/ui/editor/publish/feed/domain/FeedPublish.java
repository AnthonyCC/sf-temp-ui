package com.freshdirect.cms.ui.editor.publish.feed.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.ui.editor.publish.feed.entity.PublishStatus;

public class FeedPublish {

    private String publishId;
    private String comment;
    private Date lastModifiedTimestamp;
    private Date creationTimestamp;
    private String userId;
    private PublishStatus status;
    private ContentKey storeKey;

    private List<FeedPublishMessage> messages;

    public String getPublishId() {
        return publishId;
    }

    public void setPublishId(String publishId) {
        this.publishId = publishId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getLastModifiedTimestamp() {
        return lastModifiedTimestamp;
    }

    public void setLastModifiedTimestamp(Date lastModifiedTimestamp) {
        this.lastModifiedTimestamp = lastModifiedTimestamp;
    }

    public Date getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(Date creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public ContentKey getStoreKey() {
        return storeKey;
    }

    public void setStoreKey(ContentKey storeKey) {
        this.storeKey = storeKey;
    }

    public List<FeedPublishMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<FeedPublishMessage> messages) {
        this.messages = messages;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public PublishStatus getStatus() {
        return status;
    }

    public void setStatus(PublishStatus status) {
        this.status = status;
    }

    public void addMessage(FeedPublishMessage message) {
        if (messages == null) {
            messages = new ArrayList<FeedPublishMessage>();
        }
        message.setTimestamp(new Date());
        messages.add(message);
    }

    public String lastInfoMessage() {
        String lastMessage = "running.";
        Date lastDate = null;
        for (FeedPublishMessage message : this.getMessages()) {
            if (message.getMessageLevel().equals(FeedPublishMessageLevel.INFO)) {
                if (lastDate == null || lastDate.before(message.getTimestamp())) {
                    if (message.getMessage() != null && message.getMessage().trim().length() > 0) {
                        lastMessage = message.getMessage();
                        lastDate = message.getTimestamp();
                    }
                }
            }
        }
        return lastMessage;
    }

}
