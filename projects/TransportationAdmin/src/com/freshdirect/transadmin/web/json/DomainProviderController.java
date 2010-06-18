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
	
	public boolean copySchedule(String ids, String sourceWeekOf, String destinationWeekOf, String day) {
		String[] employeeIds = StringUtil.decodeStrings(ids);
		Date sSourceWeekOf = getWeekOf(getFromClientDate(sourceWeekOf));
		Date dSourceWeekOf = getWeekOf(getFromClientDate(destinationWeekOf));
		getDomainManagerService().copyScheduleGroup(employeeIds, sSourceWeekOf
																		, dSourceWeekOf
																		, day);
		return false;
	}
	
	public Map<String, List<ScheduleCheckResult>> checkSchedule(String ids, String sourceWeekOf,String destinationWeekOf, String day) {
		
		String[] employeeIds = StringUtil.decodeStrings(ids);
		Map<String, List<ScheduleCheckResult>> result = new HashMap<String, List<ScheduleCheckResult>>();
		List<ScheduleCheckResult> sourceMessages = new ArrayList<ScheduleCheckResult>();
		List<ScheduleCheckResult> destinationMessages = new ArrayList<ScheduleCheckResult>();
		
		Date sSourceWeekOf = getWeekOf(getFromClientDate(sourceWeekOf));
		Date dSourceWeekOf = getWeekOf(getFromClientDate(destinationWeekOf));
		
		if(employeeIds != null) {
			for(String empId : employeeIds) {
				EmployeeInfo info = TransAdminCacheManager.getInstance().getActiveInactiveEmployeeInfo(empId, getEmployeeManagerService());
				Collection sourceEmpSchedule = getScheduleEmployee(empId, sSourceWeekOf, day);
				
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
					Collection destinationEmpSchedule = getScheduleEmployee(empId, dSourceWeekOf, day);
					
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
	
	private Collection getScheduleEmployee(String empId, Date weekOf, String day) {
		if(day != null && !day.equalsIgnoreCase("ALL")) {
			return getDomainManagerService().getScheduleEmployee(empId, getParsedDate(weekOf), day);
		} else {
			return getDomainManagerService().getScheduleEmployee(empId, getParsedDate(weekOf));
		}
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
