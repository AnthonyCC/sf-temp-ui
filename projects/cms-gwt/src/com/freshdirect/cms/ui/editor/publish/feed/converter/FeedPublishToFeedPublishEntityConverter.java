package com.freshdirect.cms.ui.editor.publish.feed.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.freshdirect.cms.ui.editor.publish.feed.domain.FeedPublish;
import com.freshdirect.cms.ui.editor.publish.feed.domain.FeedPublishMessage;
import com.freshdirect.cms.ui.editor.publish.feed.entity.FeedPublishEntity;
import com.freshdirect.cms.ui.editor.publish.feed.entity.FeedPublishMessageEntity;
import com.freshdirect.cms.ui.editor.publish.feed.entity.FeedPublishMessageSeverity;

@Service
public class FeedPublishToFeedPublishEntityConverter {

    public FeedPublishEntity convert(FeedPublish feedPublish) {
        FeedPublishEntity entity = new FeedPublishEntity();
        entity.setDescription(feedPublish.getComment());
        if (feedPublish.getPublishId() != null && feedPublish.getPublishId().equals("latest")) {
            entity.setId(Long.parseLong(feedPublish.getPublishId()));
        }

        entity.setUserId(feedPublish.getUserId());
        entity.setTimestamp(feedPublish.getCreationTimestamp());
        entity.setLastModified(feedPublish.getLastModifiedTimestamp());
        entity.setStatus(feedPublish.getStatus());
        if (feedPublish.getMessages() != null && feedPublish.getPublishId() != null && !feedPublish.getPublishId().equals("latest")) {
            entity.setMessages(convert(feedPublish.getMessages(), Integer.parseInt(feedPublish.getPublishId())));
        }
        return entity;
    }

    public void convert(FeedPublish feedPublish, FeedPublishEntity entity) {
        entity.setDescription(feedPublish.getComment());
        if (feedPublish.getPublishId() != null && feedPublish.getPublishId().equals("latest")) {
            entity.setId(Long.parseLong(feedPublish.getPublishId()));
        }

        entity.setUserId(feedPublish.getUserId());
        entity.setStatus(feedPublish.getStatus());
        if (feedPublish.getMessages() != null && feedPublish.getPublishId() != null && !feedPublish.getPublishId().equals("latest")) {
            entity.setMessages(convert(feedPublish.getMessages(), Integer.parseInt(feedPublish.getPublishId())));
        }
    }

    private List<FeedPublishMessageEntity> convert(List<FeedPublishMessage> messages, int publishId) {
        List<FeedPublishMessageEntity> entityMessages = new ArrayList<FeedPublishMessageEntity>();
        if (messages != null) {
            int order = 0;
            for (FeedPublishMessage message : messages) {
                FeedPublishMessageEntity entityMessage = new FeedPublishMessageEntity();
                entityMessage.setMessage(message.getMessage());
                entityMessage.setPublishId(Long.valueOf(publishId));
                entityMessage.setSortOrder(order++);
                entityMessage.setTimestamp(message.getTimestamp());

                FeedPublishMessageSeverity severity;
                switch (message.getMessageLevel()) {
                    case DEBUG:
                        severity = FeedPublishMessageSeverity.DEBUG;
                        break;
                    case WARNING:
                        severity = FeedPublishMessageSeverity.WARNING;
                        break;
                    case ERROR:
                        severity = FeedPublishMessageSeverity.ERROR;
                        break;
                    case FAILURE:
                        severity = FeedPublishMessageSeverity.FAILURE;
                        break;
                    case INFO:
                        severity = FeedPublishMessageSeverity.INFO;
                        break;
                    default:
                        severity = FeedPublishMessageSeverity.INFO;
                }
                entityMessage.setSeverity(severity);
                entityMessage.setTask(message.getTask());
                entityMessage.setStoreId(message.getStoreId());
                if (message.getContentKey() != null) {
                    entityMessage.setContentId(message.getContentKey().id);
                    entityMessage.setContentType(message.getContentKey().type.toString());
                }
                entityMessages.add(entityMessage);
            }
        }
        return entityMessages;
    }

}
