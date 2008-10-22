package com.freshdirect.transadmin.service.impl;

import java.util.Collection;

import com.freshdirect.transadmin.dao.BaseManagerDaoI;
import com.freshdirect.transadmin.dao.DomainManagerDaoI;
import com.freshdirect.transadmin.model.TrnArea;
import com.freshdirect.transadmin.model.TrnCutOff;
import com.freshdirect.transadmin.model.TrnEmployee;
import com.freshdirect.transadmin.model.TrnRoute;
import com.freshdirect.transadmin.model.TrnTruck;
import com.freshdirect.transadmin.model.TrnZone;
import com.freshdirect.transadmin.model.TrnZoneType;
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
	
	public Collection getAreas() {
		
		return getDomainManagerDao().getAreas();
	}
	
	public Collection getMarkedAreas() {
		return getDomainManagerDao().getMarkedAreas();
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
	
	public TrnArea getArea(String id)  {
		return getDomainManagerDao().getArea(id);	
	}
	
	public Collection getZoneTypes() {
		return getDomainManagerDao().getZoneTypes();
	}
	
	
	public TrnZoneType getZoneType(String id) {
		return getDomainManagerDao().getZoneType(id);
	}
	
	public TrnCutOff getCutOff(String id) {
		return getDomainManagerDao().getCutOff(id);
	}
	
	public Collection getCutOffs() {
		return getDomainManagerDao().getCutOffs();
	}
	
	public Collection getRouteNumberGroup(String date, String area) {
		return getDomainManagerDao().getRouteNumberGroup(date, area);
	}
	
	public Collection getDeliveryModels() {
		return getDomainManagerDao().getDeliveryModels();
	}
	
}
