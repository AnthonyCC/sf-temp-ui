package com.freshdirect.webapp.ajax.cache.service;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.cms.cache.ContentCacheService;
import com.freshdirect.cms.index.IndexerService;
import com.freshdirect.cms.index.PartialIndexerService;
import com.freshdirect.framework.util.log.LoggerFactory;

public class CmsChangePropagatorService {

    private static final Logger LOGGER = LoggerFactory.getInstance(CmsChangePropagatorService.class);

    private static final CmsChangePropagatorService INSTANCE = new CmsChangePropagatorService();

    private final CmsManager cmsManager;
    private final IndexerService indexerService = PartialIndexerService.getInstance();

    private CmsChangePropagatorService() {
        cmsManager = CmsManager.getInstance();
    }

    public static CmsChangePropagatorService defaultService() {
        return INSTANCE;
    }

    public void propagateCmsChangesToCaches(Set<String> contentKeys, DraftContext draftContext) {
        if (contentKeys != null) {
            LOGGER.info("Receive CMS content keys: " + contentKeys);

            Set<ContentNodeI> contentNodes = decodeToContentNodes(contentKeys, draftContext);

            LOGGER.debug(".. transformed to " + contentNodes.size() + " nodes");

            // invalidate content node cache
            ContentCacheService.defaultService().invalidateContentNode(contentNodes);

            // reindex search service
            // @see ContentIndexerService
            indexerService.index(contentNodes);

            try {
                cmsManager.rebuildIndices(contentNodes);
            } catch (Exception exc) {
                LOGGER.error("Error occurred while rebuilding search indices", exc);
            }
        } else {
            LOGGER.warn("No keys captured");
        }
    }

    private Set<ContentNodeI> decodeToContentNodes(Set<String> contentKeys, DraftContext draftContext) {
        Set<ContentNodeI> contentNodes = new HashSet<ContentNodeI>();
        for (String contentKeyText : contentKeys) {
            ContentKey contentKey = ContentKey.getContentKey(contentKeyText);
            ContentNodeI node = cmsManager.getContentNode(contentKey, draftContext);
            if (node != null) {
                contentNodes.add(node);
            } else {
                LOGGER.warn("Content node not found " + contentKeyText);
            }
        }
        return contentNodes;
    }

}
