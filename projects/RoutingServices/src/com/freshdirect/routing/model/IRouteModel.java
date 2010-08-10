package com.freshdirect.routing.model;

import java.util.Date;
import java.util.TreeSet;

public interface IRouteModel {
	
	String getRouteId();
	void setRouteId(String routeId);
	
	Date getStartTime();
	void setStartTime(Date startTime);

	TreeSet getStops();
	void setStops(TreeSet stops);
	
	IDrivingDirection getDrivingDirection();
	void setDrivingDirection(IDrivingDirection drivingDirection);
	
	Date getCompletionTime();
	void setCompletionTime(Date completionTime);
	
	String getRoutingRouteId();
	void setRoutingRouteId(String routingRouteId);
	
	double getDistance();
	void setDistance(double distance);
	
	double getTravelTime();
	void setTravelTime(double travelTime);

	double getServiceTime();
	void setServiceTime(double serviceTime);
	
	void appendRoutingRoute(String routingRouteId);
}
