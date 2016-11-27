package com.freshdirect.transadmin.model;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;


@SuppressWarnings("serial")
public class EventLogRouteModel implements Serializable, Comparator<EventLogRouteModel>  {
	
	private String routeNo;
	
	private Set<String> stops = new HashSet<String>();
	
	private Set<String> windows = new HashSet<String>();

	public String getRouteNo() {
		return routeNo;
	}

	public void setRouteNo(String routeNo) {
		this.routeNo = routeNo;
	}

	public Set<String> getStops() {
		return stops;
	}

	public void setStops(Set<String> stops) {
		this.stops = stops;
	}

	public Set<String> getWindows() {
		return windows;
	}

	public void setWindows(Set<String> windows) {
		this.windows = windows;
	}
	
	@Override
	public int compare(EventLogRouteModel o1, EventLogRouteModel o2) {	
		return ((EventLogRouteModel) o1).getRouteNo().compareTo(((EventLogRouteModel) o2).getRouteNo());
	}
}
