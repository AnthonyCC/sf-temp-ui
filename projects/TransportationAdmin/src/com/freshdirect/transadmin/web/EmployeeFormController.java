package com.freshdirect.transadmin.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.ServletRequestDataBinder;

import com.freshdirect.transadmin.model.EmployeeRole;
import com.freshdirect.transadmin.model.EmployeeRoleType;
import com.freshdirect.transadmin.model.EmployeeroleId;
import com.freshdirect.transadmin.service.EmployeeManagerI;
import com.freshdirect.transadmin.web.model.WebEmployeeInfo;

public class EmployeeFormController extends AbstractFormController {

	private EmployeeManagerI employeeManagerService;
	
	protected Map referenceData(HttpServletRequest request) throws ServletException {
		System.out.println("referenceData");
		Map refData = new HashMap();		
		//refData.put("supervisors", getDomainManagerService().getSupervisors());
		//refData.put("region", getDomainManagerService().getR);
		refData.put("roleTypes", getEmployeeManagerService().getEmployeeRoleTypes());	
		return refData;
	}
	
	public Object getBackingObject(String id) {
		System.out.println("getBackingObject:"+id);
		return getEmployeeManagerService().getEmployee(id);
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
	

	
	protected void onBind(HttpServletRequest request, Object command) {
		
		System.out.println("On Bind");
		WebEmployeeInfo model = (WebEmployeeInfo) command;
		
		String roleTypeCodes[]=request.getParameterValues("employeeRoleTypes");
		String empId=request.getParameter("employeeId");
		
		System.out.println("roleTypeCodes :"+roleTypeCodes);
		
		List roleList=null;
		
		if(roleTypeCodes!=null)
		{			
			roleList=new ArrayList();
			
			for(int i=0;i<roleTypeCodes.length;i++){
				
				EmployeeRoleType roleType= getEmployeeManagerService().getEmployeeRoleType(roleTypeCodes[i]);
				if(roleType==null) continue;												
				EmployeeroleId roleId=new EmployeeroleId(empId,roleType.getCode());
				EmployeeRole empRole=new EmployeeRole(roleId,roleType);
				roleList.add(empRole);		
				System.out.println("roleType"+roleType);						
			}			
		}
		
		if(roleList!=null && roleList.size()>0)
			   model.setEmpRole(roleList);
			else
				model.setEmpRole(null);
	
	}
	
	protected void preProcessDomainObject(Object domainObject) {
		System.out.println("preProcessDomainObject");
//		Zone modelIn = (Zone)domainObject;
//		if(TransStringUtil.isEmpty(modelIn.getZoneCode()) ) {
//			modelIn.setZoneCode(modelIn.getZoneCode());
//		}
	}
	
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		super.initBinder(request,binder);
		//binder.registerCustomEditor(TrnArea.class, new TrnAreaPropertyEditor());
		//binder.registerCustomEditor(TrnZoneType.class, new TrnZoneTypePropertyEditor());
		//binder.registerCustomEditor(Region.class, new RegionPropertyEditor());
    }

public List saveDomainObject(Object domainObject) {
		
		System.out.println("trying to save the domain object"+domainObject);
		
		List errorList = null;
		try {
			getEmployeeManagerService().storeEmployees((WebEmployeeInfo)domainObject);
		} catch (DataIntegrityViolationException objExp) {
			errorList = new ArrayList();
			errorList.add(this.getMessage("app.actionmessage.119", new Object[]{this.getDomainObjectName()}));
		}
		return errorList;
	}

public EmployeeManagerI getEmployeeManagerService() {
	return employeeManagerService;
}

public void setEmployeeManagerService(EmployeeManagerI employeeManagerService) {
	this.employeeManagerService = employeeManagerService;
}

	
	
	

}
