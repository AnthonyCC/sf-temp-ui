package com.freshdirect.transadmin.dao;

import java.util.Collection;
import java.util.Date;

import org.springframework.dao.DataAccessException;

import com.freshdirect.transadmin.model.Dispatch;
import com.freshdirect.transadmin.model.DlvScenarioDay;
import com.freshdirect.transadmin.model.Plan;
import com.freshdirect.transadmin.model.Scrib;

public interface DispatchManagerDaoI extends BaseManagerDaoI {

	Collection getPlan(String day, String zone, String date) throws DataAccessException;
	
	Collection getPlanList(String date) throws DataAccessException;
	
	Collection getPlanForResource(String date, String resourceId) throws DataAccessException;
	
	Collection getDispatchForResource(String date, String resourceId) throws DataAccessException;

	Dispatch getDispatch(String dispatchId) throws DataAccessException;
	
	Collection getDispatch(Date dispatchDate, Date startTime, boolean isBullPen) throws DataAccessException;

	Collection getPlan() throws DataAccessException;
	
	Collection getPlan(String dateRange, String zoneLst) throws DataAccessException;
	
	Collection getPlan(Date planDate, Date startTime, boolean isBullPen) throws DataAccessException;

	Plan getPlan(String id) throws DataAccessException;

	Collection getDrivers() throws DataAccessException;

	Collection getHelpers() throws DataAccessException;
	
	//Added for new dispatch model.
	Collection getDispatchList(String date, String zone, String region) throws DataAccessException;
	
	void saveDispatch(Dispatch dispatch) throws DataAccessException;
	
	Collection getAssignedTrucks(String date) throws DataAccessException;
	
	Collection getAssignedRoutes(String date) throws DataAccessException;

	void savePlan(Plan plan) throws DataAccessException;
	
	public void evictDispatch(Dispatch d)throws DataAccessException ;
	
	public Collection getScribList(String date);
	
	public Scrib getScrib(String id);

	public Collection getUserPref(String userId);
	
	public Collection getDispatchReasons(boolean active);
	
	int addScenarioDayMapping(DlvScenarioDay scenarioDay)throws DataAccessException ;
	
	void deleteDefaultScenarioDay(String sDate, String sDay);
}
