package com.freshdirect.cms.mediaassociation.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.freshdirect.cms.changecontrol.domain.ContentUpdateContext;
import com.freshdirect.cms.changecontrol.entity.ContentChangeEntity;
import com.freshdirect.cms.changecontrol.entity.ContentChangeSetEntity;
import com.freshdirect.cms.changecontrol.entity.ContentChangeType;
import com.freshdirect.cms.changecontrol.service.ContentChangeControlService;
import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.service.ContentProviderService;
import com.freshdirect.cms.draft.domain.DraftContext;
import com.freshdirect.cms.media.domain.Media;
import com.freshdirect.cms.media.service.MediaService;
import com.google.common.base.Optional;

@Service
public class MediaEventHandlerService {

    @Autowired
    private MediaService mediaService;

    @Autowired
    private MediaAssociatorService mediaAssociatorService;

    @Autowired
    private ContentProviderService contentProviderService;

    @Autowired
    private ContentChangeControlService contentChangeControlService;

    public Optional<Media> lookupMedia(String uri) {
        return mediaService.getMediaByUri(uri);
    }

    public void createMedia(Media mediaToCreate, String userId) {
        Assert.notNull(mediaToCreate, "Media must be specified");
        final ContentKey mediaKey = mediaToCreate.getContentKey();
        Assert.isTrue(Media.isMediaType(mediaKey), "Non-media item supplied");

        mediaService.saveMedia(mediaToCreate);

        logChange(mediaKey, ContentChangeType.CRE, "Created media ", userId);

        // make media item linkable to content nodes
        if (ContentType.MediaFolder != mediaKey.type) {
            createLinkNode(mediaKey);
        }

        // do media to node association
        if (mediaAssociatorService.canAssociateMedia(mediaToCreate)) {
            mediaAssociatorService.autoAssociateMedia(mediaToCreate, userId);
        }
    }

    private void createLinkNode(final ContentKey mediaKey) {
        if (!contentProviderService.containsContentKey(mediaKey)) {
            LinkedHashMap<ContentKey, Map<Attribute, Object>> payload = new LinkedHashMap<ContentKey, Map<Attribute, Object>>();
            payload.put(mediaKey, Collections.<Attribute, Object>emptyMap());

            ContentUpdateContext updateContext = new ContentUpdateContext(MediaEventHandlerService.class.getSimpleName(), new Date(),
                    "Create link node for media item " + mediaKey, DraftContext.MAIN, payload.keySet());
            contentProviderService.updateContent(payload, updateContext);
        }
    }

    public void deleteMedia(String mediaUri, String userId) {
        List<Media> mediasByPrefix = mediaService.getMediasByUriStartsWith(mediaUri);
        for (Media media : mediasByPrefix) {
            mediaService.deleteMedia(media);
            logChange(media.getContentKey(), ContentChangeType.DEL, "Deleted media", userId);
        }
    }

    public void moveMedia(String source, String destionation, String userId) {
        List<Media> mediasByPrefix = mediaService.getMediasByUriStartsWith(source);
        for (Media media : mediasByPrefix) {
            String originalMediaUri = media.getUri();
            media.setUri(originalMediaUri.replaceFirst(source, destionation));
            mediaService.saveMedia(media);
            logChange(media.getContentKey(), ContentChangeType.MOD, "Moved media to " + destionation, userId);
        }
    }

    public void updateMedia(Media mediaToUpdate, String userId) {
        mediaService.saveMedia(mediaToUpdate);
        logChange(mediaToUpdate.getContentKey(), ContentChangeType.MOD, "Updated media", userId);
    }

    private void logChange(ContentKey contentKey, ContentChangeType changeType, String note, String userId) {
        ContentChangeSetEntity changeSet = new ContentChangeSetEntity();
        ContentChangeEntity contentChangeEntity = new ContentChangeEntity();

        contentChangeEntity.setChangeType(changeType);
        contentChangeEntity.setContentKey(contentKey);

        changeSet.setChanges(Arrays.asList(contentChangeEntity));
        changeSet.setNote(note);
        changeSet.setUserId(userId);
        changeSet.setTimestamp(new Date());

        contentChangeControlService.save(changeSet);
    }
}
