package com.freshdirect.transadmin.dao;

import java.util.Collection;
import java.util.Date;

import org.springframework.dao.DataAccessException;

import com.freshdirect.transadmin.model.Dispatch;
import com.freshdirect.transadmin.model.DlvScenarioDay;
import com.freshdirect.transadmin.model.Plan;
import com.freshdirect.transadmin.model.Scrib;
import com.freshdirect.transadmin.model.ScribLabel;
import com.freshdirect.transadmin.model.WaveInstance;

public interface DispatchManagerDaoI extends BaseManagerDaoI {

	Collection getPlan(String day, String zone, String date) throws DataAccessException;
	
	Collection<Plan> getPlanList(String date) throws DataAccessException;
	
	Collection getPlanList(String date, String region) throws DataAccessException;
	
	Collection getPlanForResource(String date, String resourceId) throws DataAccessException;
	
	Collection getDispatchForResource(String date, String resourceId) throws DataAccessException;

	Dispatch getDispatch(String dispatchId) throws DataAccessException;
	
	Collection getDispatch(Date dispatchDate, Date startTime, boolean isBullPen) throws DataAccessException;

	Collection getPlan() throws DataAccessException;
	
	Collection getPlan(String dateRange, String zoneLst) throws DataAccessException;
	
	Collection getPlan(Date planDate, Date startTime, boolean isBullPen) throws DataAccessException;

	Plan getPlan(String id) throws DataAccessException;

	//Added for new dispatch model.
	Collection getDispatchList(String date, String facilityLocation, String zone, String region) throws DataAccessException;
	
	void saveDispatch(Dispatch dispatch) throws DataAccessException;
	
	Collection getAssignedTrucks(String date) throws DataAccessException;
	
	Collection getAssignedRoutes(String date) throws DataAccessException;

	void savePlan(Plan plan) throws DataAccessException;
	
	void evictDispatch(Dispatch d)throws DataAccessException ;
		
	Collection getScribList(String date);
	
	Collection getScribList(String date, String region);
		
	Scrib getScrib(String id);

	Collection getUserPref(String userId);
	
	Collection getDispatchReasons(boolean active);
	
	int addScenarioDayMapping(DlvScenarioDay scenarioDay)throws DataAccessException ;
	
	void deleteDefaultScenarioDay(String sDate, String sDay);
	
	ScribLabel getScribLabelByDate(String date);
	
	Collection getDatesByScribLabel(String slabel);
	
	Collection getDispatchForGPS(Date dispatchDate, String assetId) throws DataAccessException;
	
	Collection getDispatchForEZPass(Date dispatchDate, String assetId) throws DataAccessException;
	
	Collection getDispatchForMotKit(Date dispatchDate, String assetId) throws DataAccessException;
	
	Collection getDispatchForRoute(Date dispatchDate, String routeNo) throws DataAccessException;
	
	Collection getPlan(Date planDate, String zone) throws DataAccessException;
	
	Collection getScrib(Date scribDate, String zone) throws DataAccessException;
	
	Collection getWaveInstance(Date deliveryDate, String area) throws DataAccessException;
	
	Collection getWaveInstance(Date deliveryDate, Date cutOff) throws DataAccessException;
	
	Collection getWaveInstance(Date cutOff) throws DataAccessException;
	
	WaveInstance getWaveInstance(String waveInstanceId) throws DataAccessException;
	
	Collection getWaveInstancePublish(Date deliveryDate) throws DataAccessException;
	
	void deleteWaveInstance(Date deliveryDate) throws DataAccessException;
}
