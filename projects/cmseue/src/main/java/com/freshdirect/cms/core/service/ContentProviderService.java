package com.freshdirect.cms.core.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.freshdirect.cms.changecontrol.domain.ContentUpdateContext;
import com.freshdirect.cms.changecontrol.entity.ContentChangeSetEntity;
import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.domain.ContentTypes;
import com.freshdirect.cms.core.domain.Relationship;
import com.freshdirect.cms.core.domain.RootContentKey;
import com.google.common.base.Optional;

@Service
public class ContentProviderService extends ContextualContentProvider {

    private static final String PARENT_KEYS_CACHE_NAME = "parentKeysCache";

    private static final Logger LOGGER = LoggerFactory.getLogger(ContentProviderService.class);

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private ContentProvider contentProvider;

    @Autowired
    private ContextService contextService;

    @Override
    @PostConstruct
    public void initializeContent() {
        contentProvider.loadAll();
        buildParentKeysCache();
    }

    private void buildParentKeysCache() {
        LOGGER.debug("Begin building parent keys map");

        final Cache parentKeysCache = cacheManager.getCache(PARENT_KEYS_CACHE_NAME);

        Map<ContentKey, Set<ContentKey>> parentKeysMap = contentProvider.generateParentKeysMap();

        LOGGER.debug("Populate parentKeys cache");

        for (Map.Entry<ContentKey, Set<ContentKey>> entry : parentKeysMap.entrySet()) {
            parentKeysCache.put(entry.getKey(), entry.getValue());
        }
        LOGGER.debug("Parent map building completed");

    }

    @Override
    public boolean isReadOnlyContent() {
        return !(contentProvider instanceof UpdatableContentProvider);
    }

    /**
     * Return all content keys that refer to this content key as parent
     */
    @Override
    @SuppressWarnings("unchecked")
    public Set<ContentKey> getParentKeys(ContentKey key) {
        final Cache cache = cacheManager.getCache(PARENT_KEYS_CACHE_NAME);
        final ValueWrapper cachedEntry = cache.get(key);

        Set<ContentKey> result = Collections.<ContentKey>emptySet();

        if (cachedEntry == null) {
            result = contentProvider.getParentKeys(key);
        } else {
            result = (Set<ContentKey>) cachedEntry.get();
        }

        return Collections.unmodifiableSet(result);
    }

    @Override
    public boolean containsContentKey(ContentKey key) {
        Assert.notNull(key, "Content Key is mandatory parameter");
        return contentProvider.getContentKeys().contains(key);
    }

    @Override
    public Set<ContentKey> getContentKeys() {
        return Collections.unmodifiableSet(contentProvider.getContentKeys());
    }

    @Override
    public Set<ContentKey> getContentKeysByType(ContentType type) {
        return Collections.unmodifiableSet(contentProvider.getContentKeysByType(type));
    }

    @Override
    public Optional<Object> getAttributeValue(ContentKey contentKey, Attribute attribute) {
        // handle the reverse-lookup case
        if (ContentType.Brand == contentKey.type && ContentTypes.Brand.producer.equals(attribute)) {
            return doReverseLookupThroughSingleRelationship(ContentType.Producer, (Relationship) ContentTypes.Producer.brand, contentKey);
        } else {
            return contentProvider.getAttributeValue(contentKey, attribute);
        }
    }

    @Override
    public Map<Attribute, Object> getAttributeValues(ContentKey contentKey, List<? extends Attribute> attributes) {
        Map<Attribute, Object> values = contentProvider.getAttributeValues(contentKey, attributes);

        // handle the reverse-lookup case
        if (ContentType.Brand == contentKey.type && attributes.contains(ContentTypes.Brand.producer)) {
            Optional<Object> optionalValue = getAttributeValue(contentKey, ContentTypes.Brand.producer);
            if (optionalValue.isPresent()) {
                values.put(ContentTypes.Brand.producer, optionalValue.get());
            }
        }

        return values;
    }

