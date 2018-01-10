package com.freshdirect.cms.draft.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.freshdirect.cms.changecontrol.domain.ContentChangedEvent;
import com.freshdirect.cms.changecontrol.domain.ContentUpdateContext;
import com.freshdirect.cms.changecontrol.entity.ContentChangeSetEntity;
import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.domain.Relationship;
import com.freshdirect.cms.core.domain.RootContentKey;
import com.freshdirect.cms.core.service.ContentProviderService;
import com.freshdirect.cms.core.service.ContextService;
import com.freshdirect.cms.core.service.ContextualContentProvider;
import com.freshdirect.cms.draft.domain.DraftChange;
import com.freshdirect.cms.draft.domain.DraftContext;
import com.google.common.base.Optional;

@Profile("database")
@Primary
@Service
public class DraftContentProviderService extends ContextualContentProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(DraftContentProviderService.class);

    private static final String DRAFT_PARENT_CACHE = "draftParentCache";
    private static final String DRAFT_NODES_CACHE = "draftNodes";

    @Autowired
    private ContentProviderService contentProviderService;

    @Autowired
    private ContextService contextService;

    @Autowired
    private DraftApplicatorService draftApplicatorService;

    @Autowired
    private DraftChangeExtractorService draftChangeExtractorService;

    @Autowired
    private DraftContextHolder draftContextHolder;

    @Autowired
    private DraftService draftService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private CacheManager cacheManager;

    @Override
    public boolean containsContentKey(ContentKey key) {
        Assert.notNull(key, "Content Key is mandatory parameter");
        boolean existsOnMain = contentProviderService.containsContentKey(key);
        boolean existsOnDraft = false;
        DraftContext currentDraftContext = draftContextHolder.getDraftContext();
        if (!currentDraftContext.isMainDraft()) {
            Set<ContentKey> changedKeysOnDraft = collectKeysCreatedOnDraft(draftContextHolder.getDraftContext().getDraftId());
            existsOnDraft = changedKeysOnDraft.contains(key);
        }
        return existsOnMain || existsOnDraft;
    }

    @Override
    public Map<Attribute, Object> getAllAttributesForContentKey(ContentKey contentKey) {
        Assert.notNull(contentKey, "ContentKey parameter can't be null!");
        Optional<Map<ContentKey, Map<Attribute, Object>>> nodesOnDraft = getDraftNodesFromCache();
        if (nodesOnDraft.isPresent() && nodesOnDraft.get().containsKey(contentKey)) {
            return nodesOnDraft.get().get(contentKey);
        }
        Map<Attribute, Object> mainNode = contentProviderService.getAllAttributesForContentKey(contentKey);
        Map<ContentKey, Map<Attribute, Object>> resultNode = new HashMap<ContentKey, Map<Attribute, Object>>();
        resultNode.put(contentKey, mainNode);
        if (!draftContextHolder.getDraftContext().isMainDraft()) {
            List<DraftChange> draftChanges = draftService.getDraftChanges(draftContextHolder.getDraftContext().getDraftId());
            resultNode.putAll(draftApplicatorService.applyDraftChangesToContentNodes(draftChanges, resultNode));
            putNodeIntoDraftCache(contentKey, resultNode.get(contentKey));
        }
        return resultNode.get(contentKey);
    }

    @Override
    public Optional<Object> getAttributeValue(ContentKey contentKey, Attribute attribute) {
        if (draftContextHolder.getDraftContext().isMainDraft()) {
            return contentProviderService.getAttributeValue(contentKey, attribute);
        }

        Map<Attribute, Object> attributeWithValue = getAttributeValues(contentKey, Arrays.asList(attribute));
        return Optional.fromNullable(attributeWithValue.get(attribute));
    }

    @Override
    public Map<Attribute, Object> getAttributeValues(ContentKey contentKey, List<? extends Attribute> attributes) {
        if (draftContextHolder.getDraftContext().isMainDraft()) {
            return contentProviderService.getAttributeValues(contentKey, attributes);
        }

        Map<Attribute, Object> nodeWithAllAttributes = getAllAttributesForContentKey(contentKey);
        Map<Attribute, Object> resultAttributes = new HashMap<Attribute, Object>();
        for (Attribute attributeInQuestion : attributes) {
            if (nodeWithAllAttributes.containsKey(attributeInQuestion)) {
                resultAttributes.put(attributeInQuestion, nodeWithAllAttributes.get(attributeInQuestion));
            }
        }
        return resultAttributes;
    }

    @Override
    public Set<ContentKey> getContentKeys() {
        Set<ContentKey> allKeys = new HashSet<ContentKey>(contentProviderService.getContentKeys());
        if (!draftContextHolder.getDraftContext().equals(DraftContext.MAIN)) {
            Set<ContentKey> changedKeysOnDraft = collectKeysCreatedOnDraft(draftContextHolder.getDraftContext().getDraftId());

            for (ContentKey key : changedKeysOnDraft) {
                if (!allKeys.contains(key)) {
                    allKeys.add(key);
                }
            }
        }
        return Collections.unmodifiableSet(allKeys);
    }

    @Override
    public Set<ContentKey> getContentKeysByType(ContentType type) {
        Set<ContentKey> allKeys = new HashSet<ContentKey>(getContentKeys());
        Set<ContentKey> contentKeysByType = new HashSet<ContentKey>();
        for (ContentKey key : allKeys) {
            if (key.type == type) {
                contentKeysByType.add(key);
            }
        }
        return contentKeysByType;
    }

    @Override
    public Set<ContentKey> getParentKeys(ContentKey key) {
        Set<ContentKey> parents = new HashSet<ContentKey>(contentProviderService.getParentKeys(key));
        if (draftContextHolder.getDraftContext().isMainDraft()) {
            return parents;
        } else {
            Set<ContentKey> changedKeysOnDraft = collectKeysCreatedOnDraft(draftContextHolder.getDraftContext().getDraftId());

            for (ContentKey changedKeyOnDraft : changedKeysOnDraft) {
                Set<ContentKey> childrenOfKey = getChildKeys(changedKeyOnDraft, true);
                // parent on main, but not a parent on draft
                if (parents.contains(changedKeyOnDraft) && !childrenOfKey.contains(key)) {
                    parents.remove(changedKeyOnDraft);
                } else if (childrenOfKey.contains(key)) { // not a parent on main, but parent on draft
                    parents.add(changedKeyOnDraft);
                }
            }
        }
        return parents;
    }

    @Override
    public void initializeContent() {
        // Nothing to initialize
    }

    @Override
    public boolean isOrphan(final ContentKey contentKey, ContentKey storeKey) {
        if (draftContextHolder.getDraftContext().isMainDraft()) {
            return contentProviderService.isOrphan(contentKey, storeKey);
        }
        Assert.notNull(contentKey, "Missing content key");
        Assert.isTrue(storeKey == null || ContentType.Store == storeKey.type, "Bad store key type");

        final Set<ContentKey> rootKeySet = RootContentKey.selectRootContentKeys(storeKey);

        boolean orphan = true;

        if (rootKeySet.contains(contentKey)) {
            orphan = false;
        } else {
            List<List<ContentKey>> allContextsOf = findContextsOf(contentKey);
            Set<ContentKey> topKeys = contextService.selectTopKeysOf(contentKey, allContextsOf);

            topKeys.retainAll(rootKeySet);
            orphan = topKeys.isEmpty();
        }

        return orphan;
    }

    @Override
    public List<List<ContentKey>> findContextsOf(final ContentKey contentKey) {
        if (draftContextHolder.getDraftContext().isMainDraft()) {
            return contentProviderService.findContextsOf(contentKey);
        }
        Map<ContentKey, Set<ContentKey>> parents = buildParentIndexFor(contentKey);
        return contextService.findContextsOf(contentKey, parents);
    }

    @Override
    public boolean isReadOnlyContent() {
        return contentProviderService.isReadOnlyContent();
    }

    @Override
    public Optional<ContentChangeSetEntity> updateContent(LinkedHashMap<ContentKey, Map<Attribute, Object>> payload, ContentUpdateContext context) {
        Optional<ContentChangeSetEntity> updateResult = Optional.absent();

        if (draftContextHolder.getDraftContext().isMainDraft()) {
            updateResult = contentProviderService.updateContent(payload, context);
            cacheManager.getCache(DRAFT_NODES_CACHE).clear();
            cacheManager.getCache(DRAFT_PARENT_CACHE).clear();
        } else {
            Map<ContentKey, Map<Attribute, Object>> originalNodes = collectOriginalNodes(payload);
            List<DraftChange> draftChanges = draftChangeExtractorService.extractChangesFromRequest(payload, originalNodes, draftContextHolder.getDraftContext(),
                    context.getAuthor());
            if (!draftChanges.isEmpty()) {
                validateContent(payload);

                originalNodes = collectOriginalNodes(payload);
                draftChanges = draftChangeExtractorService.extractChangesFromRequest(payload, originalNodes, draftContextHolder.getDraftContext(), context.getAuthor());
                invalidateDraftNodesCacheEntry(draftContextHolder.getDraftContext());
                draftService.saveDraftChange(draftChanges);
                updateDraftParentCacheForKeys(payload.keySet(), collectChildKeysOf(originalNodes));
            }
        }

        try {
            eventPublisher.publishEvent(new ContentChangedEvent(this, draftContextHolder.getDraftContext(), payload.keySet()));
        } catch (Exception e) {
            LOGGER.error("Failed to notify preview about cms change", e);
        }

        return updateResult;
    }

    public void updateDraftParentCacheForKeys(Set<ContentKey> contentKeys, Set<ContentKey> additionalKeys) {
        Set<ContentKey> effectedKeys = collectKeysForCacheInvalidation(contentKeys, additionalKeys);
        invalidateDraftParentCacheForKeysOnDraft(effectedKeys);
        for (ContentKey contentKey : effectedKeys) {
            buildParentIndexFor(contentKey);
        }
    }

    public void invalidateDraftNodesCacheEntry(DraftContext draftContext) {
        Cache draftNodesCache = cacheManager.getCache(DRAFT_NODES_CACHE);
        ValueWrapper nodesOfDraftContext = draftNodesCache.get(draftContext);
        if (nodesOfDraftContext != null) {
            draftNodesCache.evict(draftContextHolder.getDraftContext());
        }
    }

    private void invalidateDraftParentCacheForKeysOnDraft(Set<ContentKey> contentKeys) {
        Cache draftParentCache = cacheManager.getCache(DRAFT_PARENT_CACHE);
        for (ContentKey contentKey : contentKeys) {
            draftParentCache.evict(contentKey.toString() + draftContextHolder.getDraftContext().getDraftName());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Map<ContentKey, Set<ContentKey>> buildParentIndexFor(final ContentKey contentKey) {
        Cache draftParentCache = cacheManager.getCache(DRAFT_PARENT_CACHE);
        final String cacheKey = contentKey.toString() + draftContextHolder.getDraftContext().getDraftName();
        ValueWrapper valueWrapper = draftParentCache.get(cacheKey);
        if (valueWrapper != null) {
            return (Map<ContentKey, Set<ContentKey>>) valueWrapper.get();
        } else {
            Set<ContentKey> actualParents = getParentKeys(contentKey);
            Map<ContentKey, Set<ContentKey>> parentIndex = new HashMap<ContentKey, Set<ContentKey>>();
            if (!actualParents.isEmpty()) {
                for (ContentKey parent : actualParents) {
                    parentIndex.putAll(buildParentIndexFor(parent));
                }
            }
            parentIndex.put(contentKey, actualParents);

            // cache parent index
            draftParentCache.put(cacheKey, parentIndex);

            return parentIndex;
        }
    }

    private Map<ContentKey, Map<Attribute, Object>> collectOriginalNodes(Map<ContentKey, Map<Attribute, Object>> payload) {
        Map<ContentKey, Map<Attribute, Object>> originalNodes = new HashMap<ContentKey, Map<Attribute, Object>>();
        for (ContentKey contentKey : payload.keySet()) {
            Map<Attribute, Object> originalNode = getAllAttributesForContentKey(contentKey);
            originalNodes.put(contentKey, originalNode);
        }
        return originalNodes;
    }

    private Optional<Map<ContentKey, Map<Attribute, Object>>> getDraftNodesFromCache() {
        Cache draftNodesCache = cacheManager.getCache(DRAFT_NODES_CACHE);
        ValueWrapper nodesOfDraftContext = draftNodesCache.get(draftContextHolder.getDraftContext());
        if (nodesOfDraftContext != null) {
            return Optional.fromNullable((Map<ContentKey, Map<Attribute, Object>>) nodesOfDraftContext.get());
        }
        return Optional.absent();
    }

    private void putNodeIntoDraftCache(final ContentKey contentKey, final Map<Attribute, Object> nodeAttributes) {
        Cache draftNodesCache = cacheManager.getCache(DRAFT_NODES_CACHE);
        ValueWrapper nodesOfDraftContext = draftNodesCache.get(draftContextHolder.getDraftContext());
        if (nodesOfDraftContext == null) {
            draftNodesCache.put(draftContextHolder.getDraftContext(), new HashMap<ContentKey, Map<Attribute, Object>>() {

                {
                    put(contentKey, nodeAttributes);
                }
            });
        } else {
            Map<ContentKey, Map<Attribute, Object>> cachedNodesOnDraft = (Map<ContentKey, Map<Attribute, Object>>) nodesOfDraftContext.get();
            cachedNodesOnDraft.put(contentKey, nodeAttributes);
        }
    }



    private Set<ContentKey> collectKeysForCacheInvalidation(Set<ContentKey> contentKeys, Collection<ContentKey> additionalKeys) {
        Set<ContentKey> keysToUpdate = new HashSet<ContentKey>();
        keysToUpdate.addAll(contentKeys);

        // extend scope with child keys recursively
        collectReachableKeys(contentKeys, keysToUpdate);

        // include additional keys if available
        if (additionalKeys != null) {
            keysToUpdate.addAll(additionalKeys);
        }

        return keysToUpdate;
    }

    /**
     * Collect child keys of content nodes referenced by content keys
     *
     * @param contentKeys
     * @return
     */
    private Set<ContentKey> collectChildKeysOf(Collection<ContentKey> contentKeys, boolean navigable) {
        Set<ContentKey> result = new HashSet<ContentKey>();

        for (ContentKey contentKey : contentKeys) {
            Set<ContentKey> childKeys = getChildKeys(contentKey, navigable);
            result.addAll(childKeys);
        }

        return result;
    }

    /**
     * Collect child keys recursively into the reachableKeys parameter.
     *
     * @param rootKeys
     * @param reachableKeys
     */
    private void collectReachableKeys(Collection<ContentKey> rootKeys, Set<ContentKey> reachableKeys) {
        Set<ContentKey> childKeys = collectChildKeysOf(rootKeys, true);
        if (childKeys != null && !childKeys.isEmpty()) {
            reachableKeys.addAll(childKeys);
            collectReachableKeys(childKeys, reachableKeys);
        }
    }

    /**
     * Collect child key of content node referenced by key
     *
     * @param key
     * @param navigableOnly
     * @return
     */
    @Override
    public Set<ContentKey> getChildKeys(ContentKey key, boolean navigableOnly) {
        List<Relationship> relationships = contentTypeInfoService.selectRelationships(key.type, navigableOnly);
        Map<Attribute, Object> valueMap = getAttributeValues(key, relationships);

        return getChildKeys(key, valueMap, navigableOnly);
    }

    /**
     * Gather content keys modified on draft including ones
     * indirectly created, that is only appearing as child keys.
     *
     * @param draftId
     * @return
     */
    private Set<ContentKey> collectKeysCreatedOnDraft(final long draftId) {
        
        List<DraftChange> draftChanges = draftService.getDraftChanges(draftId);
        final Set<ContentKey> keySet = new HashSet<ContentKey>();
        
        for (final DraftChange change : draftChanges) {
            ContentKey draftContentKey = ContentKeyFactory.get(change.getContentKey());
            keySet.add(draftContentKey);

            Attribute attr = contentTypeInfoService.findAttributeByName(draftContentKey.type, change.getAttributeName()).orNull();
            if (attr instanceof Relationship) {
                List<ContentKey> childKeys = DraftChangeToContentNodeApplicator.getContentKeysFromRelationshipValue((Relationship) attr, change.getValue());
                for (ContentKey childKey : childKeys) {
                    if (!contentProviderService.containsContentKey(childKey)) {
                        keySet.add(childKey);
                    }
                }
            }
        }
        return keySet;
    }
}
