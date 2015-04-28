package com.freshdirect.webapp.ajax.cache.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.cache.ContentCacheService;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.cms.search.ContentSearchServiceI;
import com.freshdirect.cms.search.IBackgroundProcessor;
import com.freshdirect.cms.search.SearchRelevancyList;
import com.freshdirect.cms.search.SynonymDictionary;
import com.freshdirect.fdstore.content.ContentSearch;
import com.freshdirect.framework.conf.FDRegistry;

public class CmsChangePropagatorService {

	private static final CmsChangePropagatorService INSTANCE = new CmsChangePropagatorService();

	private final ContentSearchServiceI searchService;
	
	private final IBackgroundProcessor adminTool;

	private CmsChangePropagatorService() {
		searchService = (ContentSearchServiceI) FDRegistry.getInstance().getService("com.freshdirect.cms.search.SearchService", ContentSearchServiceI.class);
		
		adminTool = (IBackgroundProcessor) FDRegistry.getInstance().getService(IBackgroundProcessor.class);
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
			searchService.index(contentNodes, false);
			searchService.indexSpelling(contentNodes);

			// invalidate search relevancy scores
			for (ContentNodeI node : contentNodes) {
				if (SearchRelevancyList.isRelatedContentNode(node)) {
					ContentSearch.getInstance().invalidateRelevancyScores();
					break;
				}
			}

			// rebuild synonyms
			for (ContentNodeI node : contentNodes) {
		        final ContentType type = node.getKey().getType();
				if (FDContentTypes.SYNONYM.equals(type)) {
		            Set<String> keywords = new HashSet<String>();
		            String[] fromValues = SynonymDictionary.getSynonymFromValues(node);
		            keywords.addAll(Arrays.asList(fromValues));

		            adminTool.backgroundReindex(keywords);
		            
		            break;
		        } else if (FDContentTypes.FDFOLDER.equals(type) && SynonymDictionary.SYNONYM_LIST_KEY.equals(node.getKey().getId())) {
		            adminTool.backgroundReindex();

		            break;
		        }
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
