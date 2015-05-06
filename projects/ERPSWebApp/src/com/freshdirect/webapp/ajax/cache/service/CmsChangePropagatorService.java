package com.freshdirect.webapp.ajax.cache.service;

import java.util.HashSet;
import java.util.Set;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.cache.ContentCacheService;
import com.freshdirect.cms.search.ContentSearchServiceI;
import com.freshdirect.framework.conf.FDRegistry;

public class CmsChangePropagatorService {

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
			Set<ContentNodeI> contentNodes = decodeToContentNodes(contentKeys);
			
			// invalidate content node cache
			ContentCacheService.defaultService().invalidateContentNode(contentNodes);

			// reindex search service
			// @see ContentIndexerService
			searchService.index(contentNodes, false);
			searchService.indexSpelling(contentNodes);

			try {
				CmsManager.getInstance().rebuildIndices(contentNodes);
			} catch (Exception exc) {
			}
		}
	}


	
	
	private Set<ContentNodeI> decodeToContentNodes(Set<String> contentKeys) {
		Set<ContentNodeI> contentNodes = new HashSet<ContentNodeI>();
		for (String contentKeyText : contentKeys) {
			ContentKey contentKey = ContentKey.decode(contentKeyText);
			ContentNodeI node = contentKey.lookupContentNode();
			contentNodes.add(node);
		}
		return contentNodes;
	}

}
