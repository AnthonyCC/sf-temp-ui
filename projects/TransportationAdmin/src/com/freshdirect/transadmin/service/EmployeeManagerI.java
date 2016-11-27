package com.freshdirect.transadmin.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.transadmin.model.EmployeeInfo;
import com.freshdirect.transadmin.model.EmployeeRoleType;
import com.freshdirect.transadmin.model.EmployeeSubRoleType;
import com.freshdirect.transadmin.model.ScheduleEmployee;
import com.freshdirect.transadmin.web.model.WebEmployeeInfo;
import com.freshdirect.transadmin.web.model.WebSchedule;

public interface EmployeeManagerI extends BaseManagerI {
	
	void syncEmployees() throws Exception;
	
	Collection getEmployees();
	
	Collection getKronosEmployees();
	
	WebEmployeeInfo getEmployee(String id);
	
	void storeEmployeeTruckPreference(WebEmployeeInfo empInfo);
	
	WebEmployeeInfo getEmployeeEx(String id);
	
	Collection getEmployeeRoleTypes();
	
	Collection getEmployeeSubRoleTypes();
	
	EmployeeRoleType getEmployeeRoleType(String roleTypeId);
	
	EmployeeSubRoleType getEmployeeSubRoleType(String subRoleTypeId);
	
	void storeEmployees(WebEmployeeInfo employees);
	
	Collection getTerminatedEmployees();
	List getTerminatedEmployeesEx();
	
	Map<String, EmployeeInfo> getKronosTerminatedEmployees();
	
	Collection getEmployeesByRole(String roleTypeId, String dispatchType, Date dispatchDate);
	
	Collection getSupervisors();
	
	Collection getPunchInfo(String date) throws Exception;
	Collection getScheduleEmployees(Date weekOf);
	Collection getScheduleEmployees(Date weekOf, String day);
	WebSchedule getSchedule(String id, Date weekOf);
	ScheduleEmployee getSchedule(String id, String weekOf, String day);
	
	Collection getScheduledEmployees(String day, String date);
	Collection getUnAvailableEmployees(Collection plans,String date);
	
	
	Collection getKronosActiveInactiveEmployees();
	void syncEmployess();
	Collection getEmployeeRole(String empId);
	Map getTransAppActiveEmployees();
	
	Map<EmployeeInfo, Set<EmployeeInfo>> getTeams();
	Map<String, String> getTeamMapping();
	
	Collection getEmployeesTruckPrefrence();

	Collection getEmployeesByRoleAndSubRole(String roleTypeId, String subRoleTypeId);
	
	Map getEmployeeRoles(Set empIds);
	
	Map getEmployeeStatus(Set empIds);
	
	Map getEmployeeTruckPref(Set empIds);
	
	Map getTeamByEmployees(Set empIds);
	
}
