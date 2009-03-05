package com.freshdirect.transadmin.datamanager.model;

import java.util.Date;

public class TruckScheduleInfo implements ITruckScheduleInfo {
	
	private String groupCode;//For now areacode
	private Date depotArrivalTime;
	private Date truckDepartureTime;
	
	public TruckScheduleInfo(String groupCode, Date depotArrivalTime, Date truckDepartureTime) {
		super();
		this.groupCode = groupCode;
		this.depotArrivalTime = depotArrivalTime;
		this.truckDepartureTime = truckDepartureTime;
	}
	
	public TruckScheduleInfo() {
		super();
	}

	public Date getDepotArrivalTime() {
		return depotArrivalTime;
	}
	
	public void setDepotArrivalTime(Date depotArrivalTime) {
		this.depotArrivalTime = depotArrivalTime;
	}
	
	public String getGroupCode() {
		return groupCode;
	}
	
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
	
	public Date getTruckDepartureTime() {
		return truckDepartureTime;
	}
	
	public String toString() {
		return groupCode.toString()+","+depotArrivalTime.toString()+","+truckDepartureTime.toString()+"\n";
	}

	public void setTruckDepartureTime(Date truckDepartureTime) {
		this.truckDepartureTime = truckDepartureTime;
	}
	
	
}
