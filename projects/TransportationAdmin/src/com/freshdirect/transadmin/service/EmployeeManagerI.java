package com.freshdirect.transadmin.service;

import java.util.Collection;

import com.freshdirect.transadmin.model.EmployeeRoleType;
import com.freshdirect.transadmin.web.model.WebEmployeeInfo;

public interface EmployeeManagerI extends BaseManagerI {
	
	Collection getEmployees();
	
	Collection getKronosEmployees();
	
	WebEmployeeInfo getEmployee(String id);
	
	Collection getEmployeeRoleTypes();
	
	EmployeeRoleType getEmployeeRoleType(String roleTypeId);
	
	void storeEmployees(WebEmployeeInfo employees);
	
	Collection getTerminatedEmployees();
	
	Collection getKronosTerminatedEmployees();
	
	Collection getEmployeesByRole(String roleTypeId);
	
	Collection getSupervisors();
	
	Collection getPunchInfo(String date);
}
