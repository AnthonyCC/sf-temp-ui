package com.freshdirect.transadmin.web;

import com.freshdirect.transadmin.service.DomainManagerI;

public abstract class AbstractDomainFormController extends AbstractFormController {
	
	private DomainManagerI domainManagerService;
	
	public DomainManagerI getDomainManagerService() {
		return domainManagerService;
	}

	public void setDomainManagerService(DomainManagerI domainManagerService) {
		this.domainManagerService = domainManagerService;
	}
	
	public void saveDomainObject(Object domainObject) {
		getDomainManagerService().saveEntity(domainObject);
	}
	
	
}
