package com.freshdirect.transadmin.web;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.routing.constants.EnumWaveInstancePublishSrc;
import com.freshdirect.routing.util.RoutingServicesProperties;
import com.freshdirect.transadmin.model.EmployeeInfo;
import com.freshdirect.transadmin.model.MaintenanceIssue;
import com.freshdirect.transadmin.model.Plan;
import com.freshdirect.transadmin.model.Zone;
import com.freshdirect.transadmin.service.DispatchManagerI;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.service.EmployeeManagerI;
import com.freshdirect.transadmin.service.ZoneManagerI;
import com.freshdirect.transadmin.util.DispatchPlanUtil;
import com.freshdirect.transadmin.util.EnumCachedDataType;
import com.freshdirect.transadmin.util.EnumResourceType;
import com.freshdirect.transadmin.util.MaintenanceLogUtil;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.util.TransportationAdminProperties;
import com.freshdirect.transadmin.util.WaveUtil;
import com.freshdirect.transadmin.model.VIRRecord;

public class VIRRecordLogFormController extends AbstractFormController {

	private DomainManagerI domainManagerService;

	private DispatchManagerI dispatchManagerService;

	private EmployeeManagerI employeeManagerService;
	
	
	public DomainManagerI getDomainManagerService() {
		return domainManagerService;
	}

	public void setDomainManagerService(DomainManagerI domainManagerService) {
		this.domainManagerService = domainManagerService;
	}
	
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
		
		String isRefreshReqd = request.getParameter("refresh");
		if("true".equalsIgnoreCase(isRefreshReqd)){
			domainManagerService.refreshCachedData(EnumCachedDataType.TRUCK_DATA);
		}
		refData.put("trucks",  domainManagerService.getTrucks());
		
		List drivers = DispatchPlanUtil.getSortedResources(employeeManagerService.getEmployeesByRole(EnumResourceType.DRIVER.getName()));
		drivers.addAll(DispatchPlanUtil.getSortedResources(employeeManagerService.getEmployeesByRole(EnumResourceType.MANAGER.getName())));
		
		refData.put("drivers",drivers);
		
		//get the predefined vendors
		String preVendors= TransportationAdminProperties.getTransportationVendors();
		String[] vendorArray = StringUtil.decodeStrings(preVendors);
		List vendors = new ArrayList();
		for(String vendor: vendorArray){
			vendors.add(vendor);
		}
		refData.put("vendors", vendors);
		
		refData.put("issueTypes", getDomainManagerService().getIssueTypes());
		
		//get the predefined vendors
		String preTruckLocations= TransportationAdminProperties.getTruckDamageLocations();
		String[] damageLocationsArray = StringUtil.decodeStrings(preTruckLocations);
		List damageLocations = new ArrayList();
		for(String _loc: damageLocationsArray){
			damageLocations.add(_loc);
		}
		refData.put("damageLocations", damageLocations);
		
		List issueSides = new ArrayList();
		issueSides.add("Driver");issueSides.add("Passenger");
		refData.put("issueSides", issueSides);
		String currentDate = "";
		try {
			currentDate = TransStringUtil.getDate(new Date());
		} catch (ParseException e) {			
			e.printStackTrace();
		}
		refData.put("currentDate", currentDate);
		refData.put("userId", getUserId(request));
		return refData;
	}
	
	public Object getBackingObject(String id) {
		return getDomainManagerService().getVIRRecord(id);		
	}

	public Object getDefaultBackingObject() {		
		return new VIRRecord();
	}

	public boolean isNew(Object command) {
		VIRRecord modelIn = (VIRRecord)command;
		return (TransStringUtil.isEmpty(modelIn.getId()));
	}

	public String getDomainObjectName() {
		return "VIR Record";
	}

	public List saveDomainObject(HttpServletRequest request, Object command) {

		VIRRecord model = (VIRRecord)command;
		List errorList = new ArrayList();
		errorList = MaintenanceLogUtil.processVIRRecord(errorList, model, domainManagerService);
				
		return errorList;
	}
	
	public void saveErrorMessage(HttpServletRequest request, Object msg) {
		List messages = (List)msg;
		if (messages != null) {
			messages.add(getMessage("app.actionmessage.109", new Object[]{}));
		}
		super.saveErrorMessage(request, msg);
	}	

	protected void onBind(HttpServletRequest request, Object command) {

		VIRRecord model = (VIRRecord) command;
		//set userId to command object
		model.setCreatedBy(getUserId(request));
		model.setUserId(getUserId(request));
		model.setCreateDate(new Timestamp(System.currentTimeMillis()));
	}	
	
	protected String getIdFromRequest(HttpServletRequest request){
		return request.getParameter("id");		 
	}	

}

