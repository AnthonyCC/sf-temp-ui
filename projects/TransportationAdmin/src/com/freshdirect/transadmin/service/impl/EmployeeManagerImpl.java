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

import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.transadmin.dao.BaseManagerDaoI;
import com.freshdirect.transadmin.dao.DomainManagerDaoI;
import com.freshdirect.transadmin.dao.EmployeeManagerDaoI;
import com.freshdirect.transadmin.dao.PunchInfoDaoI;
import com.freshdirect.transadmin.model.EmployeeInfo;
import com.freshdirect.transadmin.model.EmployeeRole;
import com.freshdirect.transadmin.model.EmployeeRoleType;
import com.freshdirect.transadmin.model.EmployeeStatus;
import com.freshdirect.transadmin.model.EmployeeSubRoleType;
import com.freshdirect.transadmin.model.EmployeeTeam;
import com.freshdirect.transadmin.model.Plan;
import com.freshdirect.transadmin.model.PlanResource;
import com.freshdirect.transadmin.model.PunchInfo;
import com.freshdirect.transadmin.model.ScheduleEmployee;
import com.freshdirect.transadmin.model.ScheduleEmployeeInfo;
import com.freshdirect.transadmin.service.EmployeeManagerI;
import com.freshdirect.transadmin.util.DispatchPlanUtil;
import com.freshdirect.transadmin.util.EnumResourceSubType;
import com.freshdirect.transadmin.util.ModelUtil;
import com.freshdirect.transadmin.util.TransAdminCacheManager;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.util.TransportationAdminProperties;
import com.freshdirect.transadmin.util.scrib.ScheduleEmployeeDetails;
import com.freshdirect.transadmin.web.model.WebEmployeeInfo;
import com.freshdirect.transadmin.web.model.WebPlanResource;
import com.freshdirect.transadmin.web.model.WebSchedule;

