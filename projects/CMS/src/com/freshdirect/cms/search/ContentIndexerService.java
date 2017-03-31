/*
 * Created on Mar 22, 2005
 */
package com.freshdirect.cms.search;

import com.freshdirect.cms.application.CmsRequestI;
import com.freshdirect.cms.application.CmsResponseI;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.cms.application.service.ProxyContentService;
import com.freshdirect.cms.index.IndexerService;
import com.freshdirect.cms.index.configuration.IndexerConfiguration;

/**
 * Proxy content service that automatically updates a search index
 * (via a specified {@link com.freshdirect.cms.search.ContentSearchServiceI})
 * when the {@link #handle(CmsRequestI)} method is invoked.
 */
public class ContentIndexerService extends ProxyContentService {

	private final IndexerService indexerService = IndexerService.getInstance();

	/**
	 * ContentIndexerService
	 * 
	 * @param service the proxied content service
	 */
	public ContentIndexerService(ContentServiceI service) {
		super(service);
	}

	/* (non-Javadoc)
	 * @see com.freshdirect.cms.application.ContentServiceI#handle(com.freshdirect.cms.application.CmsRequestI)
	 */
    public CmsResponseI handle(CmsRequestI request) {

        CmsResponseI response = super.handle(request);

        if (DraftContext.MAIN == request.getDraftContext()) {
            indexerService.partialIndex(request.getNodes(), IndexerConfiguration.getDefaultConfiguration());
        }

        return response;
    }

}