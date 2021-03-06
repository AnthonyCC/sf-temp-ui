package com.freshdirect.transadmin.dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.dao.DataAccessException;

import com.freshdirect.transadmin.model.CapacitySnapshot;
import com.freshdirect.transadmin.model.DispatchGroup;
import com.freshdirect.transadmin.model.DispositionType;
import com.freshdirect.transadmin.model.EmployeeRoleType;
import com.freshdirect.transadmin.model.EmployeeSubRoleType;
import com.freshdirect.transadmin.model.IssueSubType;
import com.freshdirect.transadmin.model.IssueType;
import com.freshdirect.transadmin.model.MaintenanceIssue;
import com.freshdirect.transadmin.model.Region;
import com.freshdirect.transadmin.model.SectorZipcode;
import com.freshdirect.transadmin.model.TrnAdHocRoute;
import com.freshdirect.transadmin.model.TrnArea;
import com.freshdirect.transadmin.model.TrnCutOff;
import com.freshdirect.transadmin.model.TrnRegion;
import com.freshdirect.transadmin.model.TrnZoneType;
import com.freshdirect.transadmin.model.VIRRecord;
import com.freshdirect.transadmin.model.Zone;

@SuppressWarnings("rawtypes")
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

	DispatchGroup getDispatchGroup(String id) throws DataAccessException;

	Collection getDispatchGroups() throws DataAccessException;

	void saveRouteNumberGroup(Map routeMapping) throws DataAccessException;

	Collection getRouteMapping(String routeDate, String routeId)
			throws DataAccessException;

	Collection getDeliveryModels() throws DataAccessException;

	Collection getZonetypeResources(String zoneTypeId)
			throws DataAccessException;

	Collection getRegions() throws DataAccessException;

	Collection getLightDutyRegions() throws DataAccessException;

	Region getRegion(String code) throws DataAccessException;

	Collection getEmployeeSupervisor(String empId) throws DataAccessException;

	Collection getEmployeeSupervisors() throws DataAccessException;

	Collection getEmployeeRoles() throws DataAccessException;

	Collection getEmployeeRoleTypes() throws DataAccessException;

	Collection getEmployeeSubRoleTypes() throws DataAccessException;

	EmployeeRoleType getEmployeeRoleType(String roleTypeId)
			throws DataAccessException;

	EmployeeSubRoleType getEmployeeSubRoleType(String subRoleTypeId)
			throws DataAccessException;

	Collection getEmployeeRole(String empId) throws DataAccessException;

	Collection getEmployeeStatus(String empId) throws DataAccessException;

	Map getEmployeeStatus() throws DataAccessException;

	Collection getTeamByEmployee(String empId) throws DataAccessException;

	Collection getTeamByLead(String leadId) throws DataAccessException;

	Collection getTeamMembersByEmployee(String empId)
			throws DataAccessException;

	Collection getTeamInfo() throws DataAccessException;

	Collection getEmployeesByRoleType(String roleTypeId)
			throws DataAccessException;

	Collection getDispositionTypes() throws DataAccessException;

	DispositionType getDispositionType(String dispCode)
			throws DataAccessException;

	void saveZoneType(TrnZoneType zoneType) throws DataAccessException;

	Collection getActiveZones() throws DataAccessException;

	Collection getRouteMappingByCutOff(String routeDate, String cutOff)
			throws DataAccessException;

	Collection getScheduleEmployee(String employeeId, String weekOf)
			throws DataAccessException;

	Collection getScheduleEmployee(String employeeId, String weekOf, String day)
			throws DataAccessException;

	Collection getScheduleEmployees(String weekOf, String day)
			throws DataAccessException;

	Collection getUPSRouteInfo(String routeDate);

	Collection getDeliveryGroups() throws DataAccessException;

	Collection getIssueTypes() throws DataAccessException;

	IssueType getIssueType(String name) throws DataAccessException;

	IssueType getIssueTypeById(String id) throws DataAccessException;

	IssueSubType getIssueSubType(String issueSubTypeName)
			throws DataAccessException;

	Collection getIssueSubTypes() throws DataAccessException;

	Collection getVIRRecords() throws DataAccessException;

	Collection getVIRRecords(Date createDate, String truckNumber)
			throws DataAccessException;

	VIRRecord getVIRRecord(String id) throws DataAccessException;
	
	Collection getVIRRecordByTruckNo(String truckNumber) throws DataAccessException;
	
	Collection getMaintenanceIssueByTruckNo(String truckNumber) throws DataAccessException;

	Collection getMaintenanceIssue(String truckNumber, String issueTypeId,
			String issueSubTypeId) throws DataAccessException;

	Collection getMaintenanceIssue(IssueType issueTypeId,
			IssueSubType issueSubTypeId) throws DataAccessException;

	MaintenanceIssue getMaintenanceIssue(String id) throws DataAccessException;

	Collection getMaintenanceIssues() throws DataAccessException;

	Collection getMaintenanceIssues(String issueStatus, String serviceStatus)
			throws DataAccessException;

	Collection getMaintenanceIssues(String serviceStatus)
			throws DataAccessException;

	void saveMaintenanceIssue(MaintenanceIssue model)
			throws DataAccessException;

	String saveVIRRecord(VIRRecord virRecord) throws DataAccessException;

	Collection getEmployeesTruckPreference() throws DataAccessException;

	Collection getEmployeeTruckPreference(String empId)
			throws DataAccessException;

	Collection getEmployeesByRoleTypeAndSubRoleType(String roleTypeId,
			String subRoleTypeId) throws DataAccessException;

	Map findByIDs(Set ids) throws DataAccessException;

	Map getEmpRolesByIds(Set empIds) throws DataAccessException;

	Map getEmployeeStatusByIds(Set empIds) throws DataAccessException;

	Map getEmployeeTruckPreferenceByIds(Set empIds) throws DataAccessException;

	Map getTeamByEmployees(Set empIds) throws DataAccessException;

	Collection getSector() throws DataAccessException;

	Collection getActiveSector() throws DataAccessException;

	SectorZipcode getSectorZipCode(String zipCode);

	Collection getDispatchResource(Date dispatchDate, String dispatchType)
			throws DataAccessException;

	boolean saveToSnapshot(List<CapacitySnapshot> snapshots);

	Collection getSnapshotLocations();

	Object getSnapshotLocation(String buildingId, String serviceType);

	Collection getRoutingRegions();

	TrnRegion getRoutingRegion(String id) throws DataAccessException;
}
