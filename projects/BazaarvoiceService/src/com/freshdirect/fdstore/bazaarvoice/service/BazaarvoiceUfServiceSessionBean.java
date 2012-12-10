package com.freshdirect.fdstore.bazaarvoice.service;

import com.freshdirect.framework.core.SessionBeanSupport;

public class BazaarvoiceUfServiceSessionBean extends SessionBeanSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4735004684491213606L;
	
	public BazaarvoiceFeedProcessResult processFile() {
		 return new UploadFeedProcessTask().process();
	}

	public BazaarvoiceFeedProcessResult processRatings() {
		BazaarvoiceFeedProcessResult result = new DownloadFeedProcessTask().process();
		if (result.isSuccess()) {
			result = new StoreFeedTask().process();
		}
		return result;
	}
}
