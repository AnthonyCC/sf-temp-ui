package com.freshdirect.transadmin.web.json;

import java.util.Collection;

public interface IDispatchProvider {
	
	int updateRouteMapping(String routeDate, String cutOffId, String sessionId, boolean isDepot);
	Collection getActiveRoute(String date,String zone);
	Collection getActivityLog(String date);
	String generateCommunityReport(String routeDate, String cutOff);
	public int updateUserPref(String key,String value);
	public String getUserPref(String key);
	public Collection getActiveZones(String date);
	public Collection getReasonCode(boolean active);
	public int addReasonCode(String reason);
	public int setReasonCode(String code,String reason, boolean enable);
}
