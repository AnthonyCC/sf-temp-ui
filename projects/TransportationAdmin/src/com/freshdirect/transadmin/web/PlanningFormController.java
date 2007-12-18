package com.freshdirect.transadmin.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.freshdirect.transadmin.model.TrnDispatchPlan;
import com.freshdirect.transadmin.service.DispatchManagerI;
import com.freshdirect.transadmin.service.DomainManagerI;

public class PlanningFormController extends AbstractFormController {
	
	private DomainManagerI domainManagerService;
	
	private DispatchManagerI dispatchManagerService;
	
	public DispatchManagerI getDispatchManagerService() {
		return dispatchManagerService;
	}

	public void setDispatchManagerService(DispatchManagerI dispatchManagerService) {
		this.dispatchManagerService = dispatchManagerService;
	}

	protected Map referenceData(HttpServletRequest request) throws ServletException {
		Map refData = new HashMap();		
		refData.put("days", domainManagerService.getDays());
		refData.put("zones", domainManagerService.getZones());
		refData.put("drivers", dispatchManagerService.getDrivers());
		refData.put("helpers", dispatchManagerService.getHelpers());
		refData.put("timeslots", domainManagerService.getTimeSlots());
		return refData;
	}
		
	public Object getBackingObject(String id) {
		return getDispatchManagerService().getPlan(id);
	}
	
	public Object getDefaultBackingObject() {
		return new TrnDispatchPlan();
	}
	
	public boolean isNew(Object command) {
		TrnDispatchPlan modelIn = (TrnDispatchPlan)command;
		return (modelIn.getPlanId() == null);
	}
	
	public String getDomainObjectName() {
		return "Dispatch Plan";
	}
	
	public void saveDomainObject(Object domainObject) {
		getDomainManagerService().saveEntity(domainObject);
	}
	
	public DomainManagerI getDomainManagerService() {
		return domainManagerService;
	}

	public void setDomainManagerService(DomainManagerI domainManagerService) {
		this.domainManagerService = domainManagerService;
	}	

}
