package com.freshdirect.transadmin.dao;

import java.util.Date;
import java.util.Map;

import org.springframework.dao.DataAccessException;

public interface RouteManagerDaoI {
	
	Map getRouteNumberGroup(String date, String cutOff, String groupCode) throws DataAccessException;
	
	int updateRouteMapping(Date routeDate, String cutOffId, String sessionId, boolean isDepot) throws DataAccessException;
	
	Map getHTOutScan(Date routeDate) throws DataAccessException;
	
	Date getHTOutScanTimeForRoute(String routeId) throws DataAccessException;
	
	Map getHTInScan(Date routeDate) throws DataAccessException;
}
