package com.freshdirect.cms.core.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.util.Assert;

import com.freshdirect.cms.changecontrol.domain.ContentUpdateContext;
import com.freshdirect.cms.changecontrol.entity.ContentChangeSetEntity;
import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.domain.ContentTypes;
import com.freshdirect.cms.core.domain.RootContentKey;
import com.google.common.base.Optional;

public class NodeCollectionContentProviderService extends ContextualContentProvider implements UpdatableContentProvider {

    private Map<ContentKey, Map<Attribute, Object>> contentNodes;
    private Map<ContentKey, Set<ContentKey>> parentKeys;

    public NodeCollectionContentProviderService(ContentTypeInfoService contentTypeInfoService, ContentKeyParentsCollectorService contentKeyParentsCollectorService,
            ContextService contextService, Map<ContentKey, Map<Attribute, Object>> contentNodes) {
        // set services
        super.contextService = contextService;
        super.contentTypeInfoService = contentTypeInfoService;
        super.contentKeyParentsCollectorService = contentKeyParentsCollectorService;

        // set content base
        this.contentNodes = new HashMap<ContentKey, Map<Attribute, Object>>(contentNodes);

        // calculate parent keys
        this.parentKeys = contentKeyParentsCollectorService.createParentKeysMap(contentNodes);
    }

    public Map<ContentKey, Map<Attribute, Object>> filterNodesToStore(final ContentKey storeKey) {
        Assert.notNull(storeKey);
        Assert.isTrue(ContentType.Store == storeKey.type);

        Map<ContentKey, Map<Attribute, Object>> filteredNodes = new HashMap<ContentKey, Map<Attribute, Object>>();

        for (Map.Entry<ContentKey, Map<Attribute, Object>> candidate : contentNodes.entrySet()) {
            ContentKey candidateKey = candidate.getKey();
            boolean isGoodToAdd = false;

            switch (candidateKey.type) {
                // judge store-tree members
                case Store:
                    isGoodToAdd = candidateKey == storeKey;
                    break;
                case Department:
                case Category:
                case Product:
                    isGoodToAdd = isMemberOfStore(candidateKey, storeKey);
                    break;
                default:
                    // anyway, include
                    isGoodToAdd = true;
                    break;
            }

            if (isGoodToAdd) {
                if (ContentType.Product == candidateKey.type) {
                    Map<Attribute, Object> clone = cloneNode(candidateKey, candidate.getValue(), storeKey);
                    Map<Attribute, Object> filteredPrimaryHomeNode = selectPrimaryHomeByStore(candidateKey, clone, storeKey);
                    filteredNodes.put(candidateKey, filteredPrimaryHomeNode);
                } else {
                    filteredNodes.put(candidateKey, candidate.getValue());
                }
            }
        }
        return filteredNodes;
    }

    @Override
    public List<List<ContentKey>> findContextsOf(ContentKey contentKey) {
        return contextService.findContextsOf(contentKey, parentKeys);
    }

    @Override
    public Map<Attribute, Object> getAllAttributesForContentKey(ContentKey contentKey) {
        return contentNodes.containsKey(contentKey) ? contentNodes.get(contentKey) : new HashMap<Attribute, Object>();
    }

    @Override
    public Optional<Object> getAttributeValue(ContentKey contentKey, Attribute attribute) {
        return Optional.fromNullable(contentNodes.get(contentKey).get(attribute));
    }

    @Override
    public Map<Attribute, Object> getAttributeValues(ContentKey contentKey, List<? extends Attribute> attributes) {
        Map<Attribute, Object> result = new HashMap<Attribute, Object>();
        Map<Attribute, Object> nodeAttributes = getAllAttributesForContentKey(contentKey);
        for (Attribute attributeInQuestion : attributes) {
            if (nodeAttributes.containsKey(attributeInQuestion)) {
                result.put(attributeInQuestion, nodeAttributes.get(attributeInQuestion));
            }
        }
        return result;
    }

    @Override
    public Set<ContentKey> getContentKeys() {
        return contentNodes.keySet();
    }

    @Override
    public Set<ContentKey> getContentKeysByType(ContentType type) {
        Set<ContentKey> keys = new HashSet<ContentKey>();
        for (ContentKey key : contentNodes.keySet()) {
            if (key.type.equals(type)) {
                keys.add(key);
            }
        }
        return keys;
    }

    @Override
    public Set<ContentKey> getParentKeys(ContentKey key) {
        return parentKeys.get(key) == null ? Collections.<ContentKey> emptySet() : parentKeys.get(key);
    }

    @Override
    public void initializeContent() {
        // Nothing to initialize
    }

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

    @Override
    public boolean isReadOnlyContent() {
        return false;
    }

    @Override
    public Optional<ContentChangeSetEntity> updateContent(LinkedHashMap<ContentKey, Map<Attribute, Object>> payload, ContentUpdateContext context) {
        for (ContentKey key : payload.keySet()) {
            if (!contentNodes.containsKey(key) || contentNodes.get(key) == null) {
                contentNodes.put(key, new HashMap<Attribute, Object>());
            }
            for (Attribute attribute : payload.get(key).keySet()) {
                contentNodes.get(key).put(attribute, payload.get(key).get(attribute));
            }
        }

        // recalculate parent keys
        this.parentKeys = this.contentKeyParentsCollectorService.createParentKeysMap(contentNodes);

        // NOTE: do we need to return changesets?
        return Optional.absent();
    }

    private Map<Attribute, Object> cloneNode(ContentKey contentKey, Map<Attribute, Object> originalNode, ContentKey storeKey) {
        return new HashMap<Attribute, Object>(originalNode);
    }

    @SuppressWarnings("unchecked")
    private Map<Attribute, Object> selectPrimaryHomeByStore(ContentKey contentKey, Map<Attribute, Object> nodeData, ContentKey storeKey) {
        if (nodeData.containsKey(ContentTypes.Product.PRIMARY_HOME)) {
            Set<ContentKey> parentsByStore = filterParentsByStore(contentKey, storeKey);
            List<ContentKey> primaryHomes = (List<ContentKey>) nodeData.get(ContentTypes.Product.PRIMARY_HOME);
            List<ContentKey> primaryHomeByStore = new ArrayList<ContentKey>();
            for (ContentKey primaryHome : primaryHomes) {
                if (parentsByStore.contains(primaryHome)) {
                    primaryHomeByStore.add(primaryHome);
                }
            }
            nodeData.put(ContentTypes.Product.PRIMARY_HOME, primaryHomeByStore);
        }
        return nodeData;
    }

    private Set<ContentKey> filterParentsByStore(ContentKey contentKey, ContentKey storeKey) {
        List<List<ContentKey>> contextsOfCandidate = findContextsOf(contentKey);

        Set<ContentKey> filteredParentKeys = new HashSet<ContentKey>();

        for (List<ContentKey> context : contextsOfCandidate) {
            if (context.get(context.size() - 1).equals(storeKey)) {
                filteredParentKeys.add(context.get(1));
            }
        }

        return filteredParentKeys;
    }

    private boolean isMemberOfStore(ContentKey productKey, ContentKey storeKey) {
        return !filterParentsByStore(productKey, storeKey).isEmpty();
    }

    @Override
    public boolean containsContentKey(ContentKey key) {
        return contentNodes.containsKey(key);
    }
}
