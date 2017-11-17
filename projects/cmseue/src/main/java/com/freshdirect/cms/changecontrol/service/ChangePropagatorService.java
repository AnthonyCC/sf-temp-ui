package com.freshdirect.cms.changecontrol.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import com.freshdirect.cms.changecontrol.domain.ChangePropagationData;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.domain.ContentTypes;
import com.freshdirect.cms.core.service.ContentProvider;
import com.freshdirect.cms.core.service.ContentSource;
import com.freshdirect.cms.core.service.ContextualContentProvider;
import com.freshdirect.cms.draft.domain.DraftContext;
import com.freshdirect.cms.draft.service.DraftContentProviderService;
import com.freshdirect.cms.draft.service.DraftContextHolder;
import com.freshdirect.cms.draft.service.DraftService;
import com.freshdirect.cms.persistence.service.DatabaseContentProvider;
import com.google.common.base.Optional;

@Service
@Profile("database")
public class ChangePropagatorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChangePropagatorService.class);

    private static final String PROTOCOL = "http";

    @Value("${cms.eventpropagation.path:/cms/api/contentchanged/}")
    private String eventPropagationUri;

    @Autowired
    private ContentProvider contentProvider;

    @Autowired
    private ContextualContentProvider contentProviderService;

    @Autowired
    private DraftService draftService;

    @Autowired
    private DraftContextHolder draftContextHolder;

    /**
     * This method notifies the preview nodes about the changed content
     *
     * @param contentKeys
     */
    @Async
    public void notifyPreviewAboutChangedContent(DraftContext draftContext, Set<ContentKey> contentKeys) {
        draftContextHolder.setDraftContext(draftContext);
        List<String> previewHosts = loadPreviewHosts();

        RestTemplate notificationRestTemplate = new RestTemplate();

        for (String previewHost : previewHosts) {
            URI previewUri;
            try {
                previewUri = new URI(PROTOCOL + "://" + previewHost + eventPropagationUri);
                ChangePropagationData changeData = new ChangePropagationData(contentKeys, draftContext);
                LOGGER.info("Notifying preview node at [" + previewUri + "] about CMS changes " + changeData);
                notificationRestTemplate.postForLocation(previewUri, changeData);
            } catch (URISyntaxException e) {
                LOGGER.error("Couldn't create URI for previewHost: " + previewHost);
            }
        }
    }

    /**
     * On the preview nodes, this is the method that triggers the cache eviction
     *
     * @param contentKeys
     */
    public void receiveContentChangedNotification(Set<ContentKey> contentKeys, DraftContext draftContext) {
        Assert.notNull(contentKeys, "ContentKeys parameter can't be null!");
        LOGGER.info("Received content changed notification for contentKeys = " + StringUtils.join(contentKeys, ",") + " on draft = " + draftContext);
        draftContextHolder.setDraftContext(draftContext);
        if (draftContext == null || draftContext.isMainDraft()) {
            receiveContentChangedOnMain(contentKeys);
        } else {
            receiveContentChangedOnDraft(contentKeys, draftContext);
        }
    }

    private void receiveContentChangedOnMain(Set<ContentKey> contentKeys) {
        for (ContentKey contentKey : contentKeys) {
            if (contentKey != null && ContentSource.RELATIONAL_DATABASE.equals(contentProvider.getSource())) {
                ((DatabaseContentProvider) contentProvider).cacheEvict(contentKey);
                Set<ContentKey> relatedKeys = contentProviderService.getChildKeys(contentKey, false);
                for (ContentKey relatedKey : relatedKeys) {
                    ((DatabaseContentProvider) contentProvider).cacheEvict(relatedKey);
                }
            }
        }
    }

    private void receiveContentChangedOnDraft(Set<ContentKey> contentKeys, DraftContext draftContext) {
        Assert.notNull(draftContext, "Draft parameter can't be null!");
        draftService.invalidateDraftChangesCache(draftContext.getDraftId());
        if (contentProviderService instanceof DraftContentProviderService) {
            ((DraftContentProviderService) contentProviderService).updateDraftParentCacheForKeys(contentKeys);
            ((DraftContentProviderService) contentProviderService).invalidateDraftNodesCacheEntry(draftContext);
        }
    }

    private List<String> loadPreviewHosts() {
        Set<ContentKey> stores = contentProviderService.getContentKeysByType(ContentType.Store);
        List<String> previewHosts = new ArrayList<String>();
        for (ContentKey store : stores) {
            Optional<Object> preview = contentProviderService.getAttributeValue(store, ContentTypes.Store.PREVIEW_HOST_NAME);
            if (preview.isPresent()) {
                previewHosts.add(preview.get().toString());
            }
        }
        return previewHosts;
    }
}
