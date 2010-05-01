package com.freshdirect.transadmin.service;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

import com.freshdirect.customer.ErpTruckMasterInfo;
import com.freshdirect.transadmin.model.DispositionType;
import com.freshdirect.transadmin.model.FDRouteMasterInfo;
import com.freshdirect.transadmin.model.Region;
import com.freshdirect.transadmin.model.TrnAdHocRoute;
import com.freshdirect.transadmin.model.TrnArea;
import com.freshdirect.transadmin.model.TrnCutOff;
import com.freshdirect.transadmin.model.TrnTruck;
import com.freshdirect.transadmin.model.TrnZoneType;
import com.freshdirect.transadmin.model.Zone;
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
	
	ErpTruckMasterInfo getERPTruck(String truckId);
	
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

	Collection getActiveZones();
	
	void saveRouteNumberGroup(Map routeMapping);	
	
	Collection getRouteMapping(String routeDate, String routeId);
	
	Collection getRouteMappingByCutOff(String routeDate, String cutOff);
	
	Collection getUPSRouteInfo(String routeDate);
	
	Collection getEmployeeRole(String empId);
	
	Collection getZoneWorkTableInfo(String worktable, String regionId);
	
	Collection checkPolygons();
	
	void refreshDev(String worktable);
	
	void doZoneExpansion(String worktable, String zoneCode[][], String regionId, String deliveryFee, String expansionType);
	
	String getDeliveryCharge(String regionId);
	
	Collection getCommonList(String worktable, String regionId);
	
	void rollbackTimeslots(String zone[][]);
}
