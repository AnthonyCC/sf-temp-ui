package com.freshdirect.cms.core.service;

import java.util.LinkedHashMap;
import java.util.Map;

import com.freshdirect.cms.changecontrol.domain.ContentUpdateContext;
import com.freshdirect.cms.changecontrol.entity.ContentChangeSetEntity;
import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.google.common.base.Optional;

/**
 * Content Providers implementing this method can persist content changes
 *
 * @param payload
 *            Attribute values to be persisted
 * @param context holds update info like author, date of change and optional note
 */
public interface UpdatableContentProvider {
    /**
     * Update persisted content with the supplied changes
     * Changes are validated before save
     *
     * @param payload
     *            Attribute values to be persisted
     * @param context holds update info like author, date of change and optional note
     */
    public Optional<ContentChangeSetEntity> updateContent(LinkedHashMap<ContentKey, Map<Attribute, Object>> payload, ContentUpdateContext context);
}
