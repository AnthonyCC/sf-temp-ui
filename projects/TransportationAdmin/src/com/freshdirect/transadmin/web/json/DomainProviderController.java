package com.freshdirect.transadmin.web.json;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.transadmin.model.EmployeeInfo;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.service.EmployeeManagerI;
import com.freshdirect.transadmin.util.TransAdminCacheManager;
import com.freshdirect.transadmin.web.model.ScheduleCheckResult;

public class DomainProviderController extends BaseJsonRpcController  implements IDomainProvider {
		
	private DomainManagerI domainManagerService;
	
	private EmployeeManagerI employeeManagerService;
	
			
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
	
	public boolean copySchedule(String ids, String sourceWeekOf, String destinationWeekOf) {
		String[] employeeIds = StringUtil.decodeStrings(ids);
		getDomainManagerService().copyScheduleGroup(employeeIds, getFromClientDate(sourceWeekOf), getFromClientDate(destinationWeekOf));
		return false;
	}
	
	public Map<String, List<ScheduleCheckResult>> checkSchedule(String ids, String sourceWeekOf,String destinationWeekOf) {
		
		String[] employeeIds = StringUtil.decodeStrings(ids);
		Map<String, List<ScheduleCheckResult>> result = new HashMap<String, List<ScheduleCheckResult>>();
		List<ScheduleCheckResult> sourceMessages = new ArrayList<ScheduleCheckResult>();
		List<ScheduleCheckResult> destinationMessages = new ArrayList<ScheduleCheckResult>();
		
		Date sSourceWeekOf = getFromClientDate(sourceWeekOf);
		Date dSourceWeekOf = getFromClientDate(destinationWeekOf);
		
		if(employeeIds != null) {
			for(String empId : employeeIds) {
				EmployeeInfo info = TransAdminCacheManager.getInstance().getActiveInactiveEmployeeInfo(empId, getEmployeeManagerService());
				Collection sourceEmpSchedule = getDomainManagerService().getScheduleEmployee(empId, getParsedDate(sSourceWeekOf));
				
				if(info != null) {
					if(sourceEmpSchedule != null && sourceEmpSchedule.size() > 0) {
							sourceMessages.add(new ScheduleCheckResult(info.getFirstName()
																			, info.getLastName()
																			, info.getEmployeeId()
																			, "X"));
					} else {
						sourceMessages.add(new ScheduleCheckResult(info.getFirstName()
								, info.getLastName()
								, info.getEmployeeId()
								, null));
					}
					Collection destinationEmpSchedule = getDomainManagerService().getScheduleEmployee(empId, getParsedDate(dSourceWeekOf));
					
					if(destinationEmpSchedule != null && destinationEmpSchedule.size() > 0) {
						destinationMessages.add(new ScheduleCheckResult(info.getFirstName()
																		, info.getLastName()
																		, info.getEmployeeId()
																		, "X"));
					} else {
						destinationMessages.add(new ScheduleCheckResult(info.getFirstName()
								, info.getLastName()
								, info.getEmployeeId()
								, null));
					}
				}
			}
		}
		result.put("S", sourceMessages);
		result.put("D", destinationMessages);
		return result;
	}
	
	public Map<EmployeeInfo, Set<EmployeeInfo>> getTeamMapping(String ids) {
		
		Map<EmployeeInfo, Set<EmployeeInfo>> result = new HashMap<EmployeeInfo, Set<EmployeeInfo>>();
		Map<EmployeeInfo, Set<EmployeeInfo>> teams = getEmployeeManagerService().getTeams();
		
		String[] employeeIds = StringUtil.decodeStrings(ids);
		
		if(teams != null && employeeIds != null && employeeIds.length > 0) {			
			for(String empId : employeeIds) {				
				EmployeeInfo _member = getEmployeeManagerService().getEmployeeEx(empId).getEmpInfo();
				if(_member != null) {
					if(teams.containsKey(_member) && !result.containsKey(_member)) {
						result.put(_member, teams.get(_member));
					} else {
						for (Map.Entry<EmployeeInfo, Set<EmployeeInfo>> entry : teams.entrySet()) {
							if(entry.getValue().contains(_member)
									&& !result.containsKey(entry.getKey())) {
								result.put(entry.getKey(), entry.getValue());
							}
						}
					}
				}
			}
		}		
		return result;
	}
	
}
