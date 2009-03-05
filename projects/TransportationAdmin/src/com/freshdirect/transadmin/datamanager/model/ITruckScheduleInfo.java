package com.freshdirect.transadmin.datamanager.model;

import java.io.Serializable;
import java.util.Date;

public interface ITruckScheduleInfo extends Serializable {
	
	Date getDepotArrivalTime() ;
	void setDepotArrivalTime(Date depotArrivalTime);
	String getGroupCode();
	void setGroupCode(String groupCode);
	Date getTruckDepartureTime();
	void setTruckDepartureTime(Date truckDepartureTime);
}
