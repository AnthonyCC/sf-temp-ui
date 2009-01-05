package com.freshdirect.transadmin.web;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpSessionRequiredException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.transadmin.model.Dispatch;
import com.freshdirect.transadmin.model.EmployeeInfo;
import com.freshdirect.transadmin.model.Plan;
import com.freshdirect.transadmin.model.PlanResource;
import com.freshdirect.transadmin.model.Zone;
import com.freshdirect.transadmin.model.ZonetypeResource;
import com.freshdirect.transadmin.service.DispatchManagerI;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.service.EmployeeManagerI;
import com.freshdirect.transadmin.util.DispatchPlanUtil;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.web.model.DispatchCommand;
import com.freshdirect.transadmin.web.model.WebPlanInfo;
import com.freshdirect.transadmin.util.EnumResourceType;

public class PlanningFormController extends AbstractFormController {
	
	private DomainManagerI domainManagerService;
	
	private DispatchManagerI dispatchManagerService;
	
	private EmployeeManagerI employeeManagerService;
	public DispatchManagerI getDispatchManagerService() {
		return dispatchManagerService;
	}

	public void setDispatchManagerService(DispatchManagerI dispatchManagerService) {
		this.dispatchManagerService = dispatchManagerService;
	}
	
	public EmployeeManagerI getEmployeeManagerService() {
		return employeeManagerService;
	}

	public void setEmployeeManagerService(EmployeeManagerI employeeManagerService) {
		this.employeeManagerService = employeeManagerService;
	}	
	
	protected Map referenceData(HttpServletRequest request) throws ServletException {
		
		Map refData = new HashMap();		
		refData.put("days", domainManagerService.getDays());
		refData.put("zones", domainManagerService.getZones());
		refData.put("regions", domainManagerService.getRegions());
		refData.put("supervisors", DispatchPlanUtil.getSortedResources(employeeManagerService.getEmployeesByRole(EnumResourceType.SUPERVISOR.getName())));
		refData.put("drivers", DispatchPlanUtil.getSortedResources(employeeManagerService.getEmployeesByRole(EnumResourceType.DRIVER.getName())));
		refData.put("helpers", DispatchPlanUtil.getSortedResources(employeeManagerService.getEmployeesByRole(EnumResourceType.HELPER.getName())));
		refData.put("runners", DispatchPlanUtil.getSortedResources(employeeManagerService.getEmployeesByRole(EnumResourceType.RUNNER.getName())));
		return refData;
	}
	

	public void printEmployeeInfo(List data) {
		
		Iterator it=data.iterator();
		while(it.hasNext()) {
			EmployeeInfo empInfo=(EmployeeInfo)it.next();
			System.out.println(empInfo.getLastName()+" "+empInfo.getFirstName());
		}
		
	}
	public Object getBackingObject(String id) {
		WebPlanInfo planInfo=getCommand(getDispatchManagerService().getPlan(id));
		return planInfo;
	}
	
	private WebPlanInfo getCommand(Plan plan) {
		
		Zone zone=null;
		if(plan.getZone()!=null) {
			zone=domainManagerService.getZone(plan.getZone().getZoneCode());
		}
		return DispatchPlanUtil.getWebPlanInfo(plan,zone,employeeManagerService);
	}	

	
	public Object getDefaultBackingObject() {
		return new WebPlanInfo();
	}
	
	public boolean isNew(Object command) {
		WebPlanInfo modelIn = (WebPlanInfo)command;
		return (TransStringUtil.isEmpty(modelIn.getPlanId()));
	}
	
	public String getDomainObjectName() {
		return "Plan";
	}
	
