package com.freshdirect.transadmin.datamanager;

import java.io.Serializable;
import java.util.List;

public class RouteGenerationResult implements Serializable  {
	
	private List routeInfos;
	
	private List routeNoSaveInfos;

	public List getRouteInfos() {
		return routeInfos;
	}

	public void setRouteInfos(List routeInfos) {
		this.routeInfos = routeInfos;
	}

	public List getRouteNoSaveInfos() {
		return routeNoSaveInfos;
	}

	public void setRouteNoSaveInfos(List routeNoSaveInfos) {
		this.routeNoSaveInfos = routeNoSaveInfos;
	}
}
