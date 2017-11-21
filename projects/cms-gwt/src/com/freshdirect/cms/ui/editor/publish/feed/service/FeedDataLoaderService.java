package com.freshdirect.cms.ui.editor.publish.feed.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.service.ContentProviderService;
import com.freshdirect.cms.core.service.MassLoadingStrategy;
import com.freshdirect.cms.ui.editor.publish.feed.domain.FeedPublish;

@Service
public class FeedDataLoaderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FeedDataLoaderService.class);

    @Autowired
    private ContentProviderService contentProviderService;

    /**
     * @see com.freshdirect.cms.publish.service.StoreFilterService.filterContentNodes(ContentKey, Collection<ContentNodeI>, ContentNodeSource, DraftContext)
     * @param typesToPublish
     * @param feedPublish
     * @return
     */
    @Transactional(readOnly = true)
    public Map<ContentKey, Map<Attribute, Object>> loadNodesToFeedPublish(ContentType[] typesToPublish, FeedPublish feedPublish) {

        // collect nodes for feed publish
        Set<ContentKey> candidateKeys = new HashSet<ContentKey>();
        for (ContentType type : typesToPublish) {
            //LOGGER.info("Getting keys for type: " + type);
            final Set<ContentKey> contentKeysByType = contentProviderService.getContentKeysByType(type);
            LOGGER.debug("count of keys["+type.name()+"] = " + contentKeysByType.size());
            candidateKeys.addAll(contentKeysByType);
        }

        removeExtraContentKeys(typesToPublish, candidateKeys);

        LOGGER.info("we have " + candidateKeys.size()  + " candidates.");

        Map<ContentKey, Map<Attribute, Object>> candidateNodes = contentProviderService.getAllAttributesForContentKeys(candidateKeys, MassLoadingStrategy.LOAD_NEEDED);

        // narrow node collection down to the given store
        Map<ContentKey, Map<Attribute, Object>> contentNodes = filterNodesToStore(feedPublish.getStoreKey(), candidateNodes);

        LOGGER.info("we have the final list, count=" + contentNodes.size());

        removeExtraContentNodes(typesToPublish, contentNodes);

        LOGGER.info("we have the final list (after cleanup), count=" + contentNodes.size());

        return contentNodes;
    }

    /**
     * This method is subject of removal after ensured no extra nodes
     * got into the node map
     *
     * @param typesToPublish
     * @param contentNodes
     */
    private void removeExtraContentNodes(ContentType[] typesToPublish, Map<ContentKey, Map<Attribute, Object>> contentNodes) {
        // post-condition: make sure no extra nodes got into the target collection
        Iterator<Map.Entry<ContentKey, Map<Attribute, Object>>> it = contentNodes.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<ContentKey, Map<Attribute, Object>> entry = it.next();

            boolean found = false;
            final ContentKey key = entry.getKey();
            for (ContentType t : typesToPublish) {
                if (t == key.type) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                LOGGER.debug("Removing extra node " + key);
                it.remove();
            }
        }
    }

    /**
     * This method is subject of removal after ensured no extra keys
     * got into the set
     *
     * @param typesToPublish
     * @param contentNodes
     */
    private void removeExtraContentKeys(ContentType[] typesToPublish, Set<ContentKey> candidateKeys) {
        // key cleanup phase - ensure no illegal key in among the candidates
        Iterator<ContentKey> it0 = candidateKeys.iterator();
        while (it0.hasNext()) {
            ContentKey key = it0.next();
            boolean found = false;
            for (ContentType t : typesToPublish) {
                if (t == key.type) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                LOGGER.debug("Removing extra key " + key);
                it0.remove();
            }
        }
    }

    // NOTE: duplicate of NodeCollectionContentProviderService#filterNodesToStore
    private Map<ContentKey, Map<Attribute, Object>> filterNodesToStore(final ContentKey storeKey,  Map<ContentKey, Map<Attribute, Object>> contentNodes) {
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
                // feed items
                case WebPage:
                case PickList:
                case PickListItem:
                case Anchor:
                case ImageBanner:
                case TextComponent:
                case Section:
                case Schedule:
                case DarkStore:
                    isGoodToAdd = isMemberOfStore(candidateKey, storeKey);
                    break;
                default:
                    // anyway, include
                    isGoodToAdd = true;
                    break;
            }

            if (isGoodToAdd) {
                /*if (ContentType.Product == candidateKey.type) {
                    Map<Attribute, Object> clone = clonedAndFixedNode(candidateKey, candidate.getValue(), storeKey);
                    filteredNodes.put(candidateKey, clone);
                } else {
                    filteredNodes.put(candidateKey, candidate.getValue());
                } */
                filteredNodes.put(candidateKey, candidate.getValue());
            } else {
                LOGGER.debug(".. " + candidateKey + " is not part of store " + storeKey);
            }
        }
        return filteredNodes;
    }

    // NOTE: duplicate of NodeCollectionContentProviderService#isMemberOfStore
    private boolean isMemberOfStore(ContentKey productKey, ContentKey storeKey) {
        return !filterParentsByStore(productKey, storeKey).isEmpty();
    }

    // NOTE: copied from NodeCollectionContentProviderService#filterParentsByStore
    private Set<ContentKey> filterParentsByStore(ContentKey contentKey, ContentKey storeKey) {
        List<List<ContentKey>> contextsOfCandidate = contentProviderService.findContextsOf(contentKey);

        Set<ContentKey> filteredParentKeys = new HashSet<ContentKey>();

        for (List<ContentKey> context : contextsOfCandidate) {
            if (context.get(context.size() - 1).equals(storeKey)) {
                filteredParentKeys.add(context.get(1));
            }
        }

        return filteredParentKeys;
    }

}
