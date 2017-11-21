package com.freshdirect.cms.ui.editor.publish.feed.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.ui.editor.publish.feed.domain.FeedPublish;
import com.freshdirect.cms.ui.editor.publish.feed.domain.FeedPublishMessage;
import com.freshdirect.cms.ui.editor.publish.feed.domain.FeedPublishMessageLevel;
import com.freshdirect.cms.ui.editor.publish.feed.entity.FeedPublishEntity;
import com.freshdirect.cms.ui.editor.publish.feed.entity.FeedPublishMessageEntity;

@Service
public class FeedPublishEntityToFeedPublishConverter {

    public List<FeedPublish> convert(List<FeedPublishEntity> entities) {
        List<FeedPublish> result = new ArrayList<FeedPublish>();
        for (FeedPublishEntity entity : entities) {
            result.add(convert(entity));
        }
        return result;
    }

    public FeedPublish convert(FeedPublishEntity feedPublishEntity) {
        FeedPublish feedPublish = new FeedPublish();
        feedPublish.setComment(feedPublishEntity.getDescription());
        feedPublish.setPublishId(String.valueOf(feedPublishEntity.getId()));
        feedPublish.setLastModifiedTimestamp(feedPublishEntity.getLastModified());
        feedPublish.setCreationTimestamp(feedPublishEntity.getTimestamp());
        feedPublish.setStatus(feedPublishEntity.getStatus());
        feedPublish.setUserId(feedPublishEntity.getUserId());
        if (feedPublishEntity.getMessages() != null) {
            feedPublish.setMessages(convertMessages(feedPublishEntity.getMessages()));
        }
        return feedPublish;
    }

    private List<FeedPublishMessage> convertMessages(List<FeedPublishMessageEntity> entityMessages) {
        List<FeedPublishMessage> messages = new ArrayList<FeedPublishMessage>();

        if (entityMessages != null) {
            for (FeedPublishMessageEntity message : entityMessages) {
                FeedPublishMessage domainMessage = new FeedPublishMessage(FeedPublishMessageLevel.DEBUG, message.getMessage());
                domainMessage.setTimestamp(message.getTimestamp());
                if (message.getContentType() != null && message.getContentId() != null) {
                    domainMessage.setContentKey(ContentKeyFactory.get(ContentType.valueOf(message.getContentType()), message.getContentId()));
                }
                domainMessage.setTask(message.getTask());
                domainMessage.setStoreId(message.getStoreId());
                switch (message.getSeverity()) {
                    case DEBUG:
                        domainMessage.setMessageLevel(FeedPublishMessageLevel.DEBUG);
                        break;
                    case INFO:
                        domainMessage.setMessageLevel(FeedPublishMessageLevel.INFO);
                        break;
                    case WARNING:
                        domainMessage.setMessageLevel(FeedPublishMessageLevel.WARNING);
                        break;
                    case ERROR:
                        domainMessage.setMessageLevel(FeedPublishMessageLevel.ERROR);
                        break;
                    case FAILURE:
                        domainMessage.setMessageLevel(FeedPublishMessageLevel.FAILURE);
                        break;
                }

                messages.add(domainMessage);
            }
        }

        return messages;
    }

}
