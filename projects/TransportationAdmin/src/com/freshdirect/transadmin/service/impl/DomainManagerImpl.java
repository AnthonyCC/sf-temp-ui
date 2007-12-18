package com.freshdirect.transadmin.service.impl;

import java.util.Collection;

import com.freshdirect.transadmin.dao.BaseManagerDaoI;
import com.freshdirect.transadmin.dao.DomainManagerDaoI;
import com.freshdirect.transadmin.model.TrnEmployee;
import com.freshdirect.transadmin.model.TrnRoute;
import com.freshdirect.transadmin.model.TrnTruck;
import com.freshdirect.transadmin.model.TrnZone;
import com.freshdirect.transadmin.service.DomainManagerI;

public class DomainManagerImpl  
		extends BaseManagerImpl  implements DomainManagerI {
	
	private DomainManagerDaoI domainManagerDao = null;
	
	public DomainManagerDaoI getDomainManagerDao() {
		return domainManagerDao;
	}

	public void setDomainManagerDao(DomainManagerDaoI domainManagerDao) {
		this.domainManagerDao = domainManagerDao;
	}
	
	protected BaseManagerDaoI getBaseManageDao() {
		return getDomainManagerDao();
	}

	public Collection getEmployees() {
		
		return getDomainManagerDao().getEmployees();
	}
	
	public Collection getZones() {
		
		return getDomainManagerDao().getZones();
	}
	
	public Collection getRoutes() {
		
		return getDomainManagerDao().getRoutes();
	}
	
	public Collection getRouteForZone(String zoneId) {
		return getDomainManagerDao().getRouteForZone(zoneId);
	}
	
	public Collection getTrucks() {
		
		return getDomainManagerDao().getTrucks();
	}
	
	public Collection getEmployeeJobType() {
		
		return getDomainManagerDao().getEmployeeJobType();
	}
	
	public Collection getTimeSlots() {
		return getDomainManagerDao().getTimeSlots();
	}
	
	public String[] getDays() {
		return getDomainManagerDao().getDays();
	}
	
	public String[] getTimings() {
		return getDomainManagerDao().getTimings();
	}
	
	public String[] getTruckTypes() {
		return getDomainManagerDao().getTruckTypes();
	}
	
	public Collection getSupervisors() {
		return getDomainManagerDao().getSupervisors(); 
	}
		
	
	public TrnEmployee getEmployee(String id)  {
		return getDomainManagerDao().getEmployee(id);	
	}
	
	public TrnZone getZone(String id)  {
		return getDomainManagerDao().getZone(id);	
	}
	
	public TrnRoute getRoute(String id)  {
		return getDomainManagerDao().getRoute(id);	
	}
	
	public TrnTruck getTruck(String id)  {
		return getDomainManagerDao().getTruck(id);	
	}
	
	
}
