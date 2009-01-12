package com.freshdirect.transadmin.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.freshdirect.transadmin.dao.BaseManagerDaoI;
import com.freshdirect.transadmin.dao.DomainManagerDaoI;
import com.freshdirect.transadmin.dao.EmployeeManagerDaoI;
import com.freshdirect.transadmin.dao.LocationManagerDaoI;
import com.freshdirect.transadmin.model.EmployeeInfo;
import com.freshdirect.transadmin.model.EmployeeRole;
import com.freshdirect.transadmin.model.EmployeeRoleType;
import com.freshdirect.transadmin.model.TrnEmployee;
import com.freshdirect.transadmin.service.EmployeeManagerI;
import com.freshdirect.transadmin.util.ModelUtil;
import com.freshdirect.transadmin.util.TransAdminCacheManager;
import com.freshdirect.transadmin.web.model.WebEmployeeInfo;

public class EmployeeManagerImpl extends BaseManagerImpl implements EmployeeManagerI {
	
	private EmployeeManagerDaoI employeeManagerDAO = null;
	private DomainManagerDaoI domainManagerDao = null;
	
	
	public EmployeeManagerDaoI getEmployeeManagerDAO() {
		return employeeManagerDAO;
	}
	
	
	protected BaseManagerDaoI getBaseManageDao() {
		// TODO Auto-generated method stub
		return getDomainManagerDao();
	}
	
	public Collection getEmployees() {
		// TODO Auto-generated method stub
		// first get the kornos data
		// then get the role for the kornos data
		//then construct the viewer model
		// return the viewer model 
		Collection kronoEmployees=TransAdminCacheManager.getInstance().getAllEmployeeInfo(this);
		
		Collection employeeRolesList=domainManagerDao.getEmployeeRoles();
		
		System.out.println("kronoEmployees"+kronoEmployees);
		System.out.println("employeeRolesList"+employeeRolesList);
		
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
		// TODO Auto-generated method stub
		// get the empInfo from the cache
		// get the role from the db
		// wrap it and return the data simple
		
		EmployeeInfo info=TransAdminCacheManager.getInstance().getEmployeeInfo(id,this);
		Collection empRoles=this.domainManagerDao.getEmployeeRole(id);
		WebEmployeeInfo webInfo=new WebEmployeeInfo(info,empRoles);
		//this.domainManagerDao.
		
		return webInfo;
	}
	
	public EmployeeRoleType getEmployeeRoleType(String roleTypeId) {
		// TODO Auto-generated method stub
		return getDomainManagerDao().getEmployeeRoleType(roleTypeId);
	}
	
	public Collection getEmployeeJobType() {
		
		return getDomainManagerDao().getEmployeeJobType();
	}
	
	public Collection getEmployeeRoleTypes() {
		return getDomainManagerDao().getEmployeeRoleTypes();
	}

	public void storeEmployees(WebEmployeeInfo employeeInfo) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		// store only the EmployeeRole because as of now Employee is readOnly
		Collection roleList=employeeInfo.getEmpRole();
		Collection oldRoleList=getDomainManagerDao().getEmployeeRole(employeeInfo.getEmployeeId());
		removeEntity(oldRoleList);
		saveEntityList(roleList);
		
	}


	public Collection getTerminatedEmployees() {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		// first get the kornos data
		// then get the role for the kornos data
		//then construct the viewer model
		// return the viewer model 
		Collection kronoEmployees=TransAdminCacheManager.getInstance().getAllTerminatedEmployeeInfo(this);
		
		Collection employeeRolesList=domainManagerDao.getEmployeeRoles();
		
		System.out.println("kronoTerminatedEmployees"+kronoEmployees);
		System.out.println("employeeTerminatedRolesList"+employeeRolesList);
		
		Collection finalList=ModelUtil.getTrnAdminEmployeeList((List)kronoEmployees,(List)employeeRolesList);
		
		 return finalList;
	}
	

	public Collection getKronosTerminatedEmployees() {
		// TODO Auto-generated method stub
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

	
	
}	
	

