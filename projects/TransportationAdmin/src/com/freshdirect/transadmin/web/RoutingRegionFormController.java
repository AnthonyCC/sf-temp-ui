package com.freshdirect.transadmin.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.freshdirect.transadmin.model.TrnArea;
import com.freshdirect.transadmin.model.TrnRegion;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.util.TransStringUtil;

public class RoutingRegionFormController extends AbstractFormController {
	
	private DomainManagerI domainManagerService;
			
	public DomainManagerI getDomainManagerService() {
		return domainManagerService;
	}

	public void setDomainManagerService(DomainManagerI domainManagerService) {
		this.domainManagerService = domainManagerService;
	}

	protected Map referenceData(HttpServletRequest request) throws ServletException {
		Map refData = new HashMap();
		return refData;
	}
	
	public Object getBackingObject(String id) {
		return getDomainManagerService().getRoutingRegion(id);
	}
	
	public Object getDefaultBackingObject() {
		TrnRegion routingRegion = new TrnRegion();
		routingRegion.setIsNew("true");
		return routingRegion;
	}
	
	public boolean isNew(Object command) {
		TrnRegion modelIn = (TrnRegion)command;
		return (modelIn.getCode() == null);
	}
	
	public String getDomainObjectName() {
		return "Routing Region";
	}
	
	protected void preProcessDomainObject(Object domainObject) {
		TrnRegion modelIn = (TrnRegion)domainObject;
		if(TransStringUtil.isEmpty(modelIn.getCode()) ) {
			modelIn.setCode(modelIn.getCode());
		}
	}
	
	public List saveDomainObject(HttpServletRequest request, Object domainObject) {
		List errorList = new ArrayList();
		TrnRegion modelNew = (TrnRegion)domainObject;
		
		if(errorList.size() == 0) {
			getDomainManagerService().saveEntity(modelNew);
			modelNew.setIsNew(null);
		}
		return errorList;
	}

}
