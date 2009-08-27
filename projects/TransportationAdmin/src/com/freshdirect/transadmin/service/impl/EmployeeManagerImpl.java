package com.freshdirect.transadmin.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.dao.DataAccessException;

import com.freshdirect.transadmin.dao.BaseManagerDaoI;
import com.freshdirect.transadmin.dao.DomainManagerDaoI;
import com.freshdirect.transadmin.dao.EmployeeManagerDaoI;
import com.freshdirect.transadmin.dao.PunchInfoDaoI;
import com.freshdirect.transadmin.dao.PunchInfoDaoI;
import com.freshdirect.transadmin.model.EmployeeInfo;
import com.freshdirect.transadmin.model.EmployeeRole;
import com.freshdirect.transadmin.model.EmployeeRoleType;
import com.freshdirect.transadmin.model.Plan;
import com.freshdirect.transadmin.model.PlanResource;
import com.freshdirect.transadmin.model.PunchInfo;
import com.freshdirect.transadmin.model.ScheduleEmployee;
import com.freshdirect.transadmin.model.ScheduleEmployeeInfo;
import com.freshdirect.transadmin.service.EmployeeManagerI;
import com.freshdirect.transadmin.util.ModelUtil;
import com.freshdirect.transadmin.util.TransAdminCacheManager;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.util.TransportationAdminProperties;
import com.freshdirect.transadmin.util.scrib.SchdeuleEmployeeDetails;
import com.freshdirect.transadmin.web.model.WebEmployeeInfo;
import com.freshdirect.transadmin.web.model.WebPlanResource;
import com.freshdirect.transadmin.web.model.WebSchedule;

public class EmployeeManagerImpl extends BaseManagerImpl implements EmployeeManagerI {

	private EmployeeManagerDaoI employeeManagerDAO = null;
	private DomainManagerDaoI domainManagerDao = null;
	private PunchInfoDaoI punchInfoDAO=null;

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
		//then construct the viewer model
		// return the viewer model
		Collection kronoEmployees=TransAdminCacheManager.getInstance().getAllEmployeeInfo(this);
		Collection employeeRolesList=domainManagerDao.getEmployeeRoles();
		Collection finalList=ModelUtil.getTrnAdminEmployeeList((List)kronoEmployees,(List)employeeRolesList);

