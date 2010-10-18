package com.freshdirect.routing.model;

import java.util.Date;


public interface IHandOffBatchDepotScheduleEx {
	
	public String getDayOfWeek();
	Date getCutOffDateTime();
	void setDayOfWeek(String dayOfWeek);
	void setCutOffDateTime(Date cutOffDateTime);
	
	String getArea();
	Date getDepotArrivalTime();
	Date getTruckDepartureTime();
	String getOriginId();
	
	void setArea(String area);
	void setDepotArrivalTime(Date depotArrivalTime);
	void setTruckDepartureTime(Date truckDepartureTime);	
	void setOriginId(String originId);
}
