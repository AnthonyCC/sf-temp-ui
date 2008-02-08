package com.freshdirect.transadmin.web;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

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
	
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
				
		String messageKey = isNew(command) ? "app.actionmessage.101"
				: "app.actionmessage.102";

		preProcessDomainObject(command);
		List errorList = saveDomainObject(command);

		ModelAndView mav = new ModelAndView(getSuccessView(), errors.getModel());
		
		mav.getModel().put(this.getCommandName(), command);
		mav.getModel().putAll(referenceData(request));
		if(errorList == null || errorList.isEmpty()) {
			saveMessage(request, getMessage(messageKey,
					new Object[] { getDomainObjectName() }));
		} else {
			saveErrorMessage(request, errorList);
		}
		return mav;
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
			Collection sourceData = dispatchManagerService.getPlanList(strSourceDate);
			if(sourceData.isEmpty() && !"true".equalsIgnoreCase(tmpCommand.getIgnoreErrors())) {
				errorList.add("Plan does not exists for date "+strSourceDate);
			}
		} catch(ParseException parseExp) {
			errorList.add("Error Processing Request");
		}
		if(errorList.isEmpty()) {
			getDomainManagerService().saveEntity(tmpCommand);			
			tmpCommand.setIgnoreErrors(null);
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
