package com.freshdirect.cms.media.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.media.converter.MediaEntityToMediaConverter;
import com.freshdirect.cms.media.converter.MediaToMediaEntityConverter;
import com.freshdirect.cms.media.domain.Media;
import com.freshdirect.cms.media.entity.MediaEntity;
import com.freshdirect.cms.media.repository.MediaEntityRepository;
import com.google.common.base.Optional;

@Profile({ "database", "test" })
@Service
public class DatabaseMediaService implements MediaService {

    @Autowired
    private MediaEntityRepository mediaEntityRepository;

    @Autowired
    private MediaEntityToMediaConverter mediaEntityToMediaConverter;

    @Autowired
    private MediaToMediaEntityConverter mediaToMediaEntityConverter;

    @Override
    public List<Media> loadAll() {
        return mediaEntityToMediaConverter.convertCollection(mediaEntityRepository.findAll());
    }

    @Override
    public Optional<Media> getMediaByContentKey(ContentKey mediaContentKey) {
        Assert.notNull(mediaContentKey, "ContentKey parameter can't be null!");

        MediaEntity entity = mediaEntityRepository.findByTypeAndId(mediaContentKey.type.toString(), mediaContentKey.id);
        Media media = mediaEntityToMediaConverter.convert(entity);
        return Optional.fromNullable(media);
    }

    @Override
    public Map<ContentKey, Media> getMediasByContentKeys(List<ContentKey> mediaContentKeys) {
        Assert.notNull(mediaContentKeys, "ContentKeys parameter can't be null!");

        Map<ContentKey, Media> medias = new HashMap<ContentKey, Media>();

        List<String> types = new ArrayList<String>();
        List<String> ids = new ArrayList<String>();
        for (ContentKey contentKey : mediaContentKeys) {
            types.add(contentKey.type.toString());
            ids.add(contentKey.id);
        }
        List<MediaEntity> mediaEntities = mediaEntityRepository.findByTypeInAndIdIn(types, ids);
        List<Media> mediaItems = mediaEntityToMediaConverter.convertCollection(mediaEntities);
        for (Media media : mediaItems) {
            medias.put(media.getContentKey(), media);
        }
        return medias;
    }

    @Override
    public Optional<Media> getMediaByUri(String uri) {
        Assert.notNull(uri, "Uri paramter can't be null!");

        MediaEntity mediaEntity = mediaEntityRepository.findByUri(uri);
        return Optional.fromNullable(mediaEntityToMediaConverter.convert(mediaEntity));
    }

    @Override
    public Set<ContentKey> getChildMediaKeys(ContentKey parentMediaContentKey) {
        Assert.notNull(parentMediaContentKey, "parentMediaContentKey parameter may not be null");

        Set<ContentKey> childKeys = new HashSet<ContentKey>();

        Optional<Media> optionalParentMedia = getMediaByContentKey(parentMediaContentKey);
        if (optionalParentMedia.isPresent()) {
            String uri = optionalParentMedia.get().getUri();
            String basePath = uri.endsWith("/") ? uri + "%" : uri + "/%";
            List<MediaEntity> mediaEntities = mediaEntityRepository.findByBasePath(basePath);
            List<Media> mediaItems = mediaEntityToMediaConverter.convertCollection(mediaEntities);
            for (Media media : mediaItems) {
                childKeys.add(media.getContentKey());
            }
        }

        return childKeys;
    }

    @Override
    public void saveMedia(Media media) {
        Assert.notNull(media, "Media parameter can't be null!");
        Assert.notNull(media.getContentKey(), "Trying to save a Media object without a ContentKey");

        MediaEntity mediaEntity = null;
        mediaEntity = mediaEntityRepository.findByTypeAndId(media.getContentType(), media.getContentId());
        if (mediaEntity == null) {
            mediaEntity = new MediaEntity();
        }
        mediaEntity = mediaToMediaEntityConverter.convert(media, mediaEntity);
        mediaEntity.setLastModified(new Date());
        mediaEntityRepository.save(mediaEntity);
    }

    @Override
    public void deleteMedia(Media media) {
        Assert.notNull(media, "Media paramter can't be null!");
        Assert.notNull(media.getContentKey(), "Trying to save a Media object without a ContentKey");

        MediaEntity mediaEntity = null;
        mediaEntity = mediaEntityRepository.findByTypeAndId(media.getContentType(), media.getContentId());
        Assert.notNull(mediaEntity, "Trying to delete a not existing media!");

        mediaEntity = mediaToMediaEntityConverter.convert(media, mediaEntity);
        mediaEntityRepository.delete(mediaEntity);
    }

    @Override
    public List<Media> getMediasByUriStartsWith(String uriPrefix) {
        Assert.notNull(uriPrefix);
        return mediaEntityToMediaConverter.convertCollection(mediaEntityRepository.findByUriStartsWith(uriPrefix));
    }

    @Override
    public List<Media> findMediaNewerThan(Date date) {
        Assert.notNull(date);
        return mediaEntityToMediaConverter.convertCollection(mediaEntityRepository.findByLastModifiedGreaterThan(date));
    }
    

}
