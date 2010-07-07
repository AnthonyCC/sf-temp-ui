package com.freshdirect.routing.model;

import java.util.Date;
import java.util.TreeSet;

public interface IRouteModel {
	
	String getRouteId();
	void setRouteId(String routeId);

	Date getRouteStartTime();
	void setRouteStartTime(Date routeStartTime);

	TreeSet getStops();
	void setStops(TreeSet stops);
	
	IDrivingDirection getDrivingDirection();
	void setDrivingDirection(IDrivingDirection drivingDirection);
	
	Date getCompletionTime();
	void setCompletionTime(Date completionTime);
}
