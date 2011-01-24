package com.freshdirect.transadmin.web.json;

import java.util.Collection;
import java.util.List;

import com.freshdirect.transadmin.web.model.DispatchCommand;
import com.freshdirect.transadmin.web.model.WavePublishValidationResult;
import com.freshdirect.transadmin.web.model.WebPlanInfo;

public interface IDispatchProvider {
	
	int updateRouteMapping(String routeDate, String cutOffId, String sessionId, boolean isDepot);
	Collection getActiveRoute(String date,String zone);
	Collection getActivityLog(String date);
	String generateCommunityReport(String routeDate, String cutOff);
	int updateUserPref(String key,String value);
	String getUserPref(String key);
	Collection getActiveZones(String date);
	Collection getReasonCode(boolean active);
	int addReasonCode(String reason);
	int setReasonCode(String code,String reason, boolean enable);
	List<WebPlanInfo> getPlanForResource(String date, String resourceId, String planId);
	List<DispatchCommand> getDispatchForResource(String date, String resourceId, String dispatchId);
	int addScenarioDayMapping(String sCode, String sDay, String sDate);
	boolean deleteServiceTimeScenario(String sCode);
	Collection getScenarioZones(String scenarioId);
	boolean doScenarioZone(String id, String[][] zone);
	boolean addScribLabel(String date,String label,String checked);
	String getScribLabel(String date);
	Collection getDatesByScribLabel(String slabel);
	String getTotalNoTrucksByDate(String date);
	Collection getDefaultZoneSupervisors(String zoneId);
	boolean doZoneDefaultSupervisor(String id, String[][] zone);
	
	boolean hasDispatchForGPS(String dispatchDate, String dispatchId, String firstDeliveryTime, String assetId);
	boolean hasDispatchForEZPass(String dispatchDate, String dispatchId, String firstDeliveryTime, String assetId);
	boolean hasDispatchForMotKit(String dispatchDate, String dispatchId, String firstDeliveryTime, String assetId);
	
	boolean publishWave(String weekOf, String dayOfWeek);
	boolean publishWave(String deliveryDate);
	WavePublishValidationResult canPublishWave(String deliveryDate);
	WavePublishValidationResult canPublishWave(String weekOf, String dayOfWeek);
	
	int validatePlanGenAccessCode(String accessCode);
}