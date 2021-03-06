package com.freshdirect.routing.model;

import java.util.Date;
import java.util.List;
import java.util.TreeSet;

import com.freshdirect.routing.util.RoutingTimeOfDay;

public interface IRouteModel {
	
	String getRouteId();
	void setRouteId(String routeId);
	
	Date getStartTime();
	void setStartTime(Date startTime);

	TreeSet getStops();
	void setStops(TreeSet stops);
	
	TreeSet getAllocatedStops();
	void setAllocatedStops(TreeSet stops);
	
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
	
	String getOriginId();
	void setOriginId(String originId);

	int getPreferredRunTime();
	int getMaxRunTime();
	
	RoutingTimeOfDay getDispatchTime();
	int getDispatchSequence();

	void setPreferredRunTime(int preferredRunTime);
	void setMaxRunTime(int maxRunTime);

	void setDispatchTime(RoutingTimeOfDay dispatchTime);
	void setDispatchSequence(int dispatchSequence);
	
	void appendRoutingRoute(String routingRouteId);
	void copyWaveProperties(IWaveInstance waveInstance);
	
	Date getFirstDeliveryTime();
	void setFirstDeliveryTime(Date firstDeliveryTime);
	
	List<String> getRoadNetRouteIds();
	void setRoadNetRouteIds(List<String> roadNetRouteId);
	
	String getRoadNetRouteId();
	void setRoadNetRouteId(String roadNetRouteId);
	
	void appendRoadNetRouteId(String roadNetRouteId);
	
	String getWaveId();
	void setWaveId(String waveId);
	
	String getDepotParkingLocation();
	void setDepotParkingLocation(String depotParkingLocation);
	
	TreeSet getBreaks();
	void setBreaks(TreeSet breaks);
	
}
