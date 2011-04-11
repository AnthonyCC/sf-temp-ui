package com.freshdirect.transadmin.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.freshdirect.customer.ErpTruckMasterInfo;
import com.freshdirect.transadmin.model.DispositionType;
import com.freshdirect.transadmin.model.FDRouteMasterInfo;
import com.freshdirect.transadmin.model.IssueSubType;
import com.freshdirect.transadmin.model.IssueType;
import com.freshdirect.transadmin.model.MaintenanceIssue;
import com.freshdirect.transadmin.model.Region;
import com.freshdirect.transadmin.model.ScheduleEmployee;
import com.freshdirect.transadmin.model.TrnAdHocRoute;
import com.freshdirect.transadmin.model.TrnArea;
import com.freshdirect.transadmin.model.TrnCutOff;
import com.freshdirect.transadmin.model.TrnZoneType;
import com.freshdirect.transadmin.model.VIRRecord;
import com.freshdirect.transadmin.model.Zone;
import com.freshdirect.transadmin.util.EnumCachedDataType;
import com.freshdirect.transadmin.web.model.WebSchedule;

public interface DomainManagerI extends BaseManagerI {
	
	Collection getRegions();	
	
	Collection getZones();
	
	Collection getAreas();
	
	Collection getMarkedAreas();
	
	Collection getAdHocRoutes();
	
	Collection getRoutes(String requestedDate);
	
	Collection getTrucks();
	
	ErpTruckMasterInfo getERPTruck(String truckId);
	
	Collection getZoneTypes();
	
	String[] getDays();
	
	String[] getTimings();
	
	Collection getEmployeeJobType();
	
	Zone getZone(String id);
	
	TrnAdHocRoute getAdHocRoute(String id);
	
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
	
	void refreshProd(String worktable);
	
	void doZoneExpansion(String worktable, String zoneCode[][], String regionId, String deliveryFee, String expansionType);
	
	String getDeliveryCharge(String regionId);
	
	Collection getCommonList(String worktable, String regionId);
	
	void rollbackTimeslots(String zone[][]);
	
	List getStartDateForRegion(String regionId);
	
	void updateStartDate(String regionId);
	
	void makeDevLive(String regionId);
	
	Collection getGeoRestrictions();
	
	void refreshGeoRestrictionWorktable();
	
	void doGeoRestriction(String zone[][]);	
			
	void saveScheduleGroup(Collection schedules, String[] employeeIds, Date weekOf);
	
	void saveOrUpdateEmployeeSchedule(List<ScheduleEmployee> scheduleEmp);
	
	void copyScheduleGroup(String[] employeeIds, Date sourceWeekOf, Date destinationWeekOf, String day);
	
	Collection getScheduleEmployee(String employeeId, String weekOf, String day) throws DataAccessException;
	
	Collection getScheduleEmployee(String employeeId, String weekOf) throws DataAccessException;
	
	Collection getTeamInfo();
	
	Collection getDeliveryGroups();
	
	Collection getIssueTypes();
	
	IssueType getIssueType(String name);
	
	IssueType getIssueTypeById(String id);
	
	Collection getIssueSubTypes();
	
	Collection getVIRRecords();
	
	Collection getVIRRecords(String createDate, String enteredBy,String truckNumber);
	
	VIRRecord getVIRRecord(String id);
	
	Collection getMaintenanceIssue(String truckNumber, IssueType issueType, IssueSubType issueSubType);
	
	Collection getMaintenanceIssue(IssueType issueType, IssueSubType issueSubType);
	
	MaintenanceIssue getMaintenanceIssue(String id);
	
	Collection getMaintenanceIssues();
	
	Collection getMaintenanceIssues(String issueStatus, String serviceStatus);
	
	void saveMaintenanceIssue(MaintenanceIssue model);
	
	void updateDisassociatedTimeslots();
}
