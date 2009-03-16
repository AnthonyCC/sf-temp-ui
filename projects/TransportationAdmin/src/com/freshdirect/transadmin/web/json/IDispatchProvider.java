package com.freshdirect.transadmin.web.json;

public interface IDispatchProvider {
	
	int updateRouteMapping(String routeDate, String cutOffId, String sessionId, boolean isDepot);
}
