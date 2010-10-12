package com.freshdirect.transadmin.web;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.util.StringUtils;
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
import com.freshdirect.transadmin.service.ZoneManagerI;
import com.freshdirect.transadmin.util.DispatchPlanUtil;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.web.model.DispatchCommand;
import com.freshdirect.transadmin.web.model.WebPlanInfo;
import com.freshdirect.transadmin.util.EnumResourceType;

public class PlanningFormController extends AbstractFormController {

	private DomainManagerI domainManagerService;

	private DispatchManagerI dispatchManagerService;

	private EmployeeManagerI employeeManagerService;
	
	private ZoneManagerI zoneManagerService;
	
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

	private void printRequestParameters(HttpServletRequest request) {
		Enumeration e=request.getParameterNames();
		while(e.hasMoreElements()) {
			String parameter=(String)e.nextElement();
			System.out.println("Parameter :"+parameter);
			String[] values=request.getParameterValues(parameter);
			for(int i=0;i<values.length;i++)
				if(values.length==1) {
					System.out.println("{ "+values[i]+" }");
				} else	if(i==0)
					System.out.print("{ "+values[i]+", ");
				else if(i==(values.length-1)) {
					System.out.print(values[i]+"}");
				}
		}
	}
	protected Map referenceData(HttpServletRequest request) throws ServletException {
		
		Collection zones=getDomainManagerService().getZones();
		Collection activeZoneCodes = getZoneManagerService().getActiveZoneCodes();
    	if(zones != null && activeZoneCodes != null) {
    		Iterator _iterator = zones.iterator();
    		Zone _tmpZone = null;
    		while(_iterator.hasNext()) {
    			_tmpZone = (Zone)_iterator.next();
    			if(!activeZoneCodes.contains(_tmpZone.getZoneCode())) {
    				_iterator.remove();
    			}
    		}
    	}
		
		printRequestParameters(request);
		Map refData = new HashMap();
		refData.put("days", domainManagerService.getDays());
		refData.put("zones", zones);
		refData.put("regions", domainManagerService.getRegions());
		
		List drivers=DispatchPlanUtil.getSortedResources(employeeManagerService.getEmployeesByRole(EnumResourceType.DRIVER.getName()));
		drivers.addAll(DispatchPlanUtil.getSortedResources(employeeManagerService.getEmployeesByRole(EnumResourceType.MANAGER.getName())));
		
		refData.put("supervisors", DispatchPlanUtil.getSortedResources(employeeManagerService.getSupervisors()));
		refData.put("drivers",drivers);
		refData.put("helpers", DispatchPlanUtil.getSortedResources(employeeManagerService.getEmployeesByRole(EnumResourceType.HELPER.getName())));
		refData.put("runners", DispatchPlanUtil.getSortedResources(employeeManagerService.getEmployeesByRole(EnumResourceType.RUNNER.getName())));
		return refData;
	}


	public void printEmployeeInfo(List data) {

		Iterator it=data.iterator();
		while(it.hasNext()) {
			EmployeeInfo empInfo=(EmployeeInfo)it.next();
//			System.out.println(empInfo.getLastName()+" "+empInfo.getFirstName());
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

	/*protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {

		if("true".equalsIgnoreCase(((WebPlanInfo)command).getZoneModified())) {
			((WebPlanInfo)command).setZoneModified("false");
			preProcessDomainObject(command);
			ModelAndView mav = new ModelAndView(getSuccessView(), errors.getModel());
			mav.getModel().put(this.getCommandName(), command);
			mav.getModel().putAll(referenceData(request));

			return mav;
		} else {
			return super.onSubmit(request, response, command, errors);
		}

	}*/
	public List saveDomainObject(Object command) {

		List errorList = null;

		try {
			WebPlanInfo _command = (WebPlanInfo)command;
			Plan domainObject = getPlan(_command);
			getDispatchManagerService().savePlan(domainObject, _command.getReferenceContextId());
			_command.setPlanId(domainObject.getPlanId());
			_command.setReferenceContextId(null);
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
		model= DispatchPlanUtil.reconstructWebPlanInfo(model,zone,model.getFirstDeliveryTimeModified(),null,employeeManagerService,zoneManagerService);

		//set userId to command object
		model.setUserId(getUserId(request));
	}

	protected boolean isFormChangeRequest(HttpServletRequest request, Object command) {

		WebPlanInfo _command=(WebPlanInfo)command;
		if("true".equalsIgnoreCase(_command.getZoneModified())||"true".equalsIgnoreCase(_command.getFirstDeliveryTimeModified())) {
			return true;
		}
		else
			return isFormChangeRequest(request);
	}

	protected void onFormChange(HttpServletRequest request, HttpServletResponse response, Object command)
	throws Exception {

		WebPlanInfo _command=(WebPlanInfo)command;
		_command.setZoneModified("false");
		_command.setFirstDeliveryTimeModified("false");
	}


	private Plan getPlan(WebPlanInfo command) throws Exception {
		return DispatchPlanUtil.getPlan(command);
	}

	protected String getIdFromRequest(HttpServletRequest request){
		String id = request.getParameter("id");
		if(TransStringUtil.isEmpty(id)) {
			id=request.getParameter("planId");
		}
		return id;
	}

	public ZoneManagerI getZoneManagerService() {
		return zoneManagerService;
	}

	public void setZoneManagerService(ZoneManagerI zoneManagerService) {
		this.zoneManagerService = zoneManagerService;
	}

}

