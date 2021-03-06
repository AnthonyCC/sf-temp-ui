package com.freshdirect.transadmin.web.json;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.transadmin.model.EmployeeInfo;
import com.freshdirect.transadmin.web.model.ScheduleCheckResult;

public interface IDomainProvider {
	
	boolean copySchedule(String employeeIds, String sourceWeekOf, String destinationWeekOf, String day);
	
	Map<String, List<ScheduleCheckResult>> checkSchedule(String employeeIds, String sourceWeekOf, String destinationWeekOf, String day);
	
	Map<EmployeeInfo, Set<EmployeeInfo>> getTeamMapping(String ids);
	
	int addNewZipCodeCoverage(String zipCode, String homeCoverage, String cosCoverage, String envName,String ebtAccepted);
	
	boolean updateZipCodeCoverage(String zipCode, String homeCoverage, String cosCoverage,String ebtAccepted);
}
