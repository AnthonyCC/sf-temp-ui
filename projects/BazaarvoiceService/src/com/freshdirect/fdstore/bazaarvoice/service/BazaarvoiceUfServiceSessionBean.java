package com.freshdirect.fdstore.bazaarvoice.service;

import com.freshdirect.framework.core.SessionBeanSupport;

public class BazaarvoiceUfServiceSessionBean extends SessionBeanSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4735004684491213606L;
	
	public UploadFeedProcessResult processFile(){
		 return new UploadFeedProcessTask().process();
	}

}
