package com.freshdirect.transadmin.model;

import java.io.Serializable;
import java.util.*;

import com.freshdirect.customer.ErpRouteMasterInfo;

public class RouteInfo implements Serializable {
	private String routeNumber;
	private boolean adHoc;
	private String zoneNumber;
	
	public String getRouteNumber() {
		return routeNumber;
	}
	public void setRouteNumber(String routeNumber) {
		this.routeNumber = routeNumber;
	}
	public boolean isAdHoc() {
		return adHoc;
	}
	public void setAdHoc(boolean isAdHoc) {
		this.adHoc = isAdHoc;
	}
	public String getZoneNumber() {
		return zoneNumber;
	}
	public void setZoneNumber(String zoneNumber) {
		this.zoneNumber = zoneNumber;
	}

	public String getZonePrefix() {
		if(routeNumber != null && routeNumber.length() > 3){
			return routeNumber.substring(1,4);	
		}
		return "";
		
	}

}
