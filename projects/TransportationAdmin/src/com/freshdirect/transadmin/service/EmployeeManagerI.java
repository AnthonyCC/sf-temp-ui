package com.freshdirect.transadmin.service;

import java.util.Collection;

import com.freshdirect.transadmin.model.EmployeeRoleType;
import com.freshdirect.transadmin.model.ScheduleEmployee;
import com.freshdirect.transadmin.web.model.WebEmployeeInfo;
import com.freshdirect.transadmin.web.model.WebSchedule;

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
	public Collection getScheduleEmployees();
	
	public WebSchedule getSchedule(String id);
	public Collection getScheduledEmployees(String day,String date);
	public Collection getUnAvailableEmployees(Collection plans,String date);
	public ScheduleEmployee getSchedule(String id,String day);
}
