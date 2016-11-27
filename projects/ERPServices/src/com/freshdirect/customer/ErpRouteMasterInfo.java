package com.freshdirect.customer;

import java.io.Serializable;
import java.util.*;

//import com.freshdirect.transadmin.web.model.WebEmployeeInfo;

//import com.freshdirect.transadmin.model.Plan;

public class ErpRouteMasterInfo implements Serializable, Comparable<ErpRouteMasterInfo> {
	
	public ErpRouteMasterInfo(
		String routeNumber,
		String zoneNumber,
		String routeTime,
		String numberOfStops,		
		String firstDlvTime,
		String truckNumber
		) {
		this.routeNumber = routeNumber;		
		this.zoneNumber = zoneNumber;
		this.routeTime = routeTime;
		this.numberOfStops = numberOfStops;
		this.firstDlvTime=firstDlvTime;
		this.truckNumber = truckNumber;
		
	}

	public ErpRouteMasterInfo() {
		// TODO Auto-generated constructor stub
	}

	public void setDetails(List list) {
		if(list == null)
			list = new ArrayList();
		else
			details = list;
	}
	
	public List getDetails() {
		return details;
	}
	
	public String toString1() {
		return "ErpRouteMasterInfo[routeNumber: "
			+ this.routeNumber
			+ " zoneNumber: "
			+ this.zoneNumber
			+ "TruckNumber: "
			+ this.truckNumber
			+ " routeTime: "
			+ this.routeTime
			+ " numberOfStops: "
			+ numberOfStops.toString();
	}

	private String routeNumber;	
	private String zoneNumber;
	private String routeTime;
	private String numberOfStops;
	private String truckNumber;
	private String firstDlvTime;
	
	private List details = new ArrayList();

	public String getNumberOfStops() {
		return numberOfStops;
	}

	public void setNumberOfStops(String numberOfStops) {
		this.numberOfStops = numberOfStops;
	}

	public String getRouteNumber() {
		return routeNumber;
	}

	public void setRouteNumber(String routeNumber) {
		this.routeNumber = routeNumber;
	}

	public String getRouteTime() {
		return routeTime;
	}

	public void setRouteTime(String routeTime) {
		this.routeTime = routeTime;
	}

	public String getZoneNumber() {
		return zoneNumber;
	}

	public void setZoneNumber(String zoneNumber) {
		this.zoneNumber = zoneNumber;
	}

	public String getTruckNumber() {
		return truckNumber;
	}
	public String getFirstDlvTime() {
		return firstDlvTime;
	}

	public void setTruckNumber(String truckNumber) {
		this.truckNumber = truckNumber;
	}
	public void setFirstDlvTime(String firstDlvTime) {
		this.firstDlvTime = firstDlvTime;
	}

	 //added new code Appdev- 808
	public String toString() {
		return this.getRouteNumber() != null ? this.getRouteNumber().toString() : "";
	}
	
	
	@Override
	public int compareTo(ErpRouteMasterInfo  o) {
		// TODO Auto-generated method stub
		return this.toString().compareTo(o.toString());
	}

	

				
	
}
