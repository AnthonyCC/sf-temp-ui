package com.freshdirect.routing.model;

import java.util.Date;
import java.util.List;
import java.util.TreeSet;

import com.freshdirect.routing.util.RoutingTimeOfDay;

public interface ITrailerModel {
	
	String getTrailerId();
	void setTrailerId(String trailerId);
	
	Date getStartTime();
	void setStartTime(Date startTime);

	TreeSet getRoutes();
	void setRoutes(TreeSet routes);
	
	IDrivingDirection getDrivingDirection();
	void setDrivingDirection(IDrivingDirection drivingDirection);
	
	Date getCompletionTime();
	void setCompletionTime(Date completionTime);
	
	List<String> getRoutingTrailerId();
	void setRoutingTrailerId(List<String> routingTrailerId);
	
	double getDistance();
	void setDistance(double distance);
	
	double getTravelTime();
	void setTravelTime(double travelTime);

	double getServiceTime();
	void setServiceTime(double serviceTime);
	
	String getOriginId();
	void setOriginId(String originId);

	int getPreferredRunTime();
	int getMaxRunTime();
	
	RoutingTimeOfDay getDispatchTime();
	void setDispatchTime(RoutingTimeOfDay dispatchTime);
	
	void setPreferredRunTime(int preferredRunTime);
	void setMaxRunTime(int maxRunTime);	
	
	void appendRoutingTrailer(String routingTrailerId);
	void copyWaveProperties(IWaveInstance waveInstance);	
	
}
