package com.freshdirect.transadmin.dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.freshdirect.transadmin.model.UPSRouteInfo;

public interface RouteManagerDaoI {
	
	Map getRouteNumberGroup(String date, String cutOff, String groupCode) throws DataAccessException;
	
	int updateRouteMapping(Date routeDate, String cutOffId, String sessionId, boolean isDepot) throws DataAccessException;
	
	Map getHTOutScan(Date routeDate) throws DataAccessException;
	
	List getHTOutScanAsset(Date routeDate) throws DataAccessException;
	
	List getResourcesWorkedForSixConsecutiveDays(Date date) throws DataAccessException;
	
	Date getHTOutScanTimeForRoute(String routeId) throws DataAccessException;
	
	Map getHTInScan(Date routeDate) throws DataAccessException;
	
	List getDispatchTeamResourcesChanged(Date date, String type, String field) throws DataAccessException;

	List<UPSRouteInfo> getUPSRouteInfo(final Date deliveryDate) throws DataAccessException;
	
	void updateTruckInfo(List dispatchList) throws SQLException;
	
	public int updateOrderUnassignedInfo(String orderNo) throws DataAccessException;
}
