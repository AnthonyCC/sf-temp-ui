package com.freshdirect.fdstore.coremetrics.service;

import com.freshdirect.fdstore.coremetrics.CmContext;
import com.freshdirect.framework.core.SessionBeanSupport;

public class CoremetricsCdfServiceSessionBean extends SessionBeanSupport {

	private static final long serialVersionUID = -6830271094648271298L;

	@Deprecated
	public CdfProcessResult processCdf(){
		 return new CdfProcessTask().process();
	}
	
	public CdfProcessResult processCdf(CmContext ctx){
		 return new CdfProcessTask(ctx).process();
	}
}
