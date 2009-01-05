package com.freshdirect.transadmin.model;

import java.io.Serializable;
import java.util.*;

import com.freshdirect.customer.ErpRouteMasterInfo;

public class FDRouteMasterInfo implements Serializable {
	
	public FDRouteMasterInfo(ErpRouteMasterInfo info) {
		this.routeInfo = info;
		
	}


	
	public String toString() {
		return "FDRouteMasterInfo[routeNumber: "
			+ getRouteNumber()
			+ " zoneNumber: "
			+ getZoneNumber()
			+ " routeTime: "
			+ getRouteTime()
			+ " numberOfStops: "
			+ getNumberOfStops()
			+ " firstDeliveryTime: "
			+ getFirstDlvTime().toString();
	}

	private ErpRouteMasterInfo routeInfo;
	
	public String getNumberOfStops() {
		return routeInfo.getNumberOfStops();
	}

	public String getRouteNumber() {
		return routeInfo.getRouteNumber();
	}

	public String getRouteTime() {
		return routeInfo.getRouteTime();
	}

	public String getZoneNumber() {
		return routeInfo.getZoneNumber();
	}

	public String getTruckNumber() {
		return routeInfo.getTruckNumber();
	}

	public String getFirstDlvTime() {
		return routeInfo.getFirstDlvTime();
	}
}
