package com.freshdirect.transadmin.service;

import java.util.Collection;

import com.freshdirect.transadmin.model.TrnArea;
import com.freshdirect.transadmin.model.TrnCutOff;
import com.freshdirect.transadmin.model.TrnEmployee;
import com.freshdirect.transadmin.model.TrnRoute;
import com.freshdirect.transadmin.model.TrnTruck;
import com.freshdirect.transadmin.model.TrnZone;
import com.freshdirect.transadmin.model.TrnZoneType;

public interface DomainManagerI extends BaseManagerI {
	
	Collection getEmployees();
	
	Collection getZones();
	
	Collection getAreas();
	
	Collection getMarkedAreas();
	
	Collection getRoutes();
	
	Collection getRouteForZone(String zoneId);
	
	Collection getTrucks();
	
	Collection getZoneTypes();
	
	String[] getDays();
	
	String[] getTimings();
	
	String[] getTruckTypes();
	
	Collection getEmployeeJobType();
	
	Collection getSupervisors();
		
	TrnEmployee getEmployee(String id);
	
	TrnZone getZone(String id);
	
	TrnRoute getRoute(String id);
	
	TrnTruck getTruck(String id);
	
	TrnZoneType getZoneType(String id);
	
	Collection getTimeSlots();	
	
	TrnArea getArea(String id);
	
	TrnCutOff getCutOff(String id);
	
	Collection getCutOffs();
	
	Collection getRouteNumberGroup(String date, String cutOff, String area);
	
	Collection getDeliveryModels();

}
