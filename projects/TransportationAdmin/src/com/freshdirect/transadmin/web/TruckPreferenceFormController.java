package com.freshdirect.transadmin.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.ServletRequestDataBinder;

import com.freshdirect.transadmin.model.EmployeeInfo;
import com.freshdirect.transadmin.model.EmployeeRole;
import com.freshdirect.transadmin.model.EmployeeSubRoleType;
import com.freshdirect.transadmin.model.EmployeeroleId;
import com.freshdirect.transadmin.service.AssetManagerI;
import com.freshdirect.transadmin.service.EmployeeManagerI;
import com.freshdirect.transadmin.util.DispatchPlanUtil;
import com.freshdirect.transadmin.util.ModelUtil;
import com.freshdirect.transadmin.web.model.WebEmployeeInfo;

public class TruckPreferenceFormController extends AbstractFormController {

	private EmployeeManagerI employeeManagerService;
	private AssetManagerI assetManagerService;
	
	public EmployeeManagerI getEmployeeManagerService() {
		return employeeManagerService;
	}
	
	public void setEmployeeManagerService(EmployeeManagerI employeeManagerService) {
		this.employeeManagerService = employeeManagerService;
	}
	
	public AssetManagerI getAssetManagerService() {
		return assetManagerService;
	}

	public void setAssetManagerService(AssetManagerI assetManagerService) {
		this.assetManagerService = assetManagerService;
	}
	
	protected Map referenceData(HttpServletRequest request) throws ServletException {

		Map refData = new HashMap();
		refData.put("trucks",  getAssetManagerService().getAssets("TRUCK"));
		
		return refData;
	}

	public Object getBackingObject(String id) {
	
		WebEmployeeInfo info = getEmployeeManagerService().getEmployee(id);	
		return info;
	}

	public Object getDefaultBackingObject() {
		return new WebEmployeeInfo(null,null,null);
	}

	public boolean isNew(Object command) {
		WebEmployeeInfo modelIn = (WebEmployeeInfo)command;
		return (modelIn.getEmpInfo() == null);
	}

	public String getDomainObjectName() {
		return "EmployeeInfo";
	}

	protected void onBind(HttpServletRequest request, Object command) {

		WebEmployeeInfo model = (WebEmployeeInfo) command;
	}

	public List saveDomainObject(HttpServletRequest request, Object domainObject) {

		List errorList = null;
		try {
			getEmployeeManagerService().storeEmployeeTruckPreference((WebEmployeeInfo)domainObject);
		} catch (DataIntegrityViolationException objExp) {
			errorList = new ArrayList();			
			errorList.add(objExp.getMessage());
		}
		return errorList;
	}
}
