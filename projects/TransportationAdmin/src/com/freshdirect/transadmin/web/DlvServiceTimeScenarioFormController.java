package com.freshdirect.transadmin.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.freshdirect.transadmin.model.DlvServiceTimeScenario;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.service.LocationManagerI;
import com.freshdirect.transadmin.util.TransStringUtil;

public class DlvServiceTimeScenarioFormController extends AbstractFormController {
		
	private LocationManagerI locationManagerService;
	
	private DomainManagerI domainManagerService;
	
	protected Map referenceData(HttpServletRequest request) throws ServletException {
		Map refData = new HashMap();		
		refData.put("servicetimetypes", getLocationManagerService().getServiceTimeTypes());				
		refData.put("zonetypes", getDomainManagerService().getZoneTypes());
		return refData;
	}
	
	public Object getBackingObject(String id) {
		return getLocationManagerService().getServiceTimeScenario(id);
	}
	
	public Object getDefaultBackingObject() {
		DlvServiceTimeScenario scenario = new DlvServiceTimeScenario();
		scenario.setIsNew("true");
		return scenario;
	}
	
	public boolean isNew(Object command) {
		DlvServiceTimeScenario modelIn = (DlvServiceTimeScenario)command;
		return (modelIn.getCode() == null);
	}
	
	public String getDomainObjectName() {
		return "Service Time Scenario";
	}
	
	protected void preProcessDomainObject(Object domainObject) {
		DlvServiceTimeScenario modelIn = (DlvServiceTimeScenario)domainObject;
		if(TransStringUtil.isEmpty(modelIn.getCode()) ) {
			modelIn.setCode(modelIn.getCode());
		}
	}

	public LocationManagerI getLocationManagerService() {
		return locationManagerService;
	}

	public void setLocationManagerService(LocationManagerI locationManagerService) {
		this.locationManagerService = locationManagerService;
	}
	
	public List saveDomainObject(Object domainObject) {
		List errorList = new ArrayList();
		DlvServiceTimeScenario modelNew = (DlvServiceTimeScenario)domainObject;
		Collection saveDataList = new ArrayList();
		if("X".equals(modelNew.getIsDefault())) {
			DlvServiceTimeScenario modelDefault = getLocationManagerService().getDefaultServiceTimeScenario();
			if(modelDefault != null && !modelDefault.getCode().equals(modelNew.getCode())) {
				modelDefault.setIsDefault(null);
				saveDataList.add(modelDefault);
			} 
		}
		
		if("true".equals(modelNew.getIsNew())) {
			DlvServiceTimeScenario refDomain = getLocationManagerService().getServiceTimeScenario(modelNew.getCode());
			if(refDomain != null) {
				modelNew.setCode(null);
				errorList.add(this.getMessage("app.actionmessage.119", new Object[]{getDomainObjectName()}));
			}
		}
		if(errorList.size() == 0) {
			saveDataList.add(domainObject);
			getLocationManagerService().saveEntityList(saveDataList);
			modelNew.setIsNew(null);
		}
		
		return errorList;
	}

	public DomainManagerI getDomainManagerService() {
		return domainManagerService;
	}

	public void setDomainManagerService(DomainManagerI domainManagerService) {
		this.domainManagerService = domainManagerService;
	}
}
