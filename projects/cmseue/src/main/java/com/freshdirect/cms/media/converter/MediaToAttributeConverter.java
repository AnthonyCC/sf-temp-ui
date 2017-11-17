package com.freshdirect.cms.media.converter;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentTypes;
import com.freshdirect.cms.media.domain.Media;
import com.google.common.collect.ImmutableMap;

@Service
public class MediaToAttributeConverter {

    public Map<ContentKey, Map<Attribute, Object>> convert(final Media media) {
        final Map<Attribute, Object> attributes = new HashMap<Attribute, Object>();
        switch (media.getContentKey().type) {
            case Image:
                convertImageToAttributes(media, attributes);
                break;

            case Html:
                convertHtmlToAttributes(media, attributes);
                break;

            case MediaFolder:
                convertMediaFolderToAttributes(media, attributes);
                break;

            case Template:
                convertTemplateToAttributes(media, attributes);
                break;

            default:
                break;
        }
        return ImmutableMap.of(media.getContentKey(), attributes);
    }

    private void convertImageToAttributes(Media media, Map<Attribute, Object> attributes) {
        putAttribute(ContentTypes.Image.path, media.getUri(), attributes);
        putAttribute(ContentTypes.Image.height, media.getHeight(), attributes);
        putAttribute(ContentTypes.Image.width, media.getWidth(), attributes);
        putAttribute(ContentTypes.Image.lastmodified, media.getLastModified(), attributes);
    }

    private void convertHtmlToAttributes(Media media, Map<Attribute, Object> attributes) {
        putAttribute(ContentTypes.Html.path, media.getUri(), attributes);
        putAttribute(ContentTypes.Html.lastmodified, media.getLastModified(), attributes);
    }

    private void convertMediaFolderToAttributes(Media media, Map<Attribute, Object> attributes) {
        putAttribute(ContentTypes.MediaFolder.lastmodified, media.getLastModified(), attributes);
        putAttribute(ContentTypes.MediaFolder.path, media.getUri(), attributes);
        putAttribute(ContentTypes.MediaFolder.files, media.getFiles(), attributes);
        putAttribute(ContentTypes.MediaFolder.subFolders, media.getSubFolders(), attributes);
    }

    private void convertTemplateToAttributes(Media media, Map<Attribute, Object> attributes) {
        putAttribute(ContentTypes.Template.lastmodified, media.getLastModified(), attributes);
        putAttribute(ContentTypes.Template.path, media.getUri(), attributes);
    }

    private void putAttribute(Attribute attribute, Object value, Map<Attribute, Object> attributes) {
        if (value != null) {
            attributes.put(attribute, value);
        }
    }
}
