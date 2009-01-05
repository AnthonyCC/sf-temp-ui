package com.freshdirect.transadmin.web;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.propertyeditors.ClassEditor;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.transadmin.model.Dispatch;
import com.freshdirect.transadmin.model.DispatchResource;
import com.freshdirect.transadmin.model.DispatchResourceId;
import com.freshdirect.transadmin.model.EmployeeRoleType;
import com.freshdirect.transadmin.model.FDRouteMasterInfo;
import com.freshdirect.transadmin.model.PlanResource;
import com.freshdirect.transadmin.model.PlanResourceId;
import com.freshdirect.transadmin.model.Region;
import com.freshdirect.transadmin.model.TrnDispatch;
import com.freshdirect.transadmin.model.TrnDispatchId;
import com.freshdirect.transadmin.model.TrnZoneType;
import com.freshdirect.transadmin.model.Zone;
import com.freshdirect.transadmin.model.ZonetypeResource;
import com.freshdirect.transadmin.service.DispatchManagerI;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.service.EmployeeManagerI;
import com.freshdirect.transadmin.util.DispatchPlanUtil;
import com.freshdirect.transadmin.util.EnumResourceType;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.web.model.DispatchCommand;
import com.freshdirect.transadmin.web.model.ResourceReq;
import com.freshdirect.transadmin.web.model.WebEmployeeInfo;
import com.freshdirect.transadmin.web.model.WebPlanInfo;
import com.freshdirect.transadmin.web.model.ZoneTypeCommand;

public class DispatchFormController extends AbstractFormController {
	
	private DomainManagerI domainManagerService;
	
	private DispatchManagerI dispatchManagerService;
	
	private EmployeeManagerI employeeManagerService;
	
	private static final String SUPERVISOR="006";
	private static final String DRIVER="001";
	private static final String HELPER="002";
	private static final String RUNNER="003";
	
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
		refData.put("supervisors", DispatchPlanUtil.getSortedResources(employeeManagerService.getEmployeesByRole(EnumResourceType.SUPERVISOR.getName())));
		refData.put("drivers", DispatchPlanUtil.getSortedResources(employeeManagerService.getEmployeesByRole(EnumResourceType.DRIVER.getName())));
		refData.put("helpers", DispatchPlanUtil.getSortedResources(employeeManagerService.getEmployeesByRole(EnumResourceType.HELPER.getName())));
		refData.put("runners", DispatchPlanUtil.getSortedResources(employeeManagerService.getEmployeesByRole(EnumResourceType.RUNNER.getName())));

		refData.put("zones", domainManagerService.getZones());
		return refData;
	}
	
	private DispatchCommand getCommand(Dispatch dispatch) throws Exception{
		Zone zone=domainManagerService.getZone(dispatch.getZone().getZoneCode());
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
			if(!isNew) {
				getDomainManagerService().saveEntity(domainObject);
			} else {
				getDispatchManagerService().saveDispatch(domainObject);
			}
		} catch (DataIntegrityViolationException objExp) {
			objExp.printStackTrace();
			errorList = new ArrayList();
			errorList.add(this.getMessage("app.actionmessage.135", new Object[]{command.getRoute()}));
		} catch (Exception objExp) {
			objExp.printStackTrace();
			errorList = new ArrayList();
			errorList.add(this.getMessage("sys.error.1001", new Object[]{this.getDomainObjectName()}));
		}
		return errorList;
	}
	
	public List saveDomainObject(Object domainObject) {
		DispatchCommand command = (DispatchCommand) domainObject;
		return saveDispatch(command);
	}
	/*
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		String dispDate = request.getParameter("dispDate");
		Date requestedDate = TransStringUtil.getDate(dispDate);
		String routeNo = request.getParameter("routeNo");
		String zoneId = request.getParameter("zoneId");
		DispatchCommand dispatchCommand = (DispatchCommand) command;
		if(!TransStringUtil.isEmpty(zoneId)){
			
			Zone zone=domainManagerService.getZone(zoneId);
			DispatchPlanUtil.reconstructWebPlanInfo(dispatchCommand,zone,employeeManagerService);
		}
		else if(StringUtils.hasText(dispDate) && StringUtils.hasText(routeNo)){
			setTruckNumber(request, requestedDate, routeNo, dispatchCommand);

		} else {
				
			save(request, command);
		}
		ModelAndView mav = new ModelAndView(getSuccessView(), errors.getModel());
		mav.getModel().put(this.getCommandName(), command);
		mav.getModel().putAll(referenceData(request));

		return mav;
	}
	*/
	protected void onBind(HttpServletRequest request, Object command) {
		DispatchCommand model = (DispatchCommand) command;
		Zone zone=null;
		if(!TransStringUtil.isEmpty(model.getZoneCode())) {
			zone=domainManagerService.getZone(model.getZoneCode());
		}
		DispatchPlanUtil.reconstructWebPlanInfo(model,zone,employeeManagerService);
		
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
