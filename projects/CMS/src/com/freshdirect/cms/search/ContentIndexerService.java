/*
 * Created on Mar 22, 2005
 */
package com.freshdirect.cms.search;

import java.text.MessageFormat;

import org.apache.log4j.Logger;

import com.freshdirect.cms.application.CmsRequestI;
import com.freshdirect.cms.application.CmsResponseI;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.cms.application.service.ProxyContentService;
import com.freshdirect.cms.index.IndexerService;
import com.freshdirect.cms.index.PartialIndexerService;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * Proxy content service that automatically updates a search index
 * (via a specified {@link com.freshdirect.cms.search.ContentSearchServiceI})
 * when the {@link #handle(CmsRequestI)} method is invoked.
 */
public class ContentIndexerService extends ProxyContentService {

    private static final Logger LOGGER = LoggerFactory.getInstance(ContentIndexerService.class);

	private final IndexerService indexerService = PartialIndexerService.getInstance();

	/**
	 * @param service the proxied content service
	 * @param indexerService {@link ContentSearchServiceI} to update
	 */
	public ContentIndexerService(ContentServiceI service) {
		super(service);
	}

	/* (non-Javadoc)
	 * @see com.freshdirect.cms.application.ContentServiceI#handle(com.freshdirect.cms.application.CmsRequestI)
	 */
    public CmsResponseI handle(CmsRequestI request) {
        LOGGER.debug(MessageFormat.format("Starting service handles {0} request", request));

        CmsResponseI response = super.handle(request);

        if (DraftContext.MAIN == request.getDraftContext()) {
            indexerService.index(request.getNodes());
        }

        LOGGER.debug(MessageFormat.format("Ending service return {0} response", response));
        return response;
    }

}