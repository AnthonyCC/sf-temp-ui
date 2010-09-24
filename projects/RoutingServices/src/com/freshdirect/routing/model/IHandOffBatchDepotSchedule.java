package com.freshdirect.routing.model;

import java.util.Date;


public interface IHandOffBatchDepotSchedule {
	
	String getBatchId();
	String getArea();
	Date getDepotArrivalTime();
	Date getTruckDepartureTime();
	String getOriginId();
	
	void setBatchId(String batchId);
	void setArea(String area);
	void setDepotArrivalTime(Date depotArrivalTime);
	void setTruckDepartureTime(Date truckDepartureTime);	
	void setOriginId(String originId);
}