		 return finalList;
	}


	public void setEmployeeManagerDAO(EmployeeManagerDaoI employeeManagerDao) {
		this.employeeManagerDAO = employeeManagerDao;
	}


	public Collection getKronosEmployees(){
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

		EmployeeInfo info=TransAdminCacheManager.getInstance().getEmployeeInfo(id,this);
		Collection empRoles=this.domainManagerDao.getEmployeeRole(id);
		WebEmployeeInfo webInfo=new WebEmployeeInfo(info,empRoles);
		return webInfo;
	}

	public EmployeeRoleType getEmployeeRoleType(String roleTypeId) {
		return getDomainManagerDao().getEmployeeRoleType(roleTypeId);
	}

	public Collection getEmployeeJobType() {

		return getDomainManagerDao().getEmployeeJobType();
	}

	public Collection getEmployeeRoleTypes() {
		return getDomainManagerDao().getEmployeeRoleTypes();
	}

	public void storeEmployees(WebEmployeeInfo employeeInfo) {
		// store only the EmployeeRole because as of now Employee is readOnly
		Collection roleList=employeeInfo.getEmpRole();
		Collection oldRoleList=getDomainManagerDao().getEmployeeRole(employeeInfo.getEmployeeId());
		removeEntity(oldRoleList);
		saveEntityList(roleList);

	}


	public Collection getTerminatedEmployees() {
		// first get the kornos data
		// then get the role for the kornos data
		//then construct the viewer model
		// return the viewer model
		Collection kronoEmployees=TransAdminCacheManager.getInstance().getAllTerminatedEmployeeInfo(this);
		Collection employeeRolesList=domainManagerDao.getEmployeeRoles();
		Collection finalList=ModelUtil.getTrnAdminEmployeeList((List)kronoEmployees,(List)employeeRolesList);
		return finalList;
	}


	public Collection getKronosTerminatedEmployees() {
		return employeeManagerDAO.getTerminatedEmployees();
	}


	public Collection getEmployeesByRole(String roleTypeId) {

		Collection employeeIDsByRole=domainManagerDao.getEmployeesByRoleType(roleTypeId);
		Collection employees=new ArrayList(employeeIDsByRole.size());
		Iterator it=employeeIDsByRole.iterator();
		while(it.hasNext()) {
			EmployeeRole empRole=(EmployeeRole)it.next();
			EmployeeInfo info=TransAdminCacheManager.getInstance().getEmployeeInfo(empRole.getId().getKronosId(),this);
			if(info!=null) {
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
			if(!TransportationAdminProperties.isKronosBlackhole()) {
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
			if(!TransportationAdminProperties.isKronosBlackhole()) {
				return punchInfoDAO.getScheduleInfo(date);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public Collection getScheduleEmployees()
	{
		List result=new ArrayList();
		Collection employeeInfos=getKronosEmployees();
		for(Iterator it=employeeInfos.iterator();it.hasNext();)
		{
			EmployeeInfo eInfo=(EmployeeInfo)it.next();
			Collection schedules=getDomainManagerDao().getScheduleEmployee(eInfo.getEmployeeId());
			ScheduleEmployeeInfo sInfo=new ScheduleEmployeeInfo();
			Collection empRoles=this.domainManagerDao.getEmployeeRole(eInfo.getEmployeeId());
			if(empRoles!=null&&empRoles.size()>0)
			{
				sInfo.setEmpRole(empRoles);
				sInfo.setEmpInfo(eInfo);
				sInfo.setSchedule(schedules);
				result.add(sInfo);
			}
		}
		return result;
		
	}

	public WebSchedule getSchedule(String id) {
		EmployeeInfo info=TransAdminCacheManager.getInstance().getEmployeeInfo(id,this);
		Collection schedules=getDomainManagerDao().getScheduleEmployee(id);
		WebSchedule s=new WebSchedule();
		s.setEmpInfo(info);
		s.setSchdules(schedules);
		return s;
	}

	public Collection getPunchInfoPayCode(String date)
	{
		Collection off=null;
	    try {
	    	if(!TransportationAdminProperties.isKronosBlackhole()) 
	    	{				
	    		off=punchInfoDAO.getPunchInfoPayCode(date);
	    	}
		} catch (Exception e) 
		{
			e.printStackTrace();			
		}
		return off;
	}
	
	public Collection getScheduledEmployees(String day,String date)
	{
	    List result=new ArrayList();
	    Collection c=getDomainManagerDao().getScheduleEmployees(day);
	    Collection off=getPunchInfoPayCode(date);
		for(Iterator it=c.iterator();it.hasNext();)
		{
			ScheduleEmployee se=(ScheduleEmployee)it.next();
			boolean isOff=false;
			if(off!=null)
			for(Iterator itt=off.iterator();itt.hasNext();)
			{
				PunchInfo p=(PunchInfo)itt.next();
				if(se.getEmployeeId().equals(p.getEmployeeId())) isOff=true;
			}
			if(isOff)continue;
			SchdeuleEmployeeDetails detail=new SchdeuleEmployeeDetails();
			detail.setSchedule(se);
			detail.setEmpRoles(this.domainManagerDao.getEmployeeRole(se.getEmployeeId()));
			detail.setInfo(TransAdminCacheManager.getInstance().getEmployeeInfo(se.getEmployeeId(),this));
			if(detail.getEmpRoles()!=null&&detail.getEmpRoles().size()>0&&detail.getInfo()!=null)
			result.add(detail);
		}
		return result;
	}
	
	public Collection getUnAvailableEmployeesScheduleTime(Collection plans,String date)
	{
		List result=new ArrayList();		
		Collection off=getScheduleInfo(date);
		for(Iterator i=plans.iterator();i.hasNext();)
		{
			Plan p=(Plan)i.next();
			Set Planresources=p.getPlanResources();
			if(Planresources!=null)
			for(Iterator j=Planresources.iterator();j.hasNext();)
			{
				PlanResource r=(PlanResource)j.next();
				boolean isPunchAvalable=false;
				if(r!=null&&off!=null)
				for(Iterator k=off.iterator();k.hasNext();)
				{
					PunchInfo punch=(PunchInfo)k.next();
					if(r.getId().getResourceId().equals(punch.getEmployeeId()))
					{
						isPunchAvalable=true;
						try {
							String planTime=TransStringUtil.getServerTime(p.getStartTime());
							String punchTime="";
							if(punch.getStartTime()!=null)punchTime=TransStringUtil.getServerTime(punch.getStartTime());
							if("003".equalsIgnoreCase(r.getEmployeeRoleType().getCode()))
							{
								String day=TransStringUtil.getServerDay(TransStringUtil.getServerDateString(date)).toUpperCase();
								ScheduleEmployee se=getSchedule(r.getId().getResourceId(),day);
								if(se!=null&&se.getTime()!=null)planTime=TransStringUtil.getServerTime(se.getTime());
							}
							if(r.getId().getAdjustmentTime()!=null)
							{
								planTime=TransStringUtil.getServerTime(r.getId().getAdjustmentTime());
							}
							if(planTime!=null&&!planTime.equalsIgnoreCase(punchTime))
							{
								WebPlanResource wpr=new WebPlanResource();
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
				if(!isPunchAvalable)
				{
					WebPlanResource wpr=new WebPlanResource();
					wpr.setEmp(getEmployee(r.getId().getResourceId()));
					wpr.setPlanId(p.getPlanId());
					wpr.setPaycode("Schedule");					
					result.add(wpr);
				}
			}
		}
		return result;
	}
	
	public Collection getUnAvailableEmployees(Collection plans,String date)
	{
		List result=new ArrayList();
		Collection off=getPunchInfoPayCode(date);
		for(Iterator i=plans.iterator();i.hasNext();)
		{
			Plan p=(Plan)i.next();
			Set Planresources=p.getPlanResources();
			if(Planresources!=null)
			for(Iterator j=Planresources.iterator();j.hasNext();)
			{
				PlanResource r=(PlanResource)j.next();
				if(r!=null&&off!=null)
				for(Iterator k=off.iterator();k.hasNext();)
				{
					PunchInfo punch=(PunchInfo)k.next();
					if(r.getId().getResourceId().equals(punch.getEmployeeId()))
					{
						WebPlanResource wpr=new WebPlanResource();
						wpr.setEmp(getEmployee(punch.getEmployeeId()));
						wpr.setPlanId(p.getPlanId());
						wpr.setPaycode(punch.getPaycode());
						result.add(wpr);
					}
				}
			}
		}
		Collection sConflict=getUniqueEmployees(getUnAvailableEmployeesScheduleTime(plans,date));		
		result.addAll(sConflict);
		return getUniqueEmployees(result);
	}

	public Collection getUniqueEmployees(Collection c)
	{
		List unique=new ArrayList();		
		
		for(Iterator i=c.iterator();i.hasNext();)
		{			
			WebPlanResource wpr=(WebPlanResource)i.next();	
			boolean isUnique=true;
			for(Iterator j=unique.iterator();j.hasNext();)
			{
				WebPlanResource wpr1=(WebPlanResource)j.next();
				if(wpr1.getEmployeeId()!=null&&wpr1.getEmployeeId().equalsIgnoreCase(wpr.getEmployeeId()))
				{
					isUnique=false;
				}				
			}			
			if(isUnique)unique.add(wpr);
		}		
		return unique;
	}
	public ScheduleEmployee getSchedule(String id, String day) {
		Collection c=getDomainManagerDao().getScheduleEmployee(id, day);
		if(c!=null&&c.size()>0){Iterator i=c.iterator(); return(ScheduleEmployee) i.next();}
		return null;
	}
	
}


