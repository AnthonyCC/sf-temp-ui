package com.freshdirect.routing.model;

import java.util.Date;
import java.util.List;
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
	
	List<String> getRoutingRouteId();
	void setRoutingRouteId(List<String> routingRouteId);
	
	double getDistance();
	void setDistance(double distance);
	
	double getTravelTime();
	void setTravelTime(double travelTime);

	double getServiceTime();
	void setServiceTime(double serviceTime);
	
	void appendRoutingRoute(String routingRouteId);
}
