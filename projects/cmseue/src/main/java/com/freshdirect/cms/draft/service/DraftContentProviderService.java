package com.freshdirect.cms.draft.service;

import java.util.Arrays;
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
import com.freshdirect.cms.core.domain.ContentNodeComparatorUtil;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.domain.Relationship;
import com.freshdirect.cms.core.domain.RelationshipCardinality;
import com.freshdirect.cms.core.domain.RootContentKey;
import com.freshdirect.cms.core.service.ContentProviderService;
import com.freshdirect.cms.core.service.ContextualContentProvider;
import com.freshdirect.cms.core.service.ParentIndexBuilder;
import com.freshdirect.cms.draft.domain.DraftChange;
import com.freshdirect.cms.draft.domain.DraftContext;
import com.google.common.base.Optional;
import com.google.common.collect.Sets;

@Profile("database")
@Primary
@Service
public class DraftContentProviderService extends ContextualContentProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(DraftContentProviderService.class);

    private static final String DRAFT_PARENT_CACHE = "draftParentCache";
    private static final String DRAFT_NODES_CACHE = "draftNodes";

    // This represents the MAIN branch
    @Autowired
    private ContentProviderService contentProviderService;

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
    public void initializeContent() {
        // Nothing to initialize
    }

    @Override
    public boolean isReadOnlyContent() {
        return contentProviderService.isReadOnlyContent();
    }

    /**
     *  Performance optimized 'contains' check, same semantics as:
     *  {@code contentProviderService.containsContentKey(key) || collectKeysCreatedOnDraft(draftId).contains(key) }
     *  but returns early with the result if found, to avoid having to build up the whole expensive set every time
     */
    @Override
    public boolean containsContentKey(ContentKey key) {
        Assert.notNull(key, "Content Key is mandatory parameter");

        if (contentProviderService.containsContentKey(key)) {
            // exists on MAIN branch, return early with the result
            return true;
        }

        DraftContext currentDraftContext = draftContextHolder.getDraftContext();
        if (!currentDraftContext.isMainDraft()) {
            Map<ContentKey, Map<Attribute, Object>> draftOverrides = getDraftNodes(currentDraftContext);

            if (draftOverrides.containsKey(key)) {
                // exists on current DRAFT branch, return early with the result
                return true;
            }

            for (Map<Attribute, Object> draftOverride : draftOverrides.values()) {
                for (Map.Entry<Attribute, Object> entry : draftOverride.entrySet()) {
                    Attribute attribute = entry.getKey();
                    if (attribute instanceof Relationship) {
                        if (((Relationship) attribute).getCardinality() == RelationshipCardinality.ONE) {
                            if (key.equals((ContentKey) entry.getValue())) {
                                // indirect child of a node that exists on current DRAFT branch, return with the result
                                return true;
                            }
                        } else {
                            if (((List<?>) entry.getValue()).contains(key)) {
                                // indirect child of a node that exists on current DRAFT branch, return with the result
                                return true;
                            }
                        }
                    }
                }
            }
        }
        // did not find it
        return false;
    }

    @Override
    public Map<Attribute, Object> getAllAttributesForContentKey(ContentKey contentKey) {
        Assert.notNull(contentKey, "ContentKey parameter can't be null!");
        DraftContext draftContext = draftContextHolder.getDraftContext();
        if (draftContext.isMainDraft()) {
            return contentProviderService.getAllAttributesForContentKey(contentKey);
        }

        Map<Attribute, Object> resultNode = new HashMap<Attribute, Object>(contentProviderService.getAllAttributesForContentKey(contentKey));
        Map<Attribute, Object> draftNode = getDraftNodes(draftContext).get(contentKey);
        if (draftNode != null) {
            resultNode.putAll(draftNode);
        }
        return resultNode;
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
            allKeys.addAll(collectKeysChangedOnDraft(draftContextHolder.getDraftContext()));
        }
        return allKeys;
    }

    @Override
    public Set<ContentKey> getContentKeysByType(ContentType type) {
        Set<ContentKey> contentKeysByType = new HashSet<ContentKey>();
        for (ContentKey key : getContentKeys()) {
            if (key.type == type) {
                contentKeysByType.add(key);
            }
        }
        return contentKeysByType;
    }

    @Override
    public Set<ContentKey> getParentKeys(ContentKey childKey) {
        DraftContext draftContext = draftContextHolder.getDraftContext();
        if (draftContext.isMainDraft()) {
            return contentProviderService.getParentKeys(childKey);
        }

        Set<ContentKey> mainParents = contentProviderService.getParentKeys(childKey);
        Set<ContentKey> draftParents = getDraftParents(draftContext).get(childKey);
        if (draftParents == null) {
            draftParents = Collections.emptySet();
        }
        Set<ContentKey> mergedParents = new HashSet<ContentKey>(draftParents);

        for (ContentKey parentKey : mainParents) {
            Map<Attribute, Object> parentFull = getAllAttributesForContentKey(parentKey);
            if (isChildOf(parentFull, childKey)) {
                mergedParents.add(parentKey);
            }
        }
        return mergedParents;
    }

    @SuppressWarnings("unchecked")
    private boolean isChildOf(Map<Attribute, Object> parentNode, ContentKey childKey) {
        for (Map.Entry<Attribute, Object> entry : parentNode.entrySet()) {
            Attribute attribute = entry.getKey();
            Object value = entry.getValue();
            if (value != null && attribute instanceof Relationship && ((Relationship) attribute).isNavigable()) {
                if (RelationshipCardinality.ONE == ((Relationship) attribute).getCardinality()) {
                    if (((ContentKey) value).equals(childKey)) {
                        return true;
                    }
                } else {
                    if (((List<ContentKey>) value).contains(childKey)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean isOrphan(final ContentKey contentKey, ContentKey storeKey) {
        if (draftContextHolder.getDraftContext().isMainDraft()) {
            return contentProviderService.isOrphan(contentKey, storeKey);
        }
        Assert.notNull(contentKey, "Missing content key");
        Assert.isTrue(storeKey == null || ContentType.Store == storeKey.type, "Bad store key type");

        final Set<ContentKey> rootKeySet = RootContentKey.selectRootContentKeys(storeKey);
        if (rootKeySet.contains(contentKey)) {
            return false;
        }

        Set<ContentKey> topKeys = contextService.selectTopKeysOf(contentKey, findContextsOf(contentKey));
        topKeys.retainAll(rootKeySet);
        return topKeys.isEmpty();
    }

    @Override
    public Optional<ContentChangeSetEntity> updateContent(LinkedHashMap<ContentKey, Map<Attribute, Object>> payload, ContentUpdateContext context) {
        Optional<ContentChangeSetEntity> updateResult = Optional.absent();
        DraftContext draftContext = draftContextHolder.getDraftContext();

        if (draftContext.isMainDraft()) {
            updateResult = contentProviderService.updateContent(payload, context);
            sendContentChangedNotification(draftContext, payload.keySet());
        } else {
            if (ContentNodeComparatorUtil.isChanged(payload, collectOriginalNodes(payload))) {
                // Note: validate changes the payload, so we have to recalculate anything dependent on it!
                validateContent(payload);

                Map<ContentKey, Map<Attribute, Object>> originalNodes = collectOriginalNodes(payload);
                Set<ContentKey> originalChildKeys = collectChildKeysOf(originalNodes);

                invalidateDraftCaches(draftContext);
                draftService.saveDraftChange(draftContext, draftChangeExtractorService.extractChangesFromRequest(payload, originalNodes, draftContext, context.getAuthor()));
                sendContentChangedNotification(draftContext, Sets.union(payload.keySet(), originalChildKeys));
            }
        }
        return updateResult;
    }

    private void sendContentChangedNotification(DraftContext draftContext, Set<ContentKey> contentKeys) {
        try {
            eventPublisher.publishEvent(new ContentChangedEvent(this, draftContext, contentKeys));
        } catch (Exception e) {
            LOGGER.error("Failed to notify preview about cms change", e);
        }
    }

    public void invalidateDraftCaches(DraftContext draftContext) {
        cacheManager.getCache(DRAFT_NODES_CACHE).evict(draftContext);
        cacheManager.getCache(DRAFT_PARENT_CACHE).evict(draftContext);
    }

    private Map<ContentKey, Map<Attribute, Object>> collectOriginalNodes(Map<ContentKey, Map<Attribute, Object>> payload) {
        Map<ContentKey, Map<Attribute, Object>> originalNodes = new HashMap<ContentKey, Map<Attribute, Object>>();
        for (ContentKey contentKey : payload.keySet()) {
            Map<Attribute, Object> originalNode = getAllAttributesForContentKey(contentKey);
            originalNodes.put(contentKey, originalNode);
        }
        return originalNodes;
    }

    @SuppressWarnings("unchecked")
    private Map<ContentKey, Map<Attribute, Object>> getDraftNodes(final DraftContext draftContext) {
        Cache draftNodesCache = cacheManager.getCache(DRAFT_NODES_CACHE);
        ValueWrapper cacheValue = draftNodesCache.get(draftContext);
        if (cacheValue != null) {
            return (Map<ContentKey, Map<Attribute, Object>>) cacheValue.get();
        }

        List<DraftChange> draftChanges = draftService.getDraftChanges(draftContext.getDraftId());
        Map<ContentKey, Map<Attribute, Object>> draftOverrides = draftApplicatorService.convertDraftChanges(draftChanges);
        draftNodesCache.put(draftContext, draftOverrides);
        return draftOverrides;
    }

    @SuppressWarnings("unchecked")
    private Map<ContentKey, Set<ContentKey>> getDraftParents(final DraftContext draftContext) {
        Cache draftParentCache = cacheManager.getCache(DRAFT_PARENT_CACHE);
        ValueWrapper cacheValue = draftParentCache.get(draftContext);
        if (cacheValue != null) {
            return (Map<ContentKey, Set<ContentKey>>) cacheValue.get();
        }

        Map<ContentKey, Set<ContentKey>> draftParents = ParentIndexBuilder.createParentKeysMap(getDraftNodes(draftContext));
        draftParentCache.put(draftContext, draftParents);
        return draftParents;
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
    @SuppressWarnings("unchecked")
    private Set<ContentKey> collectKeysChangedOnDraft(final DraftContext draftContext) {
        final Set<ContentKey> keySet = new HashSet<ContentKey>();

        for (Map.Entry<ContentKey, Map<Attribute, Object>> draftNodeEntry : getDraftNodes(draftContext).entrySet()) {
            keySet.add(draftNodeEntry.getKey());

            for (Map.Entry<Attribute, Object> attributeEntry : draftNodeEntry.getValue().entrySet()) {
                Attribute attribute = attributeEntry.getKey();

                if (attribute instanceof Relationship) {
                    Relationship relationship = (Relationship) attribute;
                    if (relationship.getCardinality() == RelationshipCardinality.ONE) {
                        ContentKey childKey = (ContentKey) attributeEntry.getValue();
                        if (!contentProviderService.containsContentKey(childKey)) {
                            keySet.add(childKey);
                        }
                    } else {
                        List<ContentKey> childKeys = (List<ContentKey>) attributeEntry.getValue();
                        for (ContentKey childKey : childKeys) {
                            if (!contentProviderService.containsContentKey(childKey)) {
                                keySet.add(childKey);
                            }
                        }
                    }
                }
            }
        }
        return keySet;
    }
}
