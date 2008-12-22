package com.freshdirect.transadmin.service;

import java.util.Collection;
import java.util.Date;

import com.freshdirect.transadmin.model.DispositionType;
import com.freshdirect.transadmin.model.EmployeeRoleType;
import com.freshdirect.transadmin.model.FDRouteMasterInfo;
import com.freshdirect.transadmin.model.Region;
import com.freshdirect.transadmin.model.TrnAdHocRoute;
import com.freshdirect.transadmin.model.TrnArea;
import com.freshdirect.transadmin.model.TrnCutOff;
import com.freshdirect.transadmin.model.TrnEmployee;
import com.freshdirect.transadmin.model.TrnRoute;
import com.freshdirect.transadmin.model.TrnTruck;
import com.freshdirect.transadmin.model.Zone;
import com.freshdirect.transadmin.model.TrnZoneType;
import com.freshdirect.transadmin.util.EnumCachedDataType;

public interface DomainManagerI extends BaseManagerI {
	
	Collection getRegions();
	
	
	
	Collection getZones();
	
	Collection getAreas();
	
	Collection getMarkedAreas();
	
	Collection getAdHocRoutes();
	
	Collection getRoutes(String requestedDate);
	
	Collection getRouteForZone(String zoneId);
	
	Collection getTrucks();
	
	Collection getZoneTypes();
	
	
	String[] getDays();
	
	String[] getTimings();
	
	String[] getTruckTypes();
	
	Collection getEmployeeJobType();
	
	Collection getSupervisors();
		
	
	
	Zone getZone(String id);
	
	TrnAdHocRoute getAdHocRoute(String id);
	
	TrnTruck getTruck(String id);
	
	TrnZoneType getZoneType(String id);
	
	Collection getTimeSlots();	
	
	TrnArea getArea(String id);
	
	Region getRegion(String id);
	
	TrnCutOff getCutOff(String id);
	
	Collection getCutOffs();
	
	Collection getRouteNumberGroup(String date, String cutOff, String area);
	
	Collection getDeliveryModels();
	
	
	Collection getZonetypeResources(String zoneTypeId);
	
	void refreshCachedData(EnumCachedDataType dataType);
	
	Collection getDispositionTypes();
	
	DispositionType getDispositionType(String code);
	
	void saveZoneType(TrnZoneType zoneType);
	
	FDRouteMasterInfo getRouteMasterInfo(String routeNo, Date requestedDate);
	
	Collection getAllRoutes(String requestedDate);
	
	Collection getTruckNumbers();
	
	Collection getBalanceBys();
	
}
