package com.freshdirect.webapp.ajax.browse;

import org.apache.log4j.Logger;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.ajax.BaseJsonServlet.HttpErrorResponse;
import com.freshdirect.webapp.ajax.browse.data.BrowseData;
import com.freshdirect.webapp.ajax.browse.data.CmsFilteringFlowResult;

public class BrowsePopulator {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getInstance(BrowsePopulator.class);

	public static BrowseData createBrowseData(CmsFilteringFlowResult result) throws HttpErrorResponse {
		return result.getBrowseDataPrototype();
	}

}
