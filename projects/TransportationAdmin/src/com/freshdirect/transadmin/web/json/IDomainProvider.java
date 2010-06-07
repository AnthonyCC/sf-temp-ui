package com.freshdirect.transadmin.web.json;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.transadmin.model.EmployeeInfo;
import com.freshdirect.transadmin.web.model.ScheduleCheckResult;

public interface IDomainProvider {
	
	boolean copySchedule(String employeeIds, String sourceWeekOf, String destinationWeekOf);
	
	Map<String, List<ScheduleCheckResult>> checkSchedule(String employeeIds, String sourceWeekOf, String destinationWeekOf);
	
	Map<EmployeeInfo, Set<EmployeeInfo>> getTeamMapping(String ids);
}
