package com.freshdirect.transadmin.web;

import java.util.List;

import com.freshdirect.transadmin.service.DomainManagerI;

public abstract class AbstractDomainFormController extends AbstractFormController {
	
	private DomainManagerI domainManagerService;
	
	public DomainManagerI getDomainManagerService() {
		return domainManagerService;
	}

	public void setDomainManagerService(DomainManagerI domainManagerService) {
		this.domainManagerService = domainManagerService;
	}
	
	public List saveDomainObject(Object domainObject) {
		getDomainManagerService().saveEntity(domainObject);
		return null;
	}
	
	
}
