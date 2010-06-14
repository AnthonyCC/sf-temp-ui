package com.freshdirect.transadmin.service;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import com.freshdirect.transadmin.model.EmployeeInfo;
import com.freshdirect.transadmin.model.EmployeeRoleType;
import com.freshdirect.transadmin.model.EmployeeSubRoleType;
import com.freshdirect.transadmin.model.ScheduleEmployee;
import com.freshdirect.transadmin.web.model.WebEmployeeInfo;
import com.freshdirect.transadmin.web.model.WebSchedule;

public interface EmployeeManagerI extends BaseManagerI {
	
	Collection getEmployees();
	
	Collection getKronosEmployees();
	
	WebEmployeeInfo getEmployee(String id);
	
	WebEmployeeInfo getEmployeeEx(String id);
	
	Collection getEmployeeRoleTypes();
	
	Collection getEmployeeSubRoleTypes();
	
	EmployeeRoleType getEmployeeRoleType(String roleTypeId);
	
	EmployeeSubRoleType getEmployeeSubRoleType(String subRoleTypeId);
	
	void storeEmployees(WebEmployeeInfo employees);
	
	Collection getTerminatedEmployees();
	
	Collection getKronosTerminatedEmployees();
	
	Collection getEmployeesByRole(String roleTypeId);
	
	Collection getSupervisors();
	
	Collection getPunchInfo(String date);
	Collection getScheduleEmployees(Date weekOf);	
	WebSchedule getSchedule(String id, Date weekOf);
	ScheduleEmployee getSchedule(String id, String weekOf, String day);
	
	Collection getScheduledEmployees(String day, String date);
	Collection getUnAvailableEmployees(Collection plans,String date);
	
	
	Collection getKronosActiveInactiveEmployees();
	void syncEmployess();
	Collection getEmployeeRole(String empId);
	Collection getTransAppActiveEmployees();
	
	Map<EmployeeInfo, Set<EmployeeInfo>> getTeams();
	Map<String, String> getTeamMapping();
	
}