	public List saveDomainObject(Object command) {
		
		List errorList = null;
		try {
			
			boolean isNew = isNew(command);
			Plan domainObject=getPlan((WebPlanInfo)command);
			if(!isNew) {
				getDomainManagerService().saveEntity(domainObject);
			} else {
				getDispatchManagerService().savePlan(domainObject);
			}
		} catch (Exception objExp) {
			objExp.printStackTrace();
			errorList = new ArrayList();
			errorList.add(this.getMessage("sys.error.1001", new Object[]{this.getDomainObjectName()}));
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
	
	/*
	private boolean canIgnoreError(WebPlanInfo tmpCommand) {
		return "true".equalsIgnoreCase(tmpCommand.getIgnoreErrors()) && tmpCommand.getPlanDate().equals(tmpCommand.getErrorDate());
		
	}
	
	private void savePlan(WebPlanInfo tmpCommand) throws ParseException {
		//tmpCommand.setDispatchDay(TransStringUtil.getDayofWeek(tmpCommand.getPlanDate()));
		Plan plan=DispatchPlanUtil.getPlan(tmpCommand);
		//getDomainManagerService().saveEntityEx(plan);			
		
		if(TransStringUtil.isEmpty(plan.getPlanId())) {
			Set resources=plan.getPlanResources();
			plan.setPlanResources(null);
			getDomainManagerService().saveEntityEx(plan);
			if(resources!=null && resources.size()>0) {
				Iterator it=resources.iterator();
				while(it.hasNext()) {
					PlanResource pr=(PlanResource)it.next();
					pr.getId().setContextId(plan.getPlanId());
				}
			}
			plan.setPlanResources(resources);
			getDomainManagerService().saveEntityList(plan.getPlanResources());
			
		}
		else {
			getDomainManagerService().saveEntity(plan);
		}
		tmpCommand.setIgnoreErrors(null);
		tmpCommand.setErrorDate(null);
	}
	private List savePlan(WebPlanInfo command) {	
		List errorList = null;
		try {
			
			boolean isNew = isNew(command);
			Plan domainObject=DispatchPlanUtil.getPlan(command);
			if(!isNew) {
				getDomainManagerService().saveEntity(domainObject);
			} else {
				getDispatchManagerService().savePlan(domainObject);
			}
		} catch (Exception objExp) {
			objExp.printStackTrace();
			errorList = new ArrayList();
			errorList.add(this.getMessage("sys.error.1001", new Object[]{this.getDomainObjectName()}));
		}
		return errorList;
	}
	*/	
	public DomainManagerI getDomainManagerService() {
		return domainManagerService;
	}

	public void setDomainManagerService(DomainManagerI domainManagerService) {
		this.domainManagerService = domainManagerService;
	}	
	
	protected void onBind(HttpServletRequest request, Object command) {
	
		
		WebPlanInfo model = (WebPlanInfo) command;
		Zone zone=null;
		if(!TransStringUtil.isEmpty(model.getIsBullpen()) &&"Y".equalsIgnoreCase(model.getIsBullpen())) {
			model.setZoneCode("");
			model.setZoneName("");
		} else 
			
		if(!TransStringUtil.isEmpty(model.getZoneCode())) {
			zone=domainManagerService.getZone(model.getZoneCode());
		}
		model= DispatchPlanUtil.reconstructWebPlanInfo(model,zone,employeeManagerService);
		
	}
	
	

	

	
	
	
	
	/*public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		super.initBinder(request,binder);
    }*/
	
	private Plan getPlan(WebPlanInfo command) throws Exception {
		return DispatchPlanUtil.getPlan(command);
	}
	
	/*protected ModelAndView processFormSubmission(
			HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)
			throws Exception {

		if (errors.hasErrors()) {
			System.out.println(errors);
			if (logger.isDebugEnabled()) {
				logger.debug("Data binding errors: " + errors.getErrorCount());
			}
			return showForm(request, response, errors);
		}
		else if (isFormChangeRequest(request, command)) {
			logger.debug("Detected form change request -> routing request to onFormChange");
			onFormChange(request, response, command, errors);
			return showForm(request, response, errors);
		}
		else {
			logger.debug("No errors -> processing submit");
			return onSubmit(request, response, command, errors);
		}
	}*/

	/*protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		// Form submission or new form to show?
		if (isFormSubmission(request)) {
			// Fetch form object from HTTP session, bind, validate, process submission.
			try {
				Object command = getCommand(request);
				ServletRequestDataBinder binder = bindAndValidate(request, command);
				BindException errors = new BindException(binder.getBindingResult());
				return processFormSubmission(request, response, command, errors);
			}
			catch (HttpSessionRequiredException ex) {
				// Cannot submit a session form if no form object is in the session.
				if (logger.isDebugEnabled()) {
					logger.debug("Invalid submit detected: " + ex.getMessage());
				}
				return handleInvalidSubmit(request, response);
			}
		}
		
		else {
			// New form to show: render form view.
			return showNewForm(request, response);
		}
	}*/
	
	
	
}

