package com.freshdirect.transadmin.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.freshdirect.transadmin.exception.TransAdminApplicationException;
import com.freshdirect.transadmin.model.Dispatch;
import com.freshdirect.transadmin.model.Plan;
import com.freshdirect.transadmin.model.Scrib;

public interface DispatchManagerI extends BaseManagerI {
	
	Collection getPlan(String day, String zone, String date);
	
	Collection getPlanList(String date);
	
	Dispatch getDispatch(String dispatchId);
	
	Collection getPlan(String dateRange, String zoneLst);
	
	Collection getPlan();
	
	Plan getPlan(String id);
	
	Collection getDrivers();
	
	Collection getHelpers();
	
	void copyPlan(Collection addPlanList, Collection removePlanList);
	
	void autoDisptch(String date);
	void autoDisptchRegion(String date);
	//Based on new Dispatch Model
	Collection getDispatchList (String date, String zone, String region);
	
	boolean refreshRoute(Date requestedDate);
	
	void saveDispatch(Dispatch dispatch) throws TransAdminApplicationException;
	
	Collection getAssignedTrucks(String date);
	
	Collection getAssignedRoutes(String date);
	
	Collection getDispatchTrucks(String date);
	
	Collection getAvailableTrucks(String date);
	
	void savePlan(Plan plan);
	
	Collection getUnusedDispatchRoutes(String dispatchDate);
	
	Map getRouteNumberGroup(String date, String cutOff, String groupCode);
	
	int updateRouteMapping(Date routeDate, String cutOffId, String sessionId, boolean isDepot);
	
	Collection getUnassignedActiveEmployees();
	
	void evictDispatch(Dispatch dispatch);
	
	Map getHTOutScan(Date routeDate);
	
	Map getHTInScan(Date routeDate);
	
	List matchCommunity(double latitiude, double longitude, String deliveryModel);
	
	public Collection getScribList(String date);
	
	public Scrib getScrib(String id);
	
	public Collection getUserPref(String userId);
	
}
