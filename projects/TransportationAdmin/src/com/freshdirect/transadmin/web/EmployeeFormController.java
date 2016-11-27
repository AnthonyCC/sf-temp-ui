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

import com.freshdirect.delivery.EnumDayShift;
import com.freshdirect.transadmin.model.EmployeeInfo;
import com.freshdirect.transadmin.model.EmployeeRole;
import com.freshdirect.transadmin.model.EmployeeSubRoleType;
import com.freshdirect.transadmin.model.EmployeeSupervisor;
import com.freshdirect.transadmin.model.EmployeeSupervisorId;
import com.freshdirect.transadmin.model.EmployeeroleId;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.service.EmployeeManagerI;
import com.freshdirect.transadmin.util.DispatchPlanUtil;
import com.freshdirect.transadmin.util.ModelUtil;
import com.freshdirect.transadmin.web.model.WebEmployeeInfo;

public class EmployeeFormController extends AbstractFormController {

	private EmployeeManagerI employeeManagerService;
	
	private DomainManagerI domainManagerService;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected Map referenceData(HttpServletRequest request) throws ServletException {

		Map refData = new HashMap();
		Collection<WebEmployeeInfo> activeEmployees = employeeManagerService.getEmployees();
		
		refData.put("employees", DispatchPlanUtil.getSortedResources(ModelUtil.getEmployees(activeEmployees)));
		refData.put("roleTypes", getEmployeeManagerService().getEmployeeSubRoleTypes());
		refData.put("supervisors", DispatchPlanUtil.getSortedResources(employeeManagerService.getSupervisors()));
		refData.put("regions", getDomainManagerService().getRegions());
		refData.put("shifts", EnumDayShift.getEnumList());
		return refData;
	}

	public Object getBackingObject(String id) {
		
		Map<String, String> employeeTeamMapping = employeeManagerService.getTeamMapping();
		WebEmployeeInfo info = getEmployeeManagerService().getEmployee(id);
		if(employeeTeamMapping != null  
				&& info != null 
					&& employeeTeamMapping.containsValue(info.getEmployeeId())) {
			info.setLead(true);
			info.setLeadInfo(info.getEmpInfo());
		}
		return info;
	}

	public Object getDefaultBackingObject() {
		return new WebEmployeeInfo(null,null);
	}

	public boolean isNew(Object command) {
		WebEmployeeInfo modelIn = (WebEmployeeInfo)command;
		return (modelIn.getEmpInfo() == null);
	}

	public String getDomainObjectName() {
		return "WebEmployeeInfo";
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void onBind(HttpServletRequest request, Object command) {

		WebEmployeeInfo model = (WebEmployeeInfo) command;

		String roleTypeCodes[] = request.getParameterValues("employeeRoleTypes");
		String empId = request.getParameter("employeeId");
		String toggle = request.getParameter("toggle");
		String leadId = request.getParameter("leadId");		

		if (toggle != null) {
			model.toggleStatus();
		}
		List roleList = null;

		if (roleTypeCodes != null) {
			roleList = new ArrayList();

			for (int i = 0; i < roleTypeCodes.length; i++) {

				EmployeeSubRoleType roleType = getEmployeeManagerService().getEmployeeSubRoleType(roleTypeCodes[i]);
				if (roleType == null)
					continue;
				EmployeeroleId roleId = new EmployeeroleId(empId, roleType.getRole().getCode());
				EmployeeRole empRole = new EmployeeRole(roleId, roleType);
				roleList.add(empRole);
			}
		}

		if (roleList != null && roleList.size() > 0) {
			model.setEmpRole(roleList);
		} else {
			model.setEmpRole(null);
		}
		if (leadId != null && leadId.trim().length() > 0) {
			EmployeeInfo leadInfo = new EmployeeInfo();
			leadInfo.setEmployeeId(leadId);
			model.setLeadInfo(leadInfo);
		}
		if(model.getEmpSupervisor() == null){
			model.setEmpSupervisor(new EmployeeSupervisor());
		}
		if(model.getEmpSupervisor().getId() == null){
			model.getEmpSupervisor().setId(new EmployeeSupervisorId());
		}
		model.getEmpSupervisor().getId().setSupervisorId(model.getHomeSupervisorId());
		model.getEmpSupervisor().getId().setKronosId(model.getEmployeeId());

	}

	protected void preProcessDomainObject(Object domainObject) {
		
	}

	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		super.initBinder(request,binder);
    }

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List saveDomainObject(HttpServletRequest request, Object domainObject) {

		List errorList = null;
		try {
			getEmployeeManagerService().storeEmployees((WebEmployeeInfo)domainObject);
		} catch (DataIntegrityViolationException objExp) {
			errorList = new ArrayList();
			errorList.add(objExp.getMessage());
		}
		return errorList;
	}

	public EmployeeManagerI getEmployeeManagerService() {
		return employeeManagerService;
	}
	
	public void setEmployeeManagerService(EmployeeManagerI employeeManagerService) {
		this.employeeManagerService = employeeManagerService;
	}

	public DomainManagerI getDomainManagerService() {
		return domainManagerService;
	}

	public void setDomainManagerService(DomainManagerI domainManagerService) {
		this.domainManagerService = domainManagerService;
	}
}
