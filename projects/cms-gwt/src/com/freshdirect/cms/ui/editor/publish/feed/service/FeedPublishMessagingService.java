package com.freshdirect.cms.ui.editor.publish.feed.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.freshdirect.cms.ui.editor.publish.feed.converter.FeedPublishEntityToFeedPublishConverter;
import com.freshdirect.cms.ui.editor.publish.feed.converter.FeedPublishToFeedPublishEntityConverter;
import com.freshdirect.cms.ui.editor.publish.feed.domain.FeedPublish;
import com.freshdirect.cms.ui.editor.publish.feed.domain.FeedPublishMessage;
import com.freshdirect.cms.ui.editor.publish.feed.entity.FeedPublishEntity;
import com.freshdirect.cms.ui.editor.publish.feed.entity.PublishStatus;
import com.freshdirect.cms.ui.editor.publish.feed.repository.FeedPublishEntityRepository;

@Service
public class FeedPublishMessagingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FeedPublishMessagingService.class);

    @Autowired
    private FeedPublishEntityRepository repository;

    @Autowired
    private FeedPublishToFeedPublishEntityConverter feedToEntityConverter;

    @Autowired
    private FeedPublishEntityToFeedPublishConverter entityToFeedConverter;

    public FeedPublish addMessage(FeedPublish feedPublish, FeedPublishMessage messageToAdd) {
        feedPublish.addMessage(messageToAdd);
        return saveFeedPublish(feedPublish);
    }

    public FeedPublish modifyFeedPublishStatus(FeedPublish feedPublish, PublishStatus status) {
        feedPublish.setStatus(status);
        return saveFeedPublish(feedPublish);
    }

    @Transactional
    private FeedPublish saveFeedPublish(FeedPublish feedPublish) {

        FeedPublishEntity alreadySaved = null;

        if (feedPublish.getPublishId() != null && !feedPublish.getPublishId().equals("latest")) {
            alreadySaved = repository.findById(Long.parseLong(feedPublish.getPublishId()));
        }

        LOGGER.info("AlreadySaved: " + alreadySaved);

        if (alreadySaved == null) {
            alreadySaved = feedToEntityConverter.convert(feedPublish);
        } else {
            feedToEntityConverter.convert(feedPublish, alreadySaved);
        }

        LOGGER.info("AlreadySaved after conversion: " + alreadySaved.toString());

        FeedPublishEntity saved = repository.save(alreadySaved);

        FeedPublish result = entityToFeedConverter.convert(saved);
        result.setStoreKey(feedPublish.getStoreKey());

        return result;
    }
}
