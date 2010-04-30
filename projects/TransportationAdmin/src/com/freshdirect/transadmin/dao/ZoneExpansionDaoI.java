package com.freshdirect.transadmin.dao;

import java.util.Collection;

import org.springframework.dao.DataAccessException;

public interface ZoneExpansionDaoI {
	
	Collection checkPolygons() throws DataAccessException;
	
	Collection getZoneWorkTableInfo(String worktable) throws DataAccessException;
	
	Collection getZoneRegionInfo(String regionId) throws DataAccessException;
	
	void refreshDev(String worktable) throws DataAccessException;
	
	void insertNewRegionDataId(String regionId, String dlvCharge) throws DataAccessException;
	
	String getDeliveryCharge(String regionId) throws DataAccessException;
	
	void insertCommonZoneSelected(String worktable, String regionId, String zoneCode) throws DataAccessException;
	
	void insertNewZone(String worktable, String regionId, String zoneCode) throws DataAccessException;
	
	void insertUncheckedZones(String zoneCode, String regionId)throws DataAccessException;
	
	void deleteFromZoneDesc(String zoneCode)throws DataAccessException;
	
	void insertIntoZoneDesc(String zoneCode)throws DataAccessException;
	
	void deleteFromTranspZone(String zoneCode)throws DataAccessException;
	
	void insertIntoTranspZone(String zoneCode, String worktable)throws DataAccessException;
	
	void doExpansion(String worktable, String zoneCode);
	
	void deleteTimeslot(String zoneCode) throws DataAccessException;
	
	void deleteTrunkResource(String zoneCode) throws DataAccessException;
	
	void deletePlanningResource(String zoneCode) throws DataAccessException;
	
	void updateTimeslot(String zoneCode) throws DataAccessException;
	
	void updatePlanningResource(String zoneCode) throws DataAccessException;

}
