package com.freshdirect.transadmin.web.json;

import java.util.Collection;
import java.util.List;

import com.freshdirect.transadmin.web.model.DispatchCommand;
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
	WebPlanInfo getPlanForResource(String date, String resourceId, String planId);
	DispatchCommand getDispatchForResource(String date, String resourceId, String dispatchId);
	int addScenarioDayMapping(String sCode, String sDay, String sDate);
	boolean deleteServiceTimeScenario(String sCode);
	Collection getScenarioZones(String scenarioId);
	boolean doScenarioZone(String id, String[][] zone);
	boolean addScribLabel(String date,String label);	
	String getScribLabel(String date);
}
