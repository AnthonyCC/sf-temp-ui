package com.freshdirect.transadmin.web;

import java.sql.Timestamp;
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

import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.transadmin.constants.EnumIssueStatus;
import com.freshdirect.transadmin.constants.EnumServiceStatus;
import com.freshdirect.transadmin.model.MaintenanceIssue;
import com.freshdirect.transadmin.service.AssetManagerI;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.service.EmployeeManagerI;
import com.freshdirect.transadmin.util.DispatchPlanUtil;
import com.freshdirect.transadmin.util.EnumCachedDataType;
import com.freshdirect.transadmin.util.EnumResourceType;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.util.TransportationAdminProperties;

public class MaintenanceLogFormController extends AbstractFormController {

	private DomainManagerI domainManagerService;
	
	private EmployeeManagerI employeeManagerService;
	
	private AssetManagerI assetManagerService;
	
	public AssetManagerI getAssetManagerService() {
		return assetManagerService;
	}

	public void setAssetManagerService(AssetManagerI assetManagerService) {
		this.assetManagerService = assetManagerService;
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

	protected Map referenceData(HttpServletRequest request) throws ServletException {
		
		Map refData = new HashMap();
		
		String isRefreshReqd = request.getParameter("refresh");
		if("true".equalsIgnoreCase(isRefreshReqd)){
			domainManagerService.refreshCachedData(EnumCachedDataType.TRUCK_DATA);
		}
		refData.put("truckAssets",  getAssetManagerService().getAssets("TRUCK"));
		
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
				
		refData.put("serviceStatuses", EnumServiceStatus.getEnumList());
		
		return refData;
	}
	
	public Object getBackingObject(String id) {
		MaintenanceIssue model = getDomainManagerService().getMaintenanceIssue(id);		
		if(model !=null && model.getVerificationDate()== null && EnumIssueStatus.OPEN.getName().equalsIgnoreCase(model.getIssueStatus())){
			try {
				model.setVerificationDate(TransStringUtil.getServerDateString(TransStringUtil.getCurrentServerDate()));
			} catch (ParseException e) {				
				e.printStackTrace();
			}		
		}
		
		if(model.getId()!=null && !"".equals(model.getId())){
			model.setIssueId(model.getId());
		}
		return model;
	}

	public Object getDefaultBackingObject() {		
		MaintenanceIssue model = new MaintenanceIssue();
		if(model !=null && model.getCreateDate()== null)
			model.setCreateDate(new Timestamp(System.currentTimeMillis()));		
		
		return model;
	}

	public boolean isNew(Object command) {
		MaintenanceIssue modelIn = (MaintenanceIssue)command;
		return (TransStringUtil.isEmpty(modelIn.getId()));
	}

	public String getDomainObjectName() {
		return "Maintenance Record";
	}
	
	public List saveDomainObject(HttpServletRequest request, Object command) {

		MaintenanceIssue _command = (MaintenanceIssue)command;
		List errorList = new ArrayList();
		
		try{
				Collection maintenanceIssues = domainManagerService
					.getMaintenanceIssue(_command.getTruckNumber(), _command.getIssueType(), _command.getIssueSubType());

				if(_command.getRepairedBy() == null && "".equals(request.getParameter("reverify"))
						&& (EnumIssueStatus.VERIFIED.getName().equalsIgnoreCase(_command.getIssueStatus()) 
								|| EnumIssueStatus.REVERIFIED.getName().equalsIgnoreCase(_command.getIssueStatus()))){
					_command.setRepairedBy(getUserId(request));
					_command.setIssueStatus(EnumIssueStatus.RESOLVED.getName());
					_command.setServiceStatus(EnumServiceStatus.INSERVICE.getDescription());
				}
				if(_command.getVerifiedBy() == null 
						&& EnumIssueStatus.OPEN.getName().equalsIgnoreCase(_command.getIssueStatus())){
					_command.setVerifiedBy(getUserId(request));
					_command.setIssueStatus(EnumIssueStatus.VERIFIED.getName());
				}
				if(!"".equals(request.getParameter("reverify")) && EnumIssueStatus.VERIFIED.getName().equalsIgnoreCase(_command.getIssueStatus()))
					_command.setIssueStatus(EnumIssueStatus.REVERIFIED.getName());
				
				_command.setModifiedDate(new Timestamp(System.currentTimeMillis()));		
				
				if(_command.getCreatedBy() == null)
					_command.setCreatedBy(getUserId(request));	
				
				//Default attributes set to maintenance issue
				if(_command.getServiceStatus().equalsIgnoreCase(""))
					_command.setServiceStatus(EnumServiceStatus.INSERVICE.getDescription());
				if(_command.getIssueStatus()== null)
					_command.setIssueStatus(EnumIssueStatus.OPEN.getName());
				
				getDomainManagerService().saveMaintenanceIssue(_command);
				_command.setIssueId(_command.getId());
				//request.setAttribute("id", _command.getId());
		}catch(Exception e){
			e.printStackTrace();
			errorList.add(this.getMessage("app.error.135", new Object[]{"Maintenance Issue"}));
		}		
		
		return errorList;
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		String messageKey = isNew(command) ? "app.actionmessage.101"
				: "app.actionmessage.102";

		preProcessDomainObject(command);
		List errorList = saveDomainObject(request, command);

		MaintenanceIssue _command = (MaintenanceIssue)command;
		if(_command.getVerificationDate()== null && EnumIssueStatus.OPEN.getName().equalsIgnoreCase(_command.getIssueStatus())){
			try {
				_command.setVerificationDate(TransStringUtil.getServerDateString(TransStringUtil.getCurrentServerDate()));
			} catch (ParseException e) {				
				e.printStackTrace();
			}		
		}
		
		ModelAndView mav = new ModelAndView(getSuccessView(), errors.getModel());
		mav.getModel().put(this.getCommandName(), _command);
		mav.getModel().putAll(referenceData(request));
		if(errorList == null || errorList.isEmpty()) {
			saveMessage(request, getMessage(messageKey,
					new Object[] { getDomainObjectName() }));
		} else {
			saveErrorMessage(request, errorList);
		}
		return mav;
	}
	
	protected String getIdFromRequest(HttpServletRequest request){
		String id = request.getParameter("id");
		if(TransStringUtil.isEmpty(id)) {
			id = request.getParameter("issueId");
		}
		return id;
	}

}

