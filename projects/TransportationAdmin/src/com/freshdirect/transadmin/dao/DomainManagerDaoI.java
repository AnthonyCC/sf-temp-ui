package com.freshdirect.transadmin.dao;

import java.util.Collection;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.freshdirect.transadmin.model.DispositionType;
import com.freshdirect.transadmin.model.EmployeeRoleType;
import com.freshdirect.transadmin.model.EmployeeSubRoleType;
import com.freshdirect.transadmin.model.Region;
import com.freshdirect.transadmin.model.TrnAdHocRoute;
import com.freshdirect.transadmin.model.TrnArea;
import com.freshdirect.transadmin.model.TrnCutOff;
import com.freshdirect.transadmin.model.TrnZoneType;
import com.freshdirect.transadmin.model.Zone;

public interface DomainManagerDaoI extends BaseManagerDaoI {
	
	Collection getZones() throws DataAccessException;

	Collection getAdHocRoutes() throws DataAccessException;
	
	String[] getDays() throws DataAccessException;

	String[] getTimings() throws DataAccessException;

	Zone getZone(String id) throws DataAccessException;

	TrnAdHocRoute getAdHocRoute(String id) throws DataAccessException;

	Collection getTimeSlots() throws DataAccessException;		
	
	TrnZoneType getZoneType(String id) throws DataAccessException;
	
	Collection getZoneTypes() throws DataAccessException;
	
	Collection getMarkedAreas() throws DataAccessException;
	
	TrnArea getArea(String id) throws DataAccessException;
	
	Collection getAreas() throws DataAccessException;
	
	TrnCutOff getCutOff(String id) throws DataAccessException;
	
	Collection getCutOffs() throws DataAccessException;
	
	//Map getRouteNumberGroup(String date, String cutOff, String groupCode) throws DataAccessException;
	
	void saveRouteNumberGroup(Map routeMapping) throws DataAccessException;
	
	Collection getRouteMapping(String routeDate, String routeId) throws DataAccessException;
	
	Collection getDeliveryModels() throws DataAccessException;
	
	Collection getZonetypeResources(String zoneTypeId) throws DataAccessException;

	Collection getRegions()  throws DataAccessException;

	Region getRegion(String code) throws DataAccessException;
	
    Collection getEmployeeRoles() throws DataAccessException;
	
	Collection getEmployeeRoleTypes() throws DataAccessException;
	
	Collection getEmployeeSubRoleTypes() throws DataAccessException;
	
	EmployeeRoleType getEmployeeRoleType(String roleTypeId)  throws DataAccessException;
	
	EmployeeSubRoleType getEmployeeSubRoleType(String subRoleTypeId) throws DataAccessException;
	
	Collection getEmployeeRole(String empId)  throws DataAccessException;
	
	Collection getEmployeeStatus(String empId)  throws DataAccessException;
	
	Collection getTeamByEmployee(String empId)  throws DataAccessException;
	
	Collection getTeamByLead(String leadId) throws DataAccessException;
	
	Collection getTeamMembersByEmployee(String empId)  throws DataAccessException;
	
	Collection getTeamInfo()  throws DataAccessException;
	
	Collection getEmployeesByRoleType(String roleTypeId) throws DataAccessException;

	Collection getDispositionTypes() throws DataAccessException;
	
	DispositionType getDispositionType(String dispCode) throws DataAccessException;
	
	void saveZoneType(TrnZoneType zoneType) throws DataAccessException;

	Collection getActiveZones() throws DataAccessException;
	
	Collection getRouteMappingByCutOff(String routeDate, String cutOff) throws DataAccessException;
	Collection getScheduleEmployee(String employeeId, String weekOf) throws DataAccessException;
	Collection getScheduleEmployee(String employeeId, String weekOf, String day) throws DataAccessException;
	Collection getScheduleEmployees(String weekOf, String day) throws DataAccessException;
	Collection getUPSRouteInfo(String routeDate);
		
}
