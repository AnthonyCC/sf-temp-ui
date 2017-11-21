package com.freshdirect.cms.media.converter;

import java.util.List;
import java.util.Map;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentTypes;
import com.freshdirect.cms.media.domain.Media;

@Service
public class AttributeValueToMediaConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AttributeValueToMediaConverter.class);

    private static final DateTimeFormatter DATE_FORMAT = ISODateTimeFormat.date();

    public Media convert(ContentKey contentKey, Map<Attribute, Object> attributesWithValues) {
        Media media = new Media(contentKey);
        switch (contentKey.type) {
            case Image:
                convertImage(contentKey, attributesWithValues, media);
                break;

            case Html:
                convertHtml(contentKey, attributesWithValues, media);
                break;

            case MediaFolder:
                convertMediaFolder(contentKey, attributesWithValues, media);
                break;

            case Template:
                convertTemplate(contentKey, attributesWithValues, media);
                break;
            default:
                break;
        }
        return media;
    }

    private void convertImage(ContentKey contentKey, Map<Attribute, Object> attributesWithValues, Media media) {
        media.setUri(attributesWithValues.get(ContentTypes.Image.path).toString());

        Object lastModified = attributesWithValues.get(ContentTypes.Image.lastmodified);
        parseLastModifiedDate(lastModified, media);

        Object width = attributesWithValues.get(ContentTypes.Image.width);
        Object height = attributesWithValues.get(ContentTypes.Image.height);
        if (width != null && height != null) {
            media.setWidth(Integer.parseInt(width.toString()));
            media.setHeight(Integer.parseInt(height.toString()));
        }
    }

    @SuppressWarnings("unchecked")
    private void convertMediaFolder(ContentKey contentKey, Map<Attribute, Object> attributesWithValues, Media media) {
        media.setUri(attributesWithValues.get(ContentTypes.MediaFolder.path).toString());

        Object lastModified = attributesWithValues.get(ContentTypes.MediaFolder.lastmodified);
        parseLastModifiedDate(lastModified, media);

        Object files = attributesWithValues.get(ContentTypes.MediaFolder.files);
        if (files instanceof List<?>) {
            media.setFiles((List<ContentKey>) files);
        }

        Object subfolders = attributesWithValues.get(ContentTypes.MediaFolder.subFolders);
        if (subfolders instanceof List<?>) {
            media.setSubFolders((List<ContentKey>) subfolders);
        }
    }

    private void convertHtml(ContentKey contentKey, Map<Attribute, Object> attributesWithValues, Media media) {
        media.setUri(attributesWithValues.get(ContentTypes.Html.path).toString());

        Object lastModified = attributesWithValues.get(ContentTypes.Html.lastmodified);
        parseLastModifiedDate(lastModified, media);
    }

    private void convertTemplate(ContentKey contentKey, Map<Attribute, Object> attributesWithValues, Media media) {
        media.setUri(attributesWithValues.get(ContentTypes.Template.path).toString());

        Object lastModified = attributesWithValues.get(ContentTypes.Template.lastmodified);
        parseLastModifiedDate(lastModified, media);
    }

    private void parseLastModifiedDate(Object lastModifiedValue, Media media) {
        if (lastModifiedValue != null) {
            try {
                media.setLastModified(DATE_FORMAT.parseDateTime(lastModifiedValue.toString()).toDate());
            } catch (IllegalArgumentException exc) {
                //LOGGER.debug("Failed to parse lastModified date " + lastModifiedValue.toString() + " of media " + media.getContentKey());
            }
        }
    }
}
