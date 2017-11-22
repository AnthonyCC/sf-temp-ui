package com.freshdirect.cms.ui.editor.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.service.ContextualContentProvider;
import com.freshdirect.cms.draft.domain.DraftContext;
import com.freshdirect.cms.draft.service.DraftContextHolder;
import com.freshdirect.cms.ui.editor.domain.PublishType;
import com.freshdirect.cms.ui.editor.publish.feed.converter.FeedPublishToGwtPublishDataConverter;
import com.freshdirect.cms.ui.editor.publish.feed.domain.FeedPublish;
import com.freshdirect.cms.ui.editor.publish.feed.entity.PublishStatus;
import com.freshdirect.cms.ui.editor.publish.feed.service.FeedPublishMessagingService;
import com.freshdirect.cms.ui.editor.publish.feed.service.FeedPublishService;
import com.freshdirect.cms.ui.editor.publish.repository.AdminQueriesRepository;
import com.freshdirect.cms.ui.model.publish.GwtPublishData;
import com.freshdirect.cms.ui.service.ServerException;

@Service
public class PublishService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PublishService.class);

    private static final Comparator<FeedPublish> FEED_CREATION_DATE_COMPARATOR = new Comparator<FeedPublish>() {

        @Override
        public int compare(FeedPublish o1, FeedPublish o2) {
            return o2.getLastModifiedTimestamp().compareTo(o1.getLastModifiedTimestamp());
        }

    };

    @Autowired
    private FeedPublishService feedPublishService;

    @Autowired
    private DraftContextHolder draftContextHolder;

    @Autowired
    private FeedPublishMessagingService feedPublishMessagingService;

    @Autowired
    private FeedPublishToGwtPublishDataConverter feedPublishToGwtPublishDataConverter;

    @Autowired
    private ContextualContentProvider contentProviderService;

    @Autowired
    private AdminQueriesRepository adminQueriesRepository;

    public String startPublish(PublishType type, String comment, String userId) throws ServerException {
        LOGGER.info("start publish: " + type + " - " + comment);

        String publishId = "";
        if (PublishType.PUBLISH_X.equals(type)) {
            try {
                Set<ContentKey> storeKeys = contentProviderService.getContentKeysByType(ContentType.Store);
                FeedPublish publish = new FeedPublish();
                publish.setUserId(userId);
                publish.setComment(comment);
                publish = feedPublishMessagingService.modifyFeedPublishStatus(publish, PublishStatus.PROGRESS);

                draftContextHolder.setDraftContext(DraftContext.MAIN);
                feedPublishService.publishFeed(publish, new ArrayList<ContentKey>(storeKeys), userId, comment);

                publishId = publish.getPublishId();
            } catch (DataIntegrityViolationException exc) {
                LOGGER.error("Probably a feed publish is already running, discard current request", exc);
            }
        }
        return publishId;

    }

    public List<GwtPublishData> getPublishHistory(PublishType type) throws ServerException {
        List<GwtPublishData> publishHistory = new ArrayList<GwtPublishData>();
        List<FeedPublish> publishes = feedPublishService.loadAll();
        Collections.sort(publishes, FEED_CREATION_DATE_COMPARATOR);
        for (FeedPublish publish : publishes) {
            GwtPublishData publishData = feedPublishToGwtPublishDataConverter.convert(publish);
            publishHistory.add(publishData);
        }

        return publishHistory;
    }

    @Transactional
    public void abortStuckPublishes() {
        adminQueriesRepository.updateStuckPublishStatus();
    }

}
