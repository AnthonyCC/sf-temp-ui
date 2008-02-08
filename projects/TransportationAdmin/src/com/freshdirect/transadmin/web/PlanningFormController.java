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
import com.freshdirect.transadmin.model.TrnEmployee;
import com.freshdirect.transadmin.model.TrnTimeslot;
import com.freshdirect.transadmin.model.TrnZone;
import com.freshdirect.transadmin.service.DispatchManagerI;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.web.model.DispatchSheetCommand;

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
		return new DispatchSheetCommand(getDispatchManagerService().getPlan(id));
	}
	
	public Object getDefaultBackingObject() {
		return new DispatchSheetCommand();
	}
	
	public boolean isNew(Object command) {
		DispatchSheetCommand modelIn = (DispatchSheetCommand)command;
		return (modelIn.getPlanId() == null);
	}
	
	public String getDomainObjectName() {
		return "Dispatch Plan";
	}
	
	public List saveDomainObject(Object domainObject) {
		DispatchSheetCommand tmpCommand = (DispatchSheetCommand)domainObject;
		TrnDispatchPlan tmpPlan = getModel(tmpCommand);
		List errorList = new ArrayList();
		try {
			String strSourceDate = TransStringUtil.getServerDate(tmpPlan.getPlanDate());
			Collection sourceData = dispatchManagerService.getPlanList(strSourceDate);
			if(sourceData.isEmpty() && !"true".equalsIgnoreCase(tmpCommand.getIgnoreErrors())) {
				errorList.add("Plan does not exists for date "+strSourceDate);
			}
		} catch(ParseException parseExp) {
			errorList.add("Error Processing Request");
		}
		if(errorList.isEmpty()) {
			getDomainManagerService().saveEntity(tmpPlan);
			tmpCommand.setPlanId(tmpPlan.getPlanId());
			tmpCommand.setIgnoreErrors(null);
		}
		return errorList;
	}
	
	public TrnDispatchPlan getModel(DispatchSheetCommand tmpCommand) {
		TrnDispatchPlan tmpDispatch = null;
		if(tmpCommand.getPlanId() != null) {
			getDispatchManagerService().getPlan(""+tmpCommand.getPlanId().intValue());
		} else {
			tmpDispatch = new TrnDispatchPlan();
		}
		
		if(tmpCommand.getSupervisorId() != null) {
			tmpDispatch.setTrnSupervisor(new TrnEmployee(tmpCommand.getSupervisorId()));
		}
		if(tmpCommand.getZoneId() != null) {
			tmpDispatch.setTrnZone(new TrnZone(tmpCommand.getZoneId()));
		}
		if(tmpCommand.getSlotId() != null) {
			tmpDispatch.setTrnTimeslot(new TrnTimeslot(tmpCommand.getSlotId()));
		}
		
		if(tmpCommand.getDriverId() != null) {
			tmpDispatch.setTrnDriver(new TrnEmployee(tmpCommand.getDriverId()));
		}
		if(tmpCommand.getPrimaryHelperId() != null) {
			tmpDispatch.setTrnPrimaryHelper(new TrnEmployee(tmpCommand.getPrimaryHelperId()));
		}
		
		if(tmpCommand.getSecondaryHelperId() != null) {
			tmpDispatch.setTrnSecondaryHelper(new TrnEmployee(tmpCommand.getSecondaryHelperId()));
		}
				
			
		tmpDispatch.setPlanId(tmpCommand.getPlanId());
		
		try {
			tmpDispatch.setPlanDate(tmpCommand.getPlanDate() != null ? TransStringUtil.getDate(tmpCommand.getPlanDate()) : null);
			tmpDispatch.setDispatchDay(tmpDispatch.getPlanDate() != null 
					? TransStringUtil.getDayofWeek(tmpDispatch.getPlanDate()) : null);
		} catch(ParseException parseExp) {
			//do nothing
		}
		return tmpDispatch;
	}
	
	public DomainManagerI getDomainManagerService() {
		return domainManagerService;
	}

	public void setDomainManagerService(DomainManagerI domainManagerService) {
		this.domainManagerService = domainManagerService;
	}	

}
