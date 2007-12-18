package com.freshdirect.transadmin.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.freshdirect.transadmin.model.TrnEmployee;

public class EmployeeFormController extends AbstractDomainFormController {
		
	protected Map referenceData(HttpServletRequest request) throws ServletException {
		Map refData = new HashMap();		
		refData.put("supervisors", getDomainManagerService().getSupervisors());
		refData.put("jobtypes", getDomainManagerService().getEmployeeJobType());		
		return refData;
	}
	
	public Object getBackingObject(String id) {
		return getDomainManagerService().getEmployee(id);
	}
	
	public Object getDefaultBackingObject() {
		return new TrnEmployee();
	}
	
	public boolean isNew(Object command) {
		TrnEmployee modelIn = (TrnEmployee)command;
		return (modelIn.getEmployeeId() == null);
	}
	
	public String getDomainObjectName() {
		return "Employee";
	}

}
