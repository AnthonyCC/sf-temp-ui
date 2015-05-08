package com.freshdirect.webapp.ajax.cache.service;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.cache.ContentCacheService;
import com.freshdirect.cms.search.ContentSearchServiceI;
import com.freshdirect.framework.conf.FDRegistry;
import com.freshdirect.framework.util.log.LoggerFactory;

public class CmsChangePropagatorService {
	private static final Logger LOGGER = LoggerFactory.getInstance(CmsChangePropagatorService.class);
	
	private static final CmsChangePropagatorService INSTANCE = new CmsChangePropagatorService();

	private final ContentSearchServiceI searchService;

	private CmsChangePropagatorService() {
		searchService = (ContentSearchServiceI) FDRegistry.getInstance().getService("com.freshdirect.cms.search.SearchService", ContentSearchServiceI.class);
	}

	public static CmsChangePropagatorService defaultService() {
		return INSTANCE;
	}

	public void propagateCmsChangesToCaches(Set<String> contentKeys) {
		if (contentKeys != null) {
			LOGGER.info("Receive CMS content keys: " + contentKeys);

			Set<ContentNodeI> contentNodes = decodeToContentNodes(contentKeys);

			LOGGER.debug(".. transformed to " + contentNodes.size() + " nodes");

			// invalidate content node cache
			ContentCacheService.defaultService().invalidateContentNode(contentNodes);

			// reindex search service
			// @see ContentIndexerService
			searchService.index(contentNodes, false);
			searchService.indexSpelling(contentNodes);

			try {
				CmsManager.getInstance().rebuildIndices(contentNodes);
			} catch (Exception exc) {
				LOGGER.error("Error occurred while rebuilding search indices", exc);
			}
		} else {
			LOGGER.warn("No keys captured");
		}
	}


	
	
	private Set<ContentNodeI> decodeToContentNodes(Set<String> contentKeys) {
		Set<ContentNodeI> contentNodes = new HashSet<ContentNodeI>();
		for (String contentKeyText : contentKeys) {
			ContentKey contentKey = ContentKey.decode(contentKeyText);
			ContentNodeI node = contentKey.lookupContentNode();
			if (node != null) {
				contentNodes.add(node);
			} else {
				LOGGER.warn("Content node not found " + contentKeyText);
			}
		}
		return contentNodes;
	}

}
