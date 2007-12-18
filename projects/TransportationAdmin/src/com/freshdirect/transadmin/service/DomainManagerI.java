package com.freshdirect.transadmin.service;

import java.util.Collection;

import com.freshdirect.transadmin.model.TrnEmployee;
import com.freshdirect.transadmin.model.TrnRoute;
import com.freshdirect.transadmin.model.TrnTruck;
import com.freshdirect.transadmin.model.TrnZone;

public interface DomainManagerI extends BaseManagerI {
	
	Collection getEmployees();
	
	Collection getZones();
	
	Collection getRoutes();
	
	Collection getRouteForZone(String zoneId);
	
	Collection getTrucks();
	
	String[] getDays();
	
	String[] getTimings();
	
	String[] getTruckTypes();
	
	Collection getEmployeeJobType();
	
	Collection getSupervisors();
		
	TrnEmployee getEmployee(String id);
	
	TrnZone getZone(String id);
	
	TrnRoute getRoute(String id);
	
	TrnTruck getTruck(String id);
	
	Collection getTimeSlots();

}
