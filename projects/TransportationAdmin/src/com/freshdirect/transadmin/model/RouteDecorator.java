package com.freshdirect.transadmin.model;

import com.freshdirect.customer.ErpRouteMasterInfo;

public class RouteDecorator 
{
	private ErpRouteMasterInfo route;
	private Zone zone;
	
	public RouteDecorator(ErpRouteMasterInfo route,Zone zone)
	{
		this.route=route;
		this.zone=zone;
	}

	public ErpRouteMasterInfo getRoute() {
		return route;
	}

	public void setRoute(ErpRouteMasterInfo route) {
		this.route = route;
	}

	public Zone getZone() {
		return zone;
	}

	public void setZone(Zone zone) {
		this.zone = zone;
	}
	
	
}
