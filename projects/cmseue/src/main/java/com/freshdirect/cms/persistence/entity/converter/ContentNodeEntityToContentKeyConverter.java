package com.freshdirect.cms.persistence.entity.converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.persistence.entity.ContentNodeEntity;

@Service
public class ContentNodeEntityToContentKeyConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContentNodeEntityToContentKeyConverter.class);
    
    private ContentKey convert(ContentNodeEntity entity) {
        Assert.notNull(entity, "Missing content node entity");
        final String serializedKey = entity.getContentKey();
        Assert.notNull(serializedKey, "Missing serialized key");
        final String[] keyComponents = serializedKey.split(":");
        Assert.isTrue(keyComponents.length == 2, "Failed to split up '" + serializedKey + "' to components");
        return ContentKeyFactory.get(ContentType.valueOf(keyComponents[0]), keyComponents[1]);
    }

    public List<ContentKey> convert(Collection<ContentNodeEntity> entities) {
        List<ContentKey> contentKeys = Collections.emptyList();
        if (entities != null && entities.size() > 0) {
            contentKeys = new ArrayList<ContentKey>();
            for (ContentNodeEntity contentNodeEntity : entities) {
                try {
                    contentKeys.add(convert(contentNodeEntity));
                } catch (IllegalArgumentException ex) {
                    LOGGER.error("Failed to convert entity", ex);
                }
            }
        }
        return contentKeys;
    }
}