    @Override
    public Map<Attribute, Object> getAllAttributesForContentKey(ContentKey contentKey) {
        Map<Attribute, Object> values = contentProvider.getAllAttributesForContentKey(contentKey);

        // handle the reverse-lookup case
        if (ContentType.Brand == contentKey.type) {
            Optional<Object> optionalValue = getAttributeValue(contentKey, ContentTypes.Brand.producer);
            if (optionalValue.isPresent()) {
                values.put(ContentTypes.Brand.producer, optionalValue.get());
            }
        }

        return values;
    }

    /**
     * Loads the contentNodes by the provided contentKeys
     *
     * @param contentKeys
     *            the keys to load the nodes
     * @param loadingStrategy
     *            the method of loading the nodes. If LOAD_ALL_AND_SELECT_NEEDED, this method loads _all_ nodes, then selects what is needed. If LOAD_NEEDED, loads only the nodes
     *            which contentKeys were provided. There can be performance differences in the two approach. Choose wisely
     * @return the contentNodes for the contentKeys, if found, empty map otherwise
     */
    public Map<ContentKey, Map<Attribute, Object>> getAllAttributesForContentKeys(Set<ContentKey> contentKeys, MassLoadingStrategy loadingStrategy) {
        if (loadingStrategy.equals(MassLoadingStrategy.LOAD_ALL_AND_SELECT_NEEDED)) {
            Map<ContentKey, Map<Attribute, Object>> allNode = contentProvider.loadAll();
            Map<ContentKey, Map<Attribute, Object>> resultNodes = new HashMap<ContentKey, Map<Attribute, Object>>();
            for (ContentKey keyInQuestion : contentKeys) {
                if (allNode.containsKey(keyInQuestion)) {
                    resultNodes.put(keyInQuestion, allNode.get(keyInQuestion));
                }
            }
            return resultNodes;
        } else {
            return contentProvider.getAllAttributesForContentKeys(contentKeys);
        }
    }

    /**
     * Method to determine if content key is orphan. A content is regarded orphan if none of its contexts leads to a root key.
     *
     * @param contentKey
     *            the key
     *
     * @param storeKey
     *            restrict orphan test to the specified store key
     * @return
     */
    @Override
    public boolean isOrphan(ContentKey contentKey, ContentKey storeKey) {
        Assert.notNull(contentKey, "Missing content key");
        Assert.isTrue(storeKey == null || ContentType.Store == storeKey.type, "Bad store key type");

        final Set<ContentKey> rootKeySet = RootContentKey.selectRootContentKeys(storeKey);

        boolean orphan = true;

        if (rootKeySet.contains(contentKey)) {
            orphan = false;
        } else {
            Set<ContentKey> topKeys = contextService.selectTopKeysOf(contentKey, findContextsOf(contentKey));
            topKeys.retainAll(rootKeySet);
            orphan = topKeys.isEmpty();
        }

        return orphan;
    }

    /**
     * Perform save on changed content
     *
     * @param payload
     *            Attribute values to be persisted
     * @param context
     */
    @Override
    public Optional<ContentChangeSetEntity> updateContent(LinkedHashMap<ContentKey, Map<Attribute, Object>> payload, ContentUpdateContext context) {
        Assert.notNull(payload, "Content payload cannot be null");
        Assert.notNull(context, "Update context must be provided");

        if (!(contentProvider instanceof UpdatableContentProvider)) {
            return Optional.absent();
        }

        validateContent(payload);

        Optional<ContentChangeSetEntity> contentChange = ((UpdatableContentProvider) contentProvider).updateContent(payload, context);

        return contentChange;
    }

    private Optional<Object> doReverseLookupThroughSingleRelationship(ContentType parentType, Relationship relationship, ContentKey childContentKey) {
        Assert.notNull(parentType, "Parent Content Type cannot be null");
        Assert.notNull(relationship, "Relationship cannot be null");
        Assert.notNull(childContentKey, "Child Content Key cannot be null");

        Set<ContentKey> parentKeys = getContentKeysByType(parentType);
        for (ContentKey candidateKey : parentKeys) {
            Optional<Object> optionalValue = contentProvider.getAttributeValue(candidateKey, relationship);
            if (optionalValue.isPresent() && childContentKey.equals(optionalValue.get())) {
                return Optional.<Object>of(candidateKey);
            }
        }

        return Optional.absent();
    }

}
