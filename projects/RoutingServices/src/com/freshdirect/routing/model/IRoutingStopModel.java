package com.freshdirect.routing.model;

import java.util.Date;

public interface IRoutingStopModel extends IOrderModel {

	String DEPOT_STOPNO = "DPT";

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

	double getTravelTime();

	void setTravelTime(double travelTime);

	double getServiceTime();

	void setServiceTime(double serviceTime);

	double getOrderSize();

	void setOrderSize(double orderSize);

}
