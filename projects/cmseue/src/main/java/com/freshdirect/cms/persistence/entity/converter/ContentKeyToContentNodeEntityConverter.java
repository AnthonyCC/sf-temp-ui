package com.freshdirect.cms.persistence.entity.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.persistence.entity.ContentNodeEntity;

@Service
public class ContentKeyToContentNodeEntityConverter {

    public ContentNodeEntity convert(ContentKey contentKey) {
        ContentNodeEntity contentNodeEntity = new ContentNodeEntity();
        contentNodeEntity.setContentKey(contentKey.toString());
        contentNodeEntity.setContentType(contentKey.type.toString());
        return contentNodeEntity;
    }

    public List<ContentNodeEntity> convert(List<ContentKey> domains) {
        List<ContentNodeEntity> contentNodeEntities = null;
        if (domains != null && domains.size() > 0) {
            contentNodeEntities = new ArrayList<ContentNodeEntity>();
            for (ContentKey contentKey : domains) {
                contentNodeEntities.add(convert(contentKey));
            }
        }
        return contentNodeEntities;
    }
}