public class EmployeeManagerImpl extends BaseManagerImpl implements
		EmployeeManagerI {

	private EmployeeManagerDaoI employeeManagerDAO = null;
	private DomainManagerDaoI domainManagerDao = null;
	private PunchInfoDaoI punchInfoDAO = null;

	public PunchInfoDaoI getPunchInfoDAO() {
		return punchInfoDAO;
	}

	public EmployeeManagerDaoI getEmployeeManagerDAO() {
		return employeeManagerDAO;
	}

	protected BaseManagerDaoI getBaseManageDao() {
		return getDomainManagerDao();
	}

	public Collection getEmployees() {
		// first get the kornos data
		// then get the role for the kornos data
		// then construct the viewer model
		// return the viewer model
		Collection kronoEmployees = TransAdminCacheManager.getInstance().getActiveInactiveEmployeeInfo(this);
		Collection employeeRolesList = domainManagerDao.getEmployeeRoles();
		Collection finalList = ModelUtil.getTrnAdminEmployeeList((List) kronoEmployees, (List) employeeRolesList);
		Collection terminatedEmployees = getTerminatedEmployees();
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
		Collection kronoEmployees = TransAdminCacheManager.getInstance()
				.getAllTerminatedEmployeeInfo(this);
		Collection employeeRolesList = domainManagerDao.getEmployeeRoles();
		Collection finalList = ModelUtil.getTrnAdminEmployeeList((List) kronoEmployees, (List) employeeRolesList);
		
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

	public Collection getTransAppActiveEmployees() {
		Collection kronoEmployees = new ArrayList(TransAdminCacheManager
				.getInstance().getActiveInactiveEmployeeInfo(this));
		for (Iterator it = kronoEmployees.iterator(); it.hasNext();) {
			EmployeeInfo eInfo = (EmployeeInfo) it.next();
			Collection empStatus = this.domainManagerDao
					.getEmployeeStatus(eInfo.getEmployeeId());
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
		return kronoEmployees;
	}

	public EmployeeInfo getTransAppActiveEmployees(Collection activeEmployees,
			String empId) {
		List transpActiveEmployees = (List) activeEmployees;
		for (int i = 0; i < transpActiveEmployees.size(); i++) {
			EmployeeInfo info = (EmployeeInfo) transpActiveEmployees.get(i);
			if (info.getEmployeeId().equalsIgnoreCase(empId))
				return info;
		}
		return null;
	}

	public Collection getActiveInactiveEmployees() {
		// first get the kornos data
		// then get the role for the kornos data
		// then construct the viewer model
		// return the viewer model
		Collection kronoEmployees = TransAdminCacheManager.getInstance().getActiveInactiveEmployeeInfo(this);
		return kronoEmployees;
	}

	public void setEmployeeManagerDAO(EmployeeManagerDaoI employeeManagerDao) {
		this.employeeManagerDAO = employeeManagerDao;
	}

	public Collection getKronosEmployees() {
		return employeeManagerDAO.getEmployees();
	}

	public Collection getSupervisors() {
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
		// wrap it and return the data simple

		EmployeeInfo info = TransAdminCacheManager.getInstance().getActiveInactiveEmployeeInfo(id, this);
		Collection empRoles = this.domainManagerDao.getEmployeeRole(id);
		Collection empStatus = this.domainManagerDao.getEmployeeStatus(id);
		Collection empTeam = this.domainManagerDao.getTeamByEmployee(id);
		
		WebEmployeeInfo webInfo = new WebEmployeeInfo(info, empRoles);
		if (empStatus != null && empStatus.size() > 0) {
			webInfo.setTrnStatus(""
					+ ((EmployeeStatus) (empStatus.toArray()[0])).isStatus());
		}
		if (empTeam != null && empTeam.size() > 0) {
			EmployeeTeam _team = (EmployeeTeam)(empTeam.toArray()[0]);
			EmployeeInfo _leadInfo = TransAdminCacheManager.getInstance().getActiveInactiveEmployeeInfo(_team.getLeadKronosId(), this);
			webInfo.setLeadInfo(_leadInfo);
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

	public Collection getEmployeeJobType() {

		return getDomainManagerDao().getEmployeeJobType();
	}

	public Collection getEmployeeRoleTypes() {
		return getDomainManagerDao().getEmployeeRoleTypes();
	}

	public Collection getEmployeeSubRoleTypes() {
		return getDomainManagerDao().getEmployeeSubRoleTypes();
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
		
		Collection<EmployeeTeam> _teamForLead = getDomainManagerDao().getTeamByLead(employeeInfo.getEmployeeId());
		Collection<EmployeeTeam> _currentTeamForEmployee = getDomainManagerDao().getTeamByEmployee(employeeInfo.getEmployeeId());
		if(employeeInfo.getLeadInfo() != null) {
			Collection<EmployeeTeam> _teamLeadAsMember = getDomainManagerDao().getTeamByEmployee(employeeInfo.getLeadInfo().getEmployeeId());
			if(_teamLeadAsMember != null && _teamLeadAsMember.size() > 0) {
				removeEntity(_teamLeadAsMember);
			}
		}
		//Check if the current employee is a lead
		if(_teamForLead != null && _teamForLead.size() > 0) {
			if(employeeInfo.getLeadInfo() != null 
							&& employeeInfo.getLeadInfo().getEmployeeId() != null 
									&& employeeInfo.getLeadInfo().getEmployeeId().trim().length() > 0
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
							|| employeeInfo.getLeadInfo().getEmployeeId() == null
								|| employeeInfo.getLeadInfo().getEmployeeId().trim().length() == 0)) {
				// he is part of team now
				for(EmployeeTeam _empTeam : _currentTeamForEmployee) {
					
					if(_empTeam.getKronosId().equalsIgnoreCase(employeeInfo.getEmployeeId())) {
						removeEntityEx(_empTeam);
						break;
					}
				}
			} 
			if(employeeInfo.getLeadInfo() != null 
					&& employeeInfo.getLeadInfo().getEmployeeId() != null 
					&& employeeInfo.getLeadInfo().getEmployeeId().trim().length() > 0) {
				EmployeeTeam _newMemberMapping = new EmployeeTeam(employeeInfo.getEmployeeId()
																	, employeeInfo.getLeadInfo().getEmployeeId());
				saveEntity(_newMemberMapping);
			}
		}
	}


	public Collection getKronosTerminatedEmployees() {
		return employeeManagerDAO.getTerminatedEmployees();
	}

	public Collection getEmployeesByRole(String roleTypeId) {

		Collection employeeIDsByRole = domainManagerDao
				.getEmployeesByRoleType(roleTypeId);
		Collection employees = new ArrayList(employeeIDsByRole.size());
		Collection activeEmpl = getTransAppActiveEmployees();
		Iterator it = employeeIDsByRole.iterator();
		while (it.hasNext()) {
			EmployeeRole empRole = (EmployeeRole) it.next();
			EmployeeInfo info = getTransAppActiveEmployees(activeEmpl, empRole
					.getId().getKronosId());
			if (info != null) {
				employees.add(info);
			}
		}
		return employees;
	}

	public void setPunchInfoDAO(PunchInfoDaoI punchInfoDAO) {
		this.punchInfoDAO = punchInfoDAO;
	}

	public Collection getPunchInfo(String date) {
		try {
			if (!TransportationAdminProperties.isKronosBlackhole()) {
				return punchInfoDAO.getPunchInfo(date);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public Collection getScheduleInfo(String date) {
		try {
			if (!TransportationAdminProperties.isKronosBlackhole()) {
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
		Collection employeeInfos = getActiveInactiveEmployees();
		
		for (Iterator it = employeeInfos.iterator(); it.hasNext();) {
			EmployeeInfo eInfo = (EmployeeInfo) it.next();
			Collection schedules = getDomainManagerDao().getScheduleEmployee(eInfo.getEmployeeId(), getParsedDate(weekOf));
			ScheduleEmployeeInfo sInfo = new ScheduleEmployeeInfo();
			Collection empRoles = this.domainManagerDao.getEmployeeRole(eInfo.getEmployeeId());
			
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
		try {
			c = getDomainManagerDao().getScheduleEmployees(
					TransStringUtil.getServerDate(TransStringUtil
							.getWeekOf(date)), day);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Collection activeEmpl = getTransAppActiveEmployees();

		// logic for active inactive employees eligible for plan also terminated
		// employees
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
				detail.setEmpRoles(this.domainManagerDao.getEmployeeRole(se
						.getEmployeeId()));
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
		}

		
		return result;
	}

	public Collection getUnAvailableEmployeesScheduleTime(Collection plans,
			String date) {
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
								if (DispatchPlanUtil
										.isEligibleForUnAvailable(domainManagerDao
												.getEmployeeRole(r.getId()
														.getResourceId()))) {
									isPunchAvalable = true;
									try {
										String planTime = TransStringUtil
												.getServerTime(p.getStartTime());
										String punchTime = "";
										if (punch.getStartTime() != null)
											punchTime = TransStringUtil
													.getServerTime(punch
															.getStartTime());
										if ("003".equalsIgnoreCase(r
												.getEmployeeRoleType()
												.getCode())) {
											String day = TransStringUtil
													.getServerDay(
															TransStringUtil
																	.getServerDateString(date))
													.toUpperCase();
											ScheduleEmployee se = getSchedule(
													r.getId().getResourceId(),
													TransStringUtil
															.getServerDate(TransStringUtil
																	.getWeekOf(date)),
													day);
											if (se != null
													&& se.getTime() != null)
												planTime = TransStringUtil
														.getServerTime(se
																.getTime());
										}
										if (r.getId().getAdjustmentTime() != null) {
											planTime = TransStringUtil
													.getServerTime(r
															.getId()
															.getAdjustmentTime());
										}
										if (planTime != null
												&& !planTime
														.equalsIgnoreCase(punchTime)) {
											WebPlanResource wpr = new WebPlanResource();
											wpr.setEmp(getEmployee(punch
													.getEmployeeId()));
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
								.isEligibleForUnAvailable(domainManagerDao
										.getEmployeeRole(r.getId()
												.getResourceId()))) {
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
		return employeeManagerDAO.getActiveInactiveEmployees();
	}

	public void syncEmployess() {

		Collection transAppEmployees = this.domainManagerDao
				.getEmployeeStatus(null);
		if (transAppEmployees != null && transAppEmployees.size() > 0) {
			List l = new ArrayList();
			for (Iterator iterator = transAppEmployees.iterator(); iterator
					.hasNext();) {
				EmployeeStatus e = (EmployeeStatus) iterator.next();
				EmployeeInfo em = TransAdminCacheManager.getInstance()
						.getActiveInactiveEmployeeInfo(e.getPersonnum(), this);
				if (e.isStatus() && "Active".equalsIgnoreCase(em.getStatus())) {
					l.add(e);
				}
				if (!e.isStatus()
						&& "Inactive".equalsIgnoreCase(em.getStatus())) {
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
		
}
