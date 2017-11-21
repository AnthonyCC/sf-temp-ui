package com.freshdirect.cms.media.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.media.domain.Media;
import com.freshdirect.cms.media.entity.MediaEntity;

@Service
public class MediaEntityToMediaConverter {

    public List<Media> convertCollection(List<MediaEntity> mediaEntities) {
        List<Media> mediaItems = new ArrayList<Media>();
        for (MediaEntity mediaEntity : mediaEntities) {
            Media media = convert(mediaEntity);
            if (media != null) {
                mediaItems.add(media);
            }
        }
        return mediaItems;
    }

    public Media convert(MediaEntity mediaEntity) {
        Media media = null;
        if (mediaEntity != null) {
            media = new Media(ContentKeyFactory.get(ContentType.valueOf(mediaEntity.getType()), mediaEntity.getId()));
            media.setHeight(mediaEntity.getHeight());
            media.setWidth(mediaEntity.getWidth());
            media.setLastModified(mediaEntity.getLastModified());
            media.setUri(mediaEntity.getUri());
            if (mediaEntity.getMimeType() != null) {
                media.setMimeType(mediaEntity.getMimeType());
            }
        }
        return media;
    }
}
