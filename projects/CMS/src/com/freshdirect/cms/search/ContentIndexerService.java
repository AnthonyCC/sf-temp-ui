/*
 * Created on Mar 22, 2005
 */
package com.freshdirect.cms.search;

import com.freshdirect.cms.application.CmsRequestI;
import com.freshdirect.cms.application.CmsResponseI;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.service.ProxyContentService;

/**
 * Proxy content service that automatically updates a search index
 * (via a specified {@link com.freshdirect.cms.search.ContentSearchServiceI})
 * when the {@link #handle(CmsRequestI)} method is invoked.
 */
public class ContentIndexerService extends ProxyContentService {

	private final ContentSearchServiceI searchService;

	/**
	 * @param service the proxied content service
	 * @param searchService {@link ContentSearchServiceI} to update
	 */
	public ContentIndexerService(ContentServiceI service, ContentSearchServiceI searchService) {
		super(service);
		this.searchService = searchService;
	}

	/* (non-Javadoc)
	 * @see com.freshdirect.cms.application.ContentServiceI#handle(com.freshdirect.cms.application.CmsRequestI)
	 */
	public CmsResponseI handle(CmsRequestI request) {
		CmsResponseI response = super.handle(request);

		searchService.index(request.getNodes());

		return response;
	}

}