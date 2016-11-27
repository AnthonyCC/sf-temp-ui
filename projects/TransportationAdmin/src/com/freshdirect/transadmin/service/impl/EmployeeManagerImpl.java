package com.freshdirect.transadmin.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.dao.DataIntegrityViolationException;

import com.freshdirect.transadmin.constants.EnumDispatchType;
import com.freshdirect.transadmin.dao.BaseManagerDaoI;
import com.freshdirect.transadmin.dao.DomainManagerDaoI;
import com.freshdirect.transadmin.dao.EmployeeManagerDaoI;
import com.freshdirect.transadmin.dao.PunchInfoDaoI;
import com.freshdirect.transadmin.model.DispatchResource;
import com.freshdirect.transadmin.model.EmployeeInfo;
import com.freshdirect.transadmin.model.EmployeeRole;
import com.freshdirect.transadmin.model.EmployeeRoleType;
import com.freshdirect.transadmin.model.EmployeeStatus;
import com.freshdirect.transadmin.model.EmployeeSubRoleType;
import com.freshdirect.transadmin.model.EmployeeSupervisor;
import com.freshdirect.transadmin.model.EmployeeTeam;
import com.freshdirect.transadmin.model.EmployeeTruckPreference;
import com.freshdirect.transadmin.model.EmployeeTruckPreferenceId;
import com.freshdirect.transadmin.model.KronosEmployeeInfo;
import com.freshdirect.transadmin.model.Plan;
import com.freshdirect.transadmin.model.PlanResource;
import com.freshdirect.transadmin.model.PunchInfo;
import com.freshdirect.transadmin.model.ScheduleEmployee;
import com.freshdirect.transadmin.model.ScheduleEmployeeInfo;
import com.freshdirect.transadmin.service.EmployeeManagerI;
import com.freshdirect.transadmin.util.DispatchPlanUtil;
import com.freshdirect.transadmin.util.EnumCachedDataType;
import com.freshdirect.transadmin.util.EnumResourceSubType;
import com.freshdirect.transadmin.util.EnumResourceType;
import com.freshdirect.transadmin.util.ModelUtil;
import com.freshdirect.transadmin.util.TransAdminCacheManager;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.util.TransportationAdminProperties;
import com.freshdirect.transadmin.util.scrib.ScheduleEmployeeDetails;
import com.freshdirect.transadmin.web.model.WebEmployeeInfo;
import com.freshdirect.transadmin.web.model.WebPlanResource;
import com.freshdirect.transadmin.web.model.WebSchedule;
import com.google.common.collect.Lists;

public class EmployeeManagerImpl extends BaseManagerImpl implements
		EmployeeManagerI {

	private EmployeeManagerDaoI employeeManagerDAO = null;
	private DomainManagerDaoI domainManagerDao = null;
	private PunchInfoDaoI punchInfoDAO = null;
	private PunchInfoDaoI punchInfoCloudDAO = null;
	private EmployeeManagerDaoI employeeManagerCloudDAO = null;
	
	public PunchInfoDaoI getPunchInfoDAO() {
		return punchInfoDAO;
	}

	public EmployeeManagerDaoI getEmployeeManagerDAO() {
		return employeeManagerDAO;
	}

	protected BaseManagerDaoI getBaseManageDao() {
		return getDomainManagerDao();
	}

	public Collection getEmployeesTruckPrefrence(){
		
		Collection employeeIDsByRole = domainManagerDao.getEmployeesByRoleType(EnumResourceType.DRIVER.getName());
		employeeIDsByRole.addAll(domainManagerDao.getEmployeesByRoleType(EnumResourceType.MANAGER.getName()));
		Collection employeeIDsByTruckPref = domainManagerDao.getEmployeesTruckPreference();
		
		Collection employees = new ArrayList(employeeIDsByRole.size());
			
		Map activeInactiveEmpList = this.getActiveInactiveEmployees();
		Iterator it = employeeIDsByRole.iterator();
		while (it.hasNext()) {
			EmployeeRole empRole = (EmployeeRole) it.next();
			EmployeeInfo info = getTransAppActiveEmployees(activeInactiveEmpList, empRole.getId().getKronosId());
			if (info != null)					
				employees.add(info);			
		}
		
		Collection finalEmployeeList = ModelUtil.getTrnAdminEmployeeList((List) employees, (List) employeeIDsByRole, (List)employeeIDsByTruckPref);
		Map<String, String> employeeTeamMapping = ModelUtil.getIdMappedTeam(domainManagerDao.getTeamInfo());
		
		for (Iterator iterator = finalEmployeeList.iterator(); iterator.hasNext();) {
			WebEmployeeInfo webInfo = (WebEmployeeInfo) iterator.next();
			Collection empStatus = this.domainManagerDao.getEmployeeStatus(webInfo.getEmployeeId());
			if (empStatus != null && empStatus.size() > 0) {
				webInfo.setTrnStatus(""
							+ ((EmployeeStatus) (empStatus.toArray()[0]))
									.isStatus());
			}
			webInfo.setLeadInfo(TransAdminCacheManager.getInstance().getActiveInactiveEmployeeInfo
												(employeeTeamMapping.get(webInfo.getEmployeeId()), this));
			if(employeeTeamMapping.containsValue(webInfo.getEmployeeId())) {
				webInfo.setLead(true);
			}
		}

		return finalEmployeeList;
		
	}
	@SuppressWarnings("rawtypes")
	public Collection getEmployees() {
		// first get the kornos data
		// then get the role for the kornos data
		// then get the empSupervisors for the kornos data
		// then construct the viewer model
		// return the viewer model
		Map kronoEmployees = TransAdminCacheManager.getInstance().getActiveInactiveEmployeeInfo(this);
		Collection employeeRolesList = domainManagerDao.getEmployeeRoles();
		Collection employeeSupervisorList = domainManagerDao.getEmployeeSupervisors();
		Collection finalList = ModelUtil.getTrnAdminEmployeeList(kronoEmployees, (List) employeeRolesList, (List) employeeSupervisorList);
		
		Map<String, String> employeeTeamMapping = ModelUtil.getIdMappedTeam(domainManagerDao.getTeamInfo());
		
		for (Iterator iterator = finalList.iterator(); iterator.hasNext();) {
			WebEmployeeInfo webInfo = (WebEmployeeInfo) iterator.next();
			Collection empStatus = this.domainManagerDao.getEmployeeStatus(webInfo.getEmployeeId());
			if (empStatus != null && empStatus.size() > 0) {
				webInfo.setTrnStatus(""
						+ ((EmployeeStatus) (empStatus.toArray()[0]))
								.isStatus());
			}
			webInfo.setLeadInfo(TransAdminCacheManager.getInstance().getActiveInactiveEmployeeInfo
												(employeeTeamMapping.get(webInfo.getEmployeeId()), this));
			if(employeeTeamMapping.containsValue(webInfo.getEmployeeId())) {
				webInfo.setLead(true);
			}
		}
		return finalList;
	}
	
		
	public Collection getTerminatedEmployees() {
		// first get the kornos data
		// then get the role for the kornos data
		// then construct the viewer model
		// return the viewer model
		Map<String, EmployeeInfo> kronoEmployees = TransAdminCacheManager.getInstance()
				.getAllTerminatedEmployeeInfo(this);
		Collection employeeRolesList = domainManagerDao.getEmployeeRoles();
		Collection employeeSupervisorsList = domainManagerDao.getEmployeeSupervisors();
		Collection finalList = ModelUtil.getTrnAdminEmployeeList(kronoEmployees, (List) employeeRolesList, (List) employeeSupervisorsList);
		
		Map<String, String> employeeTeamMapping = ModelUtil.getIdMappedTeam(domainManagerDao.getTeamInfo());
		
		for (Iterator iterator = finalList.iterator(); iterator.hasNext();) {
			WebEmployeeInfo webInfo = (WebEmployeeInfo) iterator.next();			
			webInfo.setLeadInfo(TransAdminCacheManager.getInstance().getActiveInactiveEmployeeInfo
												(employeeTeamMapping.get(webInfo.getEmployeeId()), this));
			if(employeeTeamMapping.containsValue(webInfo.getEmployeeId())) {
				webInfo.setLead(true);
			}
		}
		return finalList;
	}
	
	public List<String> getTerminatedEmployeesEx() {
		Map<String, EmployeeInfo> kronoEmployees = TransAdminCacheManager.getInstance()
				.getAllTerminatedEmployeeInfo(this);
		
		List<String> finalList=new ArrayList<String>();
		
		Iterator<EmployeeInfo> it = kronoEmployees.values().iterator();
		
		while(it.hasNext())
		{
			EmployeeInfo info=(EmployeeInfo)it.next();
			finalList.add(info.getEmployeeId());
		}
		return finalList;
	}
	

	public Map getTransAppActiveEmployees() {
		Map kronosEmployees = TransAdminCacheManager
				.getInstance().getActiveInactiveEmployeeInfo(this);
		
		Map empStatuses = getEmployeeStatus();
		for (Iterator it = kronosEmployees.values().iterator(); it.hasNext();) {
			EmployeeInfo eInfo = (EmployeeInfo) it.next();
			Collection empStatus = (Collection) empStatuses.get(eInfo.getEmployeeId());
			if (empStatus != null && empStatus.size() > 0) {
				EmployeeStatus status = (EmployeeStatus) (empStatus.toArray()[0]);
				if (!status.isStatus()) {
					it.remove();
				}
			} else {
				if ("Inactive".equalsIgnoreCase(eInfo.getStatus())) {
					it.remove();
				}
			}
		}
		return kronosEmployees;
	}

	public EmployeeInfo getTransAppActiveEmployees(Map activeEmployees,
			String empId) {
		
		if(activeEmployees.containsKey(empId))
			return (EmployeeInfo)activeEmployees.get(empId);
		return null;
	}

	public Map getActiveInactiveEmployees() {
		// first get the kornos data
		// then get the role for the kornos data
		// then construct the viewer model
		// return the viewer model
		Map kronoEmployees = TransAdminCacheManager.getInstance().getActiveInactiveEmployeeInfo(this);
		return kronoEmployees;
	}

	public void setEmployeeManagerDAO(EmployeeManagerDaoI employeeManagerDao) {
		this.employeeManagerDAO = employeeManagerDao;
	}

	public Collection getKronosEmployees() {
		if(TransportationAdminProperties.isKronosCloudEnabled())
			return employeeManagerCloudDAO.getEmployees();
		else
			return employeeManagerDAO.getEmployees();
	}

	
	public Collection getSupervisors() {
		Map kronosEmployees = TransAdminCacheManager
				.getInstance().getActiveInactiveEmployeeInfo(this);
		List<EmployeeInfo> supervisors = new ArrayList<EmployeeInfo>();
		for (Iterator it = kronosEmployees.values().iterator(); it.hasNext();) {
			EmployeeInfo e = (EmployeeInfo) it.next();
			if ("Active".equals(e.getStatus()) && ("MANAGER".equals(e.getJobType()) || "SUPERVISOR".equals(e.getJobType()))) {
				supervisors.add(e);
			}
		}
		return supervisors;
	}
	
	public Collection getSupervisorsEx() {
		if(TransportationAdminProperties.isKronosCloudEnabled())
			return employeeManagerCloudDAO.getSupervisors();
		else
			return employeeManagerDAO.getSupervisors();
	}

	public DomainManagerDaoI getDomainManagerDao() {
		return domainManagerDao;
	}

	public void setDomainManagerDao(DomainManagerDaoI domainManagerDao) {
		this.domainManagerDao = domainManagerDao;
	}

	public WebEmployeeInfo getEmployee(String id) {
		// get the empInfo from the cache
		// get the role from the db
		// get the Truck Pref from the db
		// wrap it and return the data simple

		EmployeeInfo info = TransAdminCacheManager.getInstance().getActiveInactiveEmployeeInfo(id, this);
		Collection empRoles = this.domainManagerDao.getEmployeeRole(id);
		Collection empSupervisor = this.domainManagerDao.getEmployeeSupervisor(id);
		Collection empStatus = this.domainManagerDao.getEmployeeStatus(id);
		Collection empTeam = this.domainManagerDao.getTeamByEmployee(id);
		Collection empTruckPrefs = this.domainManagerDao.getEmployeeTruckPreference(id);
		
		Map<String, String> empTruckPrefMap = new HashMap<String, String>();
		Iterator<EmployeeTruckPreference> itr = empTruckPrefs.iterator();itr.hasNext();
		while(itr.hasNext()){
			EmployeeTruckPreference empTruckPref = itr.next();
			empTruckPrefMap.put(empTruckPref.getId().getPrefKey(),empTruckPref.getTruckNumber());
		}
		
		WebEmployeeInfo webInfo = new WebEmployeeInfo(info, empRoles, empTruckPrefMap);
		if (empStatus != null && empStatus.size() > 0) {
			webInfo.setTrnStatus(""
					+ ((EmployeeStatus) (empStatus.toArray()[0])).isStatus());
		}
		if (empTeam != null && empTeam.size() > 0) {
			EmployeeTeam _team = (EmployeeTeam)(empTeam.toArray()[0]);
			EmployeeInfo _leadInfo = TransAdminCacheManager.getInstance().getActiveInactiveEmployeeInfo(_team.getLeadKronosId(), this);
			webInfo.setLeadInfo(_leadInfo);
		}
		if(empSupervisor != null && empSupervisor.size() > 0) {
			webInfo.setEmpSupervisor( (EmployeeSupervisor) ((List)empSupervisor).get(0));
			webInfo.setHomeSupervisorId(webInfo.getEmpSupervisor().getId().getSupervisorId());
		}
		return webInfo;
	}
	
	public WebEmployeeInfo getEmployeeEx(String id) {
		
		EmployeeInfo info = TransAdminCacheManager.getInstance().getActiveInactiveEmployeeInfo(id, this);		
		return new WebEmployeeInfo(info, null);
	}
	

	public EmployeeRoleType getEmployeeRoleType(String roleTypeId) {
		return getDomainManagerDao().getEmployeeRoleType(roleTypeId);
	}

	public EmployeeSubRoleType getEmployeeSubRoleType(String subRoleTypeId) {
		return getDomainManagerDao().getEmployeeSubRoleType(subRoleTypeId);
	}

	public Collection getEmployeeRoleTypes() {
		return getDomainManagerDao().getEmployeeRoleTypes();
	}

	public Collection getEmployeeSubRoleTypes() {
		return getDomainManagerDao().getEmployeeSubRoleTypes();
	}
	
	public void storeEmployeeTruckPreference(WebEmployeeInfo empInfo){
		// Store only the EmployeeTruckPreferences as of now Employee is readOnly
		Map<String, String> truckPrefMap = empInfo.getEmpTruckPreferences();
		
		//Remove if any existing Truck Preferences
		Collection oldTruckPrefList = getDomainManagerDao().getEmployeeTruckPreference(empInfo.getEmployeeId());
		removeEntity(oldTruckPrefList);
		
		List<EmployeeTruckPreference> finalTruckPrefList = new ArrayList<EmployeeTruckPreference>();
		Iterator itr = truckPrefMap.entrySet().iterator();
		while (itr.hasNext()) {
			Map.Entry<String, String> truckPrefs = (Map.Entry<String, String>) itr.next();

			EmployeeTruckPreference empTruckPref = new EmployeeTruckPreference();
			EmployeeTruckPreferenceId empTruckPrefId = new EmployeeTruckPreferenceId();
			empTruckPrefId.setKronosId(empInfo.getEmployeeId());
			empTruckPrefId.setPrefKey(truckPrefs.getKey());
			empTruckPref.setId(empTruckPrefId);
			empTruckPref.setTruckNumber(truckPrefs.getValue());
			if (empTruckPref.getTruckNumber() != null)
			finalTruckPrefList.add(empTruckPref);
		}
		 
		if(finalTruckPrefList.size() > 0)
			saveEntityList(finalTruckPrefList);
		
	}
	public void storeEmployees(WebEmployeeInfo employeeInfo) {
		// store only the EmployeeRole because as of now Employee is readOnly
		Collection roleList = employeeInfo.getEmpRole();
		Collection oldRoleList = getDomainManagerDao().getEmployeeRole(
				employeeInfo.getEmployeeId());
		removeEntity(oldRoleList);
		saveEntityList(roleList);
		if (employeeInfo.getTrnStatus() != null
				&& employeeInfo.getTrnStatus().length() > 0) {
			EmployeeStatus status = new EmployeeStatus(employeeInfo
					.getEmployeeId(), Boolean.parseBoolean(employeeInfo
					.getTrnStatus()));
			saveEntity(status);
		} else if (employeeInfo.getTrnStatus() == null) {
			EmployeeStatus status = new EmployeeStatus(employeeInfo
					.getEmployeeId(), true);
			List l = new ArrayList();
			l.add(status);
			removeEntity(l);
		}
		
		Collection oldSupList = getDomainManagerDao().getEmployeeSupervisor(
				employeeInfo.getEmployeeId());
		removeEntity(oldSupList);
		if(employeeInfo.getEmpSupervisor().getId() != null && !"".equals(employeeInfo.getEmpSupervisor().getId().getSupervisorId())) {
			saveEntity(employeeInfo.getEmpSupervisor());
		}
		
		Collection<EmployeeTeam> _teamForLead = getDomainManagerDao().getTeamByLead(employeeInfo.getEmployeeId());
		Collection<EmployeeTeam> _currentTeamForEmployee = getDomainManagerDao().getTeamByEmployee(employeeInfo.getEmployeeId());
		
		if(employeeInfo.getLeadInfo() != null && 
				!TransStringUtil.isEmpty(employeeInfo.getLeadInfo().getEmployeeId())) {
			Collection<EmployeeTeam> _teamLeadAsMember = getDomainManagerDao().getTeamByEmployee(employeeInfo.getLeadInfo().getEmployeeId());
			if(_teamLeadAsMember != null && _teamLeadAsMember.size() > 0) {
				if(employeeInfo.getEmployeeId().equalsIgnoreCase(employeeInfo.getLeadInfo().getEmployeeId())) {
					throw new DataIntegrityViolationException("Cannot assign member to himself");
				} else {
					removeEntity(_teamLeadAsMember);
				}
			}
		}
		//Check if the current employee is a lead
		if(_teamForLead != null && _teamForLead.size() > 0) {
			if(employeeInfo.getLeadInfo() != null 
							&& !TransStringUtil.isEmpty(employeeInfo.getLeadInfo().getEmployeeId())
										&& !employeeInfo.getEmployeeId().equalsIgnoreCase(employeeInfo.getLeadInfo().getEmployeeId())) {
				removeEntity(_teamForLead);
				Collection<EmployeeTeam> _teamForJoin = getDomainManagerDao().getTeamByLead(employeeInfo.getLeadInfo().getEmployeeId());
				boolean memberFoundInNewTeam = false;
				if(_teamForJoin != null) {
					for(EmployeeTeam _empTeam : _teamForJoin) {
						if(_empTeam.getKronosId().equalsIgnoreCase(employeeInfo.getEmployeeId())) {
							memberFoundInNewTeam = true;
							break;
						}
					}					
				}
				if(!memberFoundInNewTeam) {
					EmployeeTeam _newMemberMapping = new EmployeeTeam(employeeInfo.getEmployeeId()
																		, employeeInfo.getLeadInfo().getEmployeeId());
					saveEntity(_newMemberMapping);
				}
			}
		} else { // not a lead now so join a new team and remove from old team if needed			
			if(_currentTeamForEmployee != null && _currentTeamForEmployee.size() > 0
					&& (employeeInfo.getLeadInfo() == null 
							|| TransStringUtil.isEmpty(employeeInfo.getLeadInfo().getEmployeeId()))) {
				// he is part of team now
				for(EmployeeTeam _empTeam : _currentTeamForEmployee) {
					
					if(_empTeam.getKronosId().equalsIgnoreCase(employeeInfo.getEmployeeId())) {
						removeEntityEx(_empTeam);
						break;
					}
				}
			} 
			if(employeeInfo.getLeadInfo() != null 
					&& !TransStringUtil.isEmpty(employeeInfo.getLeadInfo().getEmployeeId())) {
				if(employeeInfo.getEmployeeId().equalsIgnoreCase(employeeInfo.getLeadInfo().getEmployeeId())) {
					throw new DataIntegrityViolationException("Cannot assign member to himself");
				} else {
					EmployeeTeam _newMemberMapping = new EmployeeTeam(employeeInfo.getEmployeeId()
							, employeeInfo.getLeadInfo().getEmployeeId());
					saveEntity(_newMemberMapping);
				}
			}
		}
	}


	public Map<String, EmployeeInfo> getKronosTerminatedEmployees() {
		if(TransportationAdminProperties.isKronosCloudEnabled())
			return employeeManagerCloudDAO.getTerminatedEmployees();
		else
			return employeeManagerDAO.getTerminatedEmployees();
	}
	
	/* Get Employees By Role type & dispatch type, 
	 * if dispatch type is regular route fetch all 
	 * active employees from Kronos eliminating Light duty 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Collection getEmployeesByRole(String roleTypeId, String dispatchType, Date dispatchDate) {

		Collection employeeIDsByRole = domainManagerDao.getEmployeesByRoleType(roleTypeId);
		Collection employees = new ArrayList(employeeIDsByRole.size());
		Map activeEmpl = getTransAppActiveEmployees();
		if(dispatchDate != null && dispatchType != null) {
			List dispatchResources = (List) domainManagerDao.getDispatchResource(dispatchDate, EnumDispatchType.LIGHTDUTYDISPATCH.getName().equals(dispatchType) 
															? EnumDispatchType.ROUTEDISPATCH.getName(): EnumDispatchType.LIGHTDUTYDISPATCH.getName());			
			for (int i = 0; i < dispatchResources.size(); i++) {
				DispatchResource d = (DispatchResource) dispatchResources.get(i);
				EmployeeInfo info = new EmployeeInfo();
				info.setEmployeeId(d.getId().getResourceId());
				activeEmpl.remove(info);
			}
		}			
		Iterator it = employeeIDsByRole.iterator();
		while (it.hasNext()) {
			EmployeeRole empRole = (EmployeeRole) it.next();
			EmployeeInfo info = getTransAppActiveEmployees(activeEmpl
															, empRole.getId().getKronosId());
			if (info != null) {
				employees.add(info);
			}
		}
		return employees;
	}
	/*Get Employees By RoleType & SubRoleType*/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Collection getEmployeesByRoleAndSubRole(String roleTypeId, String subRoleTypeId) {

		Collection employeeIDsByRole = domainManagerDao.getEmployeesByRoleTypeAndSubRoleType(roleTypeId
																								,subRoleTypeId);
		Collection employees = new ArrayList(employeeIDsByRole.size());
		Map activeEmpl = getTransAppActiveEmployees();
		Iterator it = employeeIDsByRole.iterator();
		while (it.hasNext()) {
			EmployeeRole empRole = (EmployeeRole) it.next();
			EmployeeInfo info = getTransAppActiveEmployees(activeEmpl
																, empRole.getId().getKronosId());
			if (info != null) 
				employees.add(info);
		}
		return employees;
	}

	public void setPunchInfoDAO(PunchInfoDaoI punchInfoDAO) {
		this.punchInfoDAO = punchInfoDAO;
	}

	public Collection getPunchInfo(String date) throws Exception {
		try {		
			if(TransportationAdminProperties.isKronosCloudEnabled())
				return punchInfoCloudDAO.getPunchInfo(date);
			else
				return punchInfoDAO.getPunchInfo(date);
		} catch (Exception e) {
			throw new Exception("Could not load employee punch information from kronos database", e);
		}
	}

	public Collection getScheduleInfo(String date) {
		try {
			if (!TransportationAdminProperties.isKronosBlackhole()) {
				if(TransportationAdminProperties.isKronosCloudEnabled())
					return punchInfoCloudDAO.getScheduleInfo(date);
				else
					return punchInfoDAO.getScheduleInfo(date);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public Collection getScheduleEmployees(Date weekOf) {
		List result = new ArrayList();
		Map employeeInfos = getActiveInactiveEmployees();
		
		for (Iterator it = employeeInfos.values().iterator(); it.hasNext();) {
			EmployeeInfo eInfo = (EmployeeInfo) it.next();
			Collection schedules = getDomainManagerDao().getScheduleEmployee(eInfo.getEmployeeId(), getParsedDate(weekOf));
			ScheduleEmployeeInfo sInfo = new ScheduleEmployeeInfo();
			Collection empRoles = this.domainManagerDao.getEmployeeRole(eInfo.getEmployeeId());
			Map<String, String> employeeTeamMapping = ModelUtil.getIdMappedTeam(domainManagerDao.getTeamInfo());
			
			if (empRoles != null && empRoles.size() > 0) {
				if (EnumResourceSubType.isSchedule((EnumResourceSubType.getEnum(((EmployeeRole) empRoles.toArray()[0])
															.getEmployeeSubRoleType().getCode())))) {
					sInfo.setEmpRole(empRoles);
					sInfo.setEmpInfo(eInfo);
					sInfo.setSchedule(schedules);
					result.add(sInfo);
				}
			}
			Collection empStatus = this.domainManagerDao.getEmployeeStatus(eInfo.getEmployeeId());
			if (empStatus != null && empStatus.size() > 0) {
				sInfo.setTrnStatus(""+ ((EmployeeStatus) (empStatus.toArray()[0])).isStatus());
			}
			
			sInfo.setLeadInfo(TransAdminCacheManager.getInstance().getActiveInactiveEmployeeInfo
										(employeeTeamMapping.get(sInfo.getEmployeeId()), this));
			if(employeeTeamMapping.containsValue(sInfo.getEmployeeId())) {
				sInfo.setLead(true);
			}
		}
		return result;

	}
	
	public Collection getScheduleEmployees(Date weekOf, String day) {
		List result = new ArrayList();
		Map employeeInfos = getActiveInactiveEmployees();
		
		for (Iterator it = employeeInfos.values().iterator(); it.hasNext();) {
			EmployeeInfo eInfo = (EmployeeInfo) it.next();
			Collection schedules = getDomainManagerDao().getScheduleEmployee(eInfo.getEmployeeId(), getParsedDate(weekOf),day);
			ScheduleEmployeeInfo sInfo = new ScheduleEmployeeInfo();
			Collection empRoles = this.domainManagerDao.getEmployeeRole(eInfo.getEmployeeId());
			Map<String, String> employeeTeamMapping = ModelUtil.getIdMappedTeam(domainManagerDao.getTeamInfo());
			
			if (empRoles != null && empRoles.size() > 0) {
				if (EnumResourceSubType.isSchedule((EnumResourceSubType.getEnum(((EmployeeRole) empRoles.toArray()[0])
															.getEmployeeSubRoleType().getCode())))) {
					sInfo.setEmpRole(empRoles);
					sInfo.setEmpInfo(eInfo);
					sInfo.setSchedule(schedules);
					result.add(sInfo);
				}
			}
			Collection empStatus = this.domainManagerDao.getEmployeeStatus(eInfo.getEmployeeId());
			if (empStatus != null && empStatus.size() > 0) {
				sInfo.setTrnStatus(""+ ((EmployeeStatus) (empStatus.toArray()[0])).isStatus());
			}
			
			sInfo.setLeadInfo(TransAdminCacheManager.getInstance().getActiveInactiveEmployeeInfo
										(employeeTeamMapping.get(sInfo.getEmployeeId()), this));
			if(employeeTeamMapping.containsValue(sInfo.getEmployeeId())) {
				sInfo.setLead(true);
			}
		}
		return result;

	}

	public WebSchedule getSchedule(String id, Date weekOf) {
		EmployeeInfo info = TransAdminCacheManager.getInstance().getActiveInactiveEmployeeInfo(id, this);
		Collection schedules = getDomainManagerDao().getScheduleEmployee(id, getParsedDate(weekOf));
		WebSchedule s = new WebSchedule(null, weekOf);
		s.setEmpInfo(info);
		s.setSchdules(schedules);
		return s;
	}
	
	
	public Collection getPunchInfoPayCode(String date) {
		Collection off = null;
		try {
			if (!TransportationAdminProperties.isKronosBlackhole()) {
				if(TransportationAdminProperties.isKronosCloudEnabled())
					off = punchInfoCloudDAO.getPunchInfoPayCode(date);
				else
					off = punchInfoDAO.getPunchInfoPayCode(date);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return off;
	}

	public Collection getScheduledEmployees(String day, String date) {
		List result = new ArrayList();
		Collection c = null;
		long start = System.currentTimeMillis();
		
		try {
			c = getDomainManagerDao().getScheduleEmployees(
					TransStringUtil.getServerDate(TransStringUtil
							.getWeekOf(date)), day);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Map activeEmpl = getTransAppActiveEmployees();

		// logic for active inactive employees eligible for plan also terminated
		// employees
		Set<String> resourceIds = new HashSet<String>();
		if(c != null) {
			for (Iterator it = c.iterator(); it.hasNext();) {
				ScheduleEmployee se = (ScheduleEmployee) it.next();
				EmployeeInfo info = getTransAppActiveEmployees(activeEmpl, se
						.getEmployeeId());
				// EmployeeInfo
				// info=TransAdminCacheManager.getInstance().getEmployeeInfo(se.getEmployeeId(),this);
				if (info == null) {
					it.remove();
					continue;
				}else{
					resourceIds.add(se.getEmployeeId());
				}
				/*
				 * Collection
				 * empStatus=this.domainManagerDao.getEmployeeStatus(se.getEmployeeId
				 * ()); String trnStatus=null;
				 * if(empStatus!=null&&empStatus.size()>0) {
				 * trnStatus=""+((EmployeeStatus
				 * )(empStatus.toArray()[0])).isStatus(); }
				 * if(!DispatchPlanUtil.isEligibleForPlan(info.getStatus(),
				 * trnStatus)) { it.remove(); }
				 */
			}
			Map empRoleMap = null;
			if (resourceIds != null && resourceIds.size() > 0) {
				empRoleMap = getEmployeeRoles(resourceIds);
			}
			Collection off = getPunchInfoPayCode(date);
			for (Iterator it = c.iterator(); it.hasNext();) {
				ScheduleEmployee se = (ScheduleEmployee) it.next();
				boolean isOff = false;
				if (off != null)
					for (Iterator itt = off.iterator(); itt.hasNext();) {
						PunchInfo p = (PunchInfo) itt.next();
						if (se.getEmployeeId().equals(p.getEmployeeId()))
							isOff = true;
					}
				if (isOff)
					continue;
				ScheduleEmployeeDetails detail = new ScheduleEmployeeDetails();
				detail.setSchedule(se);
				detail.setEmpRoles((empRoleMap.get(se.getEmployeeId())!=null)?(List<EmployeeRole>)empRoleMap.get(se.getEmployeeId()):null);
				detail.setInfo(getTransAppActiveEmployees(activeEmpl, se
						.getEmployeeId()));
				if (detail.getEmpRoles() != null && detail.getEmpRoles().size() > 0
						&& detail.getInfo() != null) {
					if (EnumResourceSubType
							.isSchedule((EnumResourceSubType
									.getEnum(((EmployeeRole) detail.getEmpRoles()
											.toArray()[0]).getEmployeeSubRoleType()
											.getCode())))) {
						result.add(detail);
					}
				}
			}
			long end = System.currentTimeMillis();
			System.err.println("getScheduledEmployees in (sec) "+(end-start)/1000);
				
		}

		
		return result;
	}

	public Collection getUnAvailableEmployeesScheduleTime(Collection plans, String date) {

		List result = new ArrayList();
		Collection off = getScheduleInfo(date);
		for (Iterator i = plans.iterator(); i.hasNext();) {
			Plan p = (Plan) i.next();
			Set Planresources = p.getPlanResources();
			if (Planresources != null)
				for (Iterator j = Planresources.iterator(); j.hasNext();) {
					PlanResource r = (PlanResource) j.next();
					boolean isPunchAvalable = false;
					if (r != null && off != null)
						for (Iterator k = off.iterator(); k.hasNext();) {
							PunchInfo punch = (PunchInfo) k.next();
							if (r.getId().getResourceId().equals(
									punch.getEmployeeId())) {
								if (DispatchPlanUtil.isEligibleForUnAvailable(domainManagerDao.getEmployeeRole(r.getId().getResourceId()))) {
									isPunchAvalable = true;
									try {
										String planTime = TransStringUtil.getServerTime(p.getStartTime());
										String punchTime = "";
										if (punch.getStartTime() != null)
											punchTime = TransStringUtil.getServerTime(punch.getStartTime());
										if ("003".equalsIgnoreCase(r.getEmployeeRoleType().getCode())) {
											String day = TransStringUtil.getServerDay(TransStringUtil.getServerDateString(date)).toUpperCase();
											ScheduleEmployee se = getSchedule(r.getId().getResourceId(), TransStringUtil.getServerDate(TransStringUtil.getWeekOf(date)),
													day);
											if (se != null && se.getDispatchGroupTime() != null)
												planTime = TransStringUtil.getServerTime(se.getDispatchGroupTime());
										}
										if (r.getId().getAdjustmentTime() != null) {
											planTime = TransStringUtil.getServerTime(r.getId().getAdjustmentTime());
										}
										if (planTime != null && !planTime.equalsIgnoreCase(punchTime)) {
											WebPlanResource wpr = new WebPlanResource();
											wpr.setEmp(getEmployee(punch.getEmployeeId()));
											wpr.setPlanId(p.getPlanId());
											wpr.setPaycode("Schedule");
											result.add(wpr);
										}

									} catch (ParseException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}

							}
						}
					if (!isPunchAvalable) {
						WebPlanResource wpr = new WebPlanResource();
						wpr.setEmp(getEmployee(r.getId().getResourceId()));
						wpr.setPlanId(p.getPlanId());
						wpr.setPaycode("Schedule");
						if (DispatchPlanUtil
								.isEligibleForUnAvailable(domainManagerDao.getEmployeeRole(r.getId().getResourceId()))) {
							result.add(wpr);
						}
					}
				}
		}
		return result;
	}

	public Collection getUnAvailableEmployees(Collection plans, String date) {
		List result = new ArrayList();
		Collection off = getPunchInfoPayCode(date);
		for (Iterator i = plans.iterator(); i.hasNext();) {
			Plan p = (Plan) i.next();
			Set Planresources = p.getPlanResources();
			if (Planresources != null)
				for (Iterator j = Planresources.iterator(); j.hasNext();) {
					PlanResource r = (PlanResource) j.next();
					if (r != null && off != null)
						for (Iterator k = off.iterator(); k.hasNext();) {
							PunchInfo punch = (PunchInfo) k.next();
							if (r.getId().getResourceId().equals(
									punch.getEmployeeId())) {
								WebPlanResource wpr = new WebPlanResource();
								wpr.setEmp(getEmployee(punch.getEmployeeId()));
								wpr.setPlanId(p.getPlanId());
								wpr.setPaycode(punch.getPaycode());
								if (DispatchPlanUtil
										.isEligibleForUnAvailable(domainManagerDao
												.getEmployeeRole(r.getId()
														.getResourceId()))) {
									result.add(wpr);
								}
							}
						}
				}
		}
		Collection sConflict = getUniqueEmployees(getUnAvailableEmployeesScheduleTime(
				plans, date));
		result.addAll(sConflict);
		return getUniqueEmployees(result);
	}

	public Collection getUniqueEmployees(Collection c) {
		List unique = new ArrayList();

		for (Iterator i = c.iterator(); i.hasNext();) {
			WebPlanResource wpr = (WebPlanResource) i.next();
			boolean isUnique = true;
			for (Iterator j = unique.iterator(); j.hasNext();) {
				WebPlanResource wpr1 = (WebPlanResource) j.next();
				if (wpr1.getEmployeeId() != null
						&& wpr1.getEmployeeId().equalsIgnoreCase(
								wpr.getEmployeeId())) {
					isUnique = false;
				}
			}
			if (isUnique)
				unique.add(wpr);
		}
		return unique;
	}

	public ScheduleEmployee getSchedule(String id, String weekOf, String day) {
		Collection c = getDomainManagerDao().getScheduleEmployee(id, weekOf, day);
		if (c != null && c.size() > 0) {
			Iterator i = c.iterator();
			return (ScheduleEmployee) i.next();
		}
		return null;
	}

	public Collection getKronosActiveInactiveEmployees() {
		// TODO Auto-generated method stub
		if(TransportationAdminProperties.isKronosCloudEnabled())
			return employeeManagerCloudDAO.getActiveInactiveEmployees();
		else
			return employeeManagerDAO.getActiveInactiveEmployees();
	}

	public void syncEmployess() {
		if(TransportationAdminProperties.isKronosCloudEnabled())
			return;
		employeeManagerDAO.refresh("KRONOS_EMPLOYEE");	
		TransAdminCacheManager.getInstance().refreshCacheData(EnumCachedDataType.EMPLOYEE_DATA);
		
		Collection transAppEmployees = this.domainManagerDao
				.getEmployeeStatus(null);
		if (transAppEmployees != null && transAppEmployees.size() > 0) {
			List l = new ArrayList();
			for (Iterator iterator = transAppEmployees.iterator(); iterator
					.hasNext();) {
				EmployeeStatus e = (EmployeeStatus) iterator.next();
				EmployeeInfo em = TransAdminCacheManager.getInstance()
						.getActiveInactiveEmployeeInfo(e.getPersonnum(), this);
				if (e != null && em != null && e.isStatus() && "Active".equalsIgnoreCase(em.getStatus())) {
					l.add(e);
				}
				if (e != null && em != null && !e.isStatus() && "Inactive".equalsIgnoreCase(em.getStatus())) {
					l.add(e);
				}
			}
			if (l.size() > 0) {
				domainManagerDao.removeEntity(l);
			}
		}

	}

	public Collection getEmployeeRole(String empId) {
		return getDomainManagerDao().getEmployeeRole(empId);
	}
	
	public Map<EmployeeInfo, Set<EmployeeInfo>> getTeams() {
		
		Map<EmployeeInfo, Set<EmployeeInfo>> result = new HashMap<EmployeeInfo, Set<EmployeeInfo>>();
		Collection<EmployeeTeam> teams = getDomainManagerDao().getTeamInfo();
		EmployeeInfo _lead = null;
		EmployeeInfo _member = null;
		if(teams != null) {
			for(EmployeeTeam _tmpTeam : teams) {
				_lead = TransAdminCacheManager.getInstance().getActiveInactiveEmployeeInfo(_tmpTeam.getLeadKronosId(), this);
				_member = TransAdminCacheManager.getInstance().getActiveInactiveEmployeeInfo(_tmpTeam.getKronosId(), this);
				if(_lead != null && _member != null) {
					if(!result.containsKey(_lead)) {
						result.put(_lead, new HashSet<EmployeeInfo>());
					}
					result.get(_lead).add(_member);
				}
			}
		}
		return result;
	}
	
	public Map<String, String> getTeamMapping() {
		return ModelUtil.getIdMappedTeam(domainManagerDao.getTeamInfo());
	}
	
	public Map getEmployeeRoles(Set empIds){
		return processList(empIds, 0);
		}
	public Map getEmployeeStatus(Set empIds) {
		return processList(empIds, 1);
	}
	
	public Map getEmployeeStatus() {
		return getDomainManagerDao().getEmployeeStatus();
	}
	
	
	public Map getEmployeeTruckPref(Set empIds)
	{
		return processList(empIds, 2);
	}
	public Map getTeamByEmployees(Set empIds)
	{
		return processList(empIds, 3);
	}
	
	public Map processList(Set empIds, int type)
	{
		List<String> empIdList = setToList(empIds);
		Map map = new HashMap();
		for (List part : Lists.partition(empIdList, 800))
		{
			switch(type)
			{
			case 0:map.putAll(getDomainManagerDao().getEmpRolesByIds(listToSet(part)));break;
			case 1:map.putAll(getDomainManagerDao().getEmployeeStatusByIds(listToSet(part)));break;
			case 2:map.putAll(getDomainManagerDao().getEmployeeTruckPreferenceByIds(listToSet(part)));break;
			case 3:map.putAll(getDomainManagerDao().getTeamByEmployees(listToSet(part))); break;
			default:break;
			}
		}
		return map;
	
	}
	public List setToList(Set set) {
	    return new ArrayList(set);
	}
	public Set listToSet(List list) {
	    return new HashSet(list);
	}

	public PunchInfoDaoI getPunchInfoCloudDAO() {
		return punchInfoCloudDAO;
	}

	public void setPunchInfoCloudDAO(PunchInfoDaoI punchInfoCloudDAO) {
		this.punchInfoCloudDAO = punchInfoCloudDAO;
	}

	public EmployeeManagerDaoI getEmployeeManagerCloudDAO() {
		return employeeManagerCloudDAO;
	}

	public void setEmployeeManagerCloudDAO(
			EmployeeManagerDaoI employeeManagerCloudDAO) {
		this.employeeManagerCloudDAO = employeeManagerCloudDAO;
	}
	

	@Override
	public void syncEmployees() throws Exception {
		List<KronosEmployeeInfo> kronosemplist = new ArrayList<KronosEmployeeInfo>();
		List<KronosEmployeeInfo> transpemplist = new ArrayList<KronosEmployeeInfo>();
		List<KronosEmployeeInfo> inserts = new ArrayList<KronosEmployeeInfo>();
		List<KronosEmployeeInfo> updates = new ArrayList<KronosEmployeeInfo>();
		List<KronosEmployeeInfo> deletes = new ArrayList<KronosEmployeeInfo>();

	
		if (TransportationAdminProperties.isKronosCloudEnabled()) {
				kronosemplist = employeeManagerCloudDAO.getAllKronosEmployees();
				transpemplist = employeeManagerDAO.getAllKronosEmployees();
		}

		for (KronosEmployeeInfo emp : kronosemplist) {
				if (transpemplist.contains(emp)) {
					updates.add(emp);
				} else {
					inserts.add(emp);
				}
		}

		transpemplist.removeAll(updates);
		transpemplist.removeAll(inserts);
		deletes = transpemplist;

		employeeManagerDAO.syncEmployees(inserts, updates, deletes);
			
			
	}
}
