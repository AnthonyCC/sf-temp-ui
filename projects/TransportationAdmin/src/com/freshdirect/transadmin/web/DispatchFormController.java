package com.freshdirect.transadmin.web;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestDataBinder;

import com.freshdirect.transadmin.exception.TransAdminApplicationException;
import com.freshdirect.transadmin.model.Dispatch;
import com.freshdirect.transadmin.model.Zone;
import com.freshdirect.transadmin.service.DispatchManagerI;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.service.EmployeeManagerI;
import com.freshdirect.transadmin.util.DispatchPlanUtil;
import com.freshdirect.transadmin.util.EnumResourceType;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.web.model.DispatchCommand;

public class DispatchFormController extends AbstractFormController {
	
	private DomainManagerI domainManagerService;
	
	private DispatchManagerI dispatchManagerService;
	
	private EmployeeManagerI employeeManagerService;
	
	public DispatchManagerI getDispatchManagerService() {
		return dispatchManagerService;
	}

	public void setDispatchManagerService(DispatchManagerI dispatchManagerService) {
		this.dispatchManagerService = dispatchManagerService;
	}
	
	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder binder) throws Exception {
		super.initBinder(request,binder);
	}

	protected Map referenceData(HttpServletRequest request) throws ServletException {
		Map refData = new HashMap();		
		refData.put("statuses", domainManagerService.getDispositionTypes());
		String dispDate = request.getParameter("dispDate");
		String zoneCode = request.getParameter("zoneCode");
		System.out.println("Zone code "+zoneCode);
		if (StringUtils.hasText(dispDate)) {
			refData.put("routes", domainManagerService.getAllRoutes(getServerDate(dispDate)));
			
		} else {
			refData.put("routes", domainManagerService.getAllRoutes(TransStringUtil.getCurrentServerDate()));
		}
		if (StringUtils.hasText(dispDate)) {
			refData.put("trucks",  getDispatchManagerService().getAvailableTrucks(getServerDate(dispDate)));
		} else {
			refData.put("trucks",  getDispatchManagerService().getAvailableTrucks(TransStringUtil.getCurrentServerDate()));
		}
		refData.put("supervisors", DispatchPlanUtil.getSortedResources(employeeManagerService.getSupervisors()));
		refData.put("drivers", DispatchPlanUtil.getSortedResources(employeeManagerService.getEmployeesByRole(EnumResourceType.DRIVER.getName())));
		refData.put("helpers", DispatchPlanUtil.getSortedResources(employeeManagerService.getEmployeesByRole(EnumResourceType.HELPER.getName())));
		refData.put("runners", DispatchPlanUtil.getSortedResources(employeeManagerService.getEmployeesByRole(EnumResourceType.RUNNER.getName())));

		refData.put("zones", domainManagerService.getZones());
		refData.put("regions", domainManagerService.getRegions());
		return refData;
	}
	
	private DispatchCommand getCommand(Dispatch dispatch) throws Exception{
		Zone zone=null;
		if(dispatch.getZone() != null) {
			zone=domainManagerService.getZone(dispatch.getZone().getZoneCode());
		}
		return DispatchPlanUtil.getDispatchCommand(dispatch, zone, employeeManagerService);
	}
	

	
	private Dispatch getDispatch(DispatchCommand command) throws Exception {
		return DispatchPlanUtil.getDispatch(command, domainManagerService);
	}
	
	public Object getDefaultBackingObject() {
		DispatchCommand command = new  DispatchCommand();
		command.setZoneCode("");
		command.setTruck("");
		try{
			command.setDispatchDate(TransStringUtil.getDate(new Date()));
			command.setStartTime(TransStringUtil.getServerTime(new Date()));
			command.setFirstDeliveryTime(TransStringUtil.getServerTime(new Date()));
		}catch(ParseException exp){}
		return command;
	}
	
	public Object getBackingObject(String id) {
		try{
			DispatchCommand command = getCommand(getDispatchManagerService().getDispatch(id));
			return command;
		}
		catch(Exception ex){
			ex.printStackTrace();
			throw new RuntimeException("An Error Ocuurred when trying construct command object "+ex.getMessage());
		}
		
	}
	
	public boolean isNew(Object command) {
		DispatchCommand modelIn = (DispatchCommand)command;
		return (TransStringUtil.isEmpty(modelIn.getDispatchId()));
	}

	
	public String getDomainObjectName() {
		return "DispatchCommand";
	}
	
	
	private List saveDispatch(DispatchCommand command) {	
		List errorList = null;
		try {
			
			boolean isNew = isNew(command);
			Dispatch domainObject=getDispatch(command);
			getDispatchManagerService().saveDispatch(domainObject);
			command.setDispatchId(domainObject.getDispatchId());
		} catch (TransAdminApplicationException objExp) {
			errorList = new ArrayList();
			errorList.add(objExp.getMessage());
		} catch (Exception objExp) {
			errorList = new ArrayList();
			errorList.add(this.getMessage("sys.error.1001", new Object[]{this.getDomainObjectName()}));
		}
		return errorList;
	}
	
	public List saveDomainObject(Object domainObject) {
		DispatchCommand command = (DispatchCommand) domainObject;
		return saveDispatch(command);
	}

	
	protected void onBind(HttpServletRequest request, Object command) {
		DispatchCommand model = (DispatchCommand) command;
		if(!TransStringUtil.isEmpty(model.getIsBullpen()) && model.getIsBullpen().equals("true") && !TransStringUtil.isEmpty(model.getZoneCode())){
			model.setZoneCode("");
			model.setZoneName("");
			model.setZoneType("");
		}
		Zone zone=null;
		
		if(!TransStringUtil.isEmpty(model.getZoneCode())) {
			zone=domainManagerService.getZone(model.getZoneCode());
		}
	
		DispatchPlanUtil.reconstructWebPlanInfo(model,zone,employeeManagerService);
		
	}
	
	protected String getIdFromRequest(HttpServletRequest request){
		String id = request.getParameter("id");
		if(TransStringUtil.isEmpty(id)) {
			id=request.getParameter("dispatchId");
		}
		return id;
	}

	
	public DomainManagerI getDomainManagerService() {
		return domainManagerService;
	}

	public void setDomainManagerService(DomainManagerI domainManagerService) {
		this.domainManagerService = domainManagerService;
	}	
	
	public EmployeeManagerI getEmployeeManagerService() {
		return employeeManagerService;
	}

	public void setEmployeeManagerService(EmployeeManagerI employeeManagerService) {
		this.employeeManagerService = employeeManagerService;
	}

}
