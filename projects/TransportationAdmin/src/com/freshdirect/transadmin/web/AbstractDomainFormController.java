package com.freshdirect.transadmin.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;

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
		List errorList = null;
		try {
			getDomainManagerService().saveEntity(domainObject);
		} catch (DataIntegrityViolationException objExp) {
			errorList = new ArrayList();
			errorList.add(this.getMessage("app.actionmessage.119", new Object[]{this.getDomainObjectName()}));
		}
		return errorList;
	}
	
	
}
