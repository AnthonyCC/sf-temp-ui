package com.freshdirect.transadmin.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.routing.constants.EnumWaveInstancePublishSrc;
import com.freshdirect.transadmin.exception.TransAdminApplicationException;
import com.freshdirect.transadmin.model.Dispatch;
import com.freshdirect.transadmin.model.DlvScenarioDay;
import com.freshdirect.transadmin.model.Plan;
import com.freshdirect.transadmin.model.Scrib;
import com.freshdirect.transadmin.model.ScribLabel;
import com.freshdirect.transadmin.model.UPSRouteInfo;
import com.freshdirect.transadmin.model.WaveInstance;

public interface DispatchManagerI extends BaseManagerI {
	
	Collection getPlan(String day, String zone, String date);
	
	Collection<Plan> getPlanList(String date);
	
	Collection getPlanList(String date, String region);
	
	Collection getPlanForResource(String date, String resourceId);
	
	Collection getDispatchForResource(String date, String resourceId);
	
	Dispatch getDispatch(String dispatchId);
	
	Collection getPlanEntry(String dateRange, String zoneLst, String facilityLocation);
	
	Collection getPlan();
	
	Plan getPlan(String id);
	
	void copyPlan(Collection addPlanList, Collection removePlanList);
	
	void autoDisptch(String date);

	void processAutoDispatch(String date);
	//Based on new Dispatch Model
	Collection getDispatchList(String date, String facilityLocation, String zone, String region);
	
	List refreshRoute(Date requestedDate);
	
	void saveDispatch(Dispatch dispatch) throws TransAdminApplicationException;
	
	void saveDispatch(Dispatch dispatch, String referenceContextId) throws TransAdminApplicationException;
	
	Collection getAssignedTrucks(String date);
	
	Collection getAssignedRoutes(String date);
	
	Collection getDispatchTrucks(String date);
	
	Collection getAvailableTrucks(String date);
	
	void savePlan(Plan plan);
	
	void savePlan(Plan plan, String referencePlanId);
	
	Collection getUnusedDispatchRoutes(String dispatchDate);
	
	Map getRouteNumberGroup(String date, String cutOff, String groupCode);
	
	int updateRouteMapping(Date routeDate, String cutOffId, String sessionId, boolean isDepot);
	
	Collection getUnassignedActiveEmployees();
	
	void evictDispatch(Dispatch dispatch);
	
	Map getHTOutScan(Date routeDate);
	
	List getHTOutScanAsset(Date routeDate);
	
	List getResourcesWorkedForSixConsecutiveDays(Date date);
	
	List getDispatchTeamResourcesChanged(Date date, String type, String field);
	
	Map getHTInScan(Date routeDate);
	
	List matchCommunity(double latitiude, double longitude, String deliveryModel);
	
	public Collection getScribList(String date);
	
	public Collection getScribList(String date, String region);
	
	public Scrib getScrib(String id);
	
	public Collection getUserPref(String userId);
	
	public Collection getDispatchReasons(boolean active);
	
	int addScenarioDayMapping(DlvScenarioDay scenarioDay);
	
	void deleteDefaultScenarioDay(String sDate, String sDay);
	
	ScribLabel getScribLabelByDate(String date);
	
	Collection getDatesByScribLabel(String slabel);
	
	Collection getDispatchForGPS(Date dispatchDate, String assetId);
	
	Collection getDispatchForEZPass(Date dispatchDate, String assetId);
	
	Collection getDispatchForMotKit(Date dispatchDate, String assetId);
	
	Collection getDispatchForRoute(Date dispatchDate, String routeNo);

	Collection getPlan(Date planDate, String zone);
	Collection getScrib(Date scribDate, String zone);	
	Collection getWaveInstance(Date deliveryDate, String area);
	Collection getWaveInstance(Date deliveryDate, Date cutOff);
	Collection getWaveInstancePublish(Date deliveryDate);	
	Collection getWaveInstance(Date cutOff);
	WaveInstance getWaveInstance(String waveInstanceId);
		
	void publishWaveInstance(Date deliveryDate, String actionBy, EnumWaveInstancePublishSrc source);
	
	void uploadScrib(Set<Date> scribDates, List<Scrib> toSaveScribs);
		
	void saveWaveInstances(List<WaveInstance> saveWaveInstances, List<WaveInstance> deleteWaveInstances);

	List<UPSRouteInfo> getUPSRouteInfo(Date deliveryDate);
	
	Collection getTrnFacilitys();
}
