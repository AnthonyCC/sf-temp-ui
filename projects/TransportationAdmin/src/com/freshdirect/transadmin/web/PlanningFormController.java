package com.freshdirect.transadmin.web;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.freshdirect.transadmin.model.TrnDispatchPlan;
import com.freshdirect.transadmin.service.DispatchManagerI;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.util.TransStringUtil;

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
	
	public List saveDomainObject(Object domainObject) {
		TrnDispatchPlan tmpCommand = (TrnDispatchPlan)domainObject;
		List errorList = new ArrayList();
		try {
			String strSourceDate = TransStringUtil.getServerDate(tmpCommand.getPlanDate());
			if(canIgnoreError(tmpCommand)) {
				savePlan(tmpCommand);
			} else {
				Collection sourceData = dispatchManagerService.getPlanList(strSourceDate);
				if(sourceData.isEmpty()) {
					errorList.add(getMessage("app.actionmessage.106", new Object[]{strSourceDate}));
					tmpCommand.setErrorDate(tmpCommand.getPlanDate());					
				} else {
					savePlan(tmpCommand);
				}
			}			
			
		} catch(ParseException parseExp) {
			errorList.add("Error Processing Request");
		}
		
		return errorList;
	}
	
	public void saveErrorMessage(HttpServletRequest request, Object msg) {
		List messages = (List)msg;
		if (messages != null) {
			messages.add(getMessage("app.actionmessage.109", new Object[]{}));
		}
		super.saveErrorMessage(request, msg);
	}
	
	private boolean canIgnoreError(TrnDispatchPlan tmpCommand) {
		return "true".equalsIgnoreCase(tmpCommand.getIgnoreErrors()) && tmpCommand.getPlanDate().equals(tmpCommand.getErrorDate());
	}
	
	private void savePlan(TrnDispatchPlan tmpCommand) throws ParseException {
		tmpCommand.setDispatchDay(TransStringUtil.getDayofWeek(tmpCommand.getPlanDate()));
		getDomainManagerService().saveEntity(tmpCommand);			
		tmpCommand.setIgnoreErrors(null);
		tmpCommand.setErrorDate(null);
	}
	
		
	public DomainManagerI getDomainManagerService() {
		return domainManagerService;
	}

	public void setDomainManagerService(DomainManagerI domainManagerService) {
		this.domainManagerService = domainManagerService;
	}	

}
