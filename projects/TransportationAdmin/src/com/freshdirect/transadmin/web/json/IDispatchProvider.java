package com.freshdirect.transadmin.web.json;

import java.util.Collection;

public interface IDispatchProvider {
	
	int updateRouteMapping(String routeDate, String cutOffId, String sessionId, boolean isDepot);
	public Collection getActiveRoute(String date,String zone);
	public Collection getActivityLog(String date);
	public int updateUserPref(String key,String value);
	public String getUserPref(String key);

}
