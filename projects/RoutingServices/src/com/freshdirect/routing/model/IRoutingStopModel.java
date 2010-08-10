package com.freshdirect.routing.model;

import java.util.Date;

public interface IRoutingStopModel extends IOrderModel {
	
	String DEPOT_STOPNO = "9999999999";
	
	 boolean isDepot();

	 void setDepot(boolean isDepot);
	
	 Date getStopArrivalTime();

	 void setStopArrivalTime(Date stopArrivalTime);
		
	 int getStopNo();

	 void setStopNo(int stopNo);
	
	 Date getStopDepartureTime();

	 void setStopDepartureTime(Date stopDepartureTime);
	 
	 String getRoutingRouteId();
	 void setRoutingRouteId(String routingRouteId);
}
