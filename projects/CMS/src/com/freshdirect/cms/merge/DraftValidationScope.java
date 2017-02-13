package com.freshdirect.cms.merge;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentTypeDefI;
import com.freshdirect.cms.RelationshipDefI;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.ContentTypeServiceI;
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.cms.application.draftchange.converter.DraftChangeToContentNodeApplicator;
import com.freshdirect.cmsadmin.domain.DraftChange;
import com.freshdirect.framework.util.log.LoggerFactory;

public class DraftValidationScope {

    private static final Logger LOGGER = LoggerFactory.getInstance(DraftValidationScope.class);
    
    private final Set<ContentKey> contentKeys = new HashSet<ContentKey>();
    private final Set<ContentKey> expandedKeys;

    // map children keys of a navigable relationship to their parent
    private final Map<Long, ContentKey> changeId2keyMap = new HashMap<Long, ContentKey>();

    private DraftValidationScope(Builder builder) {
        ContentTypeServiceI typeService = builder.contentService.getTypeService();

        for (DraftChange change : builder.draftChanges) {
            final ContentKey key = ContentKey.getContentKey(change.getContentKey());
            final ContentTypeDefI typeDef = typeService.getContentTypeDefinition(key.getType());

            AttributeDefI attributeDef = typeDef.getAttributeDef(change.getAttributeName());

            if (attributeDef != null) {
                expandScopeWithChildKeys(key, change, attributeDef);
            }
        }
        
        expandedKeys = new HashSet<ContentKey>(changeId2keyMap.values());
        LOGGER.debug("Draft Changes: " + builder.draftChanges.size() + "; Content Keys: " + this.contentKeys.size() + "; Expanded keys: " + expandedKeys.size());
    }

    protected void expandScopeWithChildKeys(final ContentKey draftKey, DraftChange change, AttributeDefI attributeDef) {
        if (attributeDef instanceof RelationshipDefI) {
            RelationshipDefI relshipDef = (RelationshipDefI) attributeDef;
            if (relshipDef.isNavigable()) {
                // navigable key -> map draft change IDs to child keys
                final List<ContentKey> changedChildKeys = DraftChangeToContentNodeApplicator.getContentKeysFromRelationshipValue(relshipDef, change.getValue());
                if (!changedChildKeys.isEmpty()) {
                    for (ContentKey childKey : changedChildKeys) {
                        changeId2keyMap.put(change.getId(), childKey);
                    }
                }
            }
        }

        contentKeys.add(draftKey);
    }

    /**
     * Return content keys related directly to draft changes
     * 
     * @return
     */
    public Set<ContentKey> getContentKeys() {
        return Collections.unmodifiableSet(contentKeys);
    }

    /**
     * Return content keys affected by changes indirectly
     * @return
     */
    public Set<ContentKey> getExpandedKeys() {
        return Collections.unmodifiableSet(expandedKeys);
    }

    /**
     * Collect content keys may be affected by draft changes 
     * 
     * @param originalKey
     * @return
     */
    public Collection<Long> lookupDraftChangeIds(ContentKey originalKey) {
        Set<Long> draftKeys = new HashSet<Long>();
        for (Map.Entry<Long, ContentKey> entry : changeId2keyMap.entrySet()) {
            if (entry.getValue().equals(originalKey)) {
                draftKeys.add(entry.getKey());
            }
        }
        return draftKeys;
    }

    public static class Builder {

        private ContentServiceI contentService;
        @SuppressWarnings("unused")
        private DraftContext draftContext = DraftContext.MAIN;

        private Collection<DraftChange> draftChanges = new ArrayList<DraftChange>();

        public Builder setContentService(ContentServiceI contentService) {
            this.contentService = contentService;
            return this;
        }

        public Builder setDraftContext(DraftContext draftContext) {
            this.draftContext = draftContext;
            return this;
        }

        public Builder addDraftChange(DraftChange change) {
            this.draftChanges.add(change);
            return this;
        }

        public Builder addDraftChanges(Collection<DraftChange> changes) {
            this.draftChanges.addAll(changes);
            return this;
        }

        public DraftValidationScope build() {
            return new DraftValidationScope(this);
        }
    }
}
