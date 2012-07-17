package com.freshdirect.fdstore.coremetrics.service;

import com.freshdirect.framework.core.SessionBeanSupport;

public class CoremetricsCdfServiceSessionBean extends SessionBeanSupport {

	private static final long serialVersionUID = -6830271094648271298L;

	public boolean processCdf(){
		 return new CdfProcessTask().process();
	}
	
}
