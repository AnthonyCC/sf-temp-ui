package com.freshdirect.transadmin.datamanager;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class RouteGenerationResult implements Serializable  {
	
	private List routeInfos;
	
	private Map routeNoSaveInfos;

	public List getRouteInfos() {
		return routeInfos;
	}

	public Map getRouteNoSaveInfos() {
		return routeNoSaveInfos;
	}

	public void setRouteNoSaveInfos(Map routeNoSaveInfos) {
		this.routeNoSaveInfos = routeNoSaveInfos;
	}

	public void setRouteInfos(List routeInfos) {
		this.routeInfos = routeInfos;
	}

	
}
