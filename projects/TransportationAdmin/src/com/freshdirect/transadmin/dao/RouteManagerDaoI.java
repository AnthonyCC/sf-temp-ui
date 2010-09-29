package com.freshdirect.transadmin.dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

public interface RouteManagerDaoI {
	
	Map getRouteNumberGroup(String date, String cutOff, String groupCode) throws DataAccessException;
	
	int updateRouteMapping(Date routeDate, String cutOffId, String sessionId, boolean isDepot) throws DataAccessException;
	
	Map getHTOutScan(Date routeDate) throws DataAccessException;
	
	List getHTOutScanAsset(Date routeDate) throws DataAccessException;
	
	List getResourcesWorkedForSixConsecutiveDays(Date date) throws DataAccessException;
	
	Date getHTOutScanTimeForRoute(String routeId) throws DataAccessException;
	
	Map getHTInScan(Date routeDate) throws DataAccessException;
	
	List getDispatchTeamResourcesChanged(Date date, String type, String field) throws DataAccessException;
}
