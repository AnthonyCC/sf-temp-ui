package com.freshdirect.cms.media.converter;

import org.springframework.stereotype.Service;

import com.freshdirect.cms.media.domain.Media;
import com.freshdirect.cms.media.entity.MediaEntity;

@Service
public class MediaToMediaEntityConverter {

    public MediaEntity convert(Media media, MediaEntity mediaEntity) {
        mediaEntity.setId(media.getContentId());
        mediaEntity.setType(media.getContentType());
        mediaEntity.setUri(media.getUri());
        mediaEntity.setHeight(media.getHeight());
        mediaEntity.setWidth(media.getWidth());
        if (media.getMimeType() != null) {
            mediaEntity.setMimeType(media.getMimeType());
        }
        return mediaEntity;
    }
}
