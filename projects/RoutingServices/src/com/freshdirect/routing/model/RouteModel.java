package com.freshdirect.routing.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

import com.freshdirect.routing.util.RoutingTimeOfDay;

public class RouteModel extends BaseModel implements IRouteModel {
	
	private String routeId;
	
	private TreeSet stops;
	
	private TreeSet breaks;
	
	private TreeSet allocatedStops;
	
	private Date firstDeliveryTime;
	
	private Date startTime;
	
	private Date completionTime;
	
	private IDrivingDirection drivingDirection;
	
	private List<String> routingRouteId;
	
	private double distance;
	private double travelTime;
	private double serviceTime;
	
	private String originId;
	
	private int preferredRunTime;
	private int maxRunTime;
		
	private RoutingTimeOfDay dispatchTime;	
	private int dispatchSequence;
	private List<String> roadNetRouteIds;
	private String roadNetRouteId;

	private String waveId;
	private String depotParkingLocation;
	
	public void copyWaveProperties(IWaveInstance waveInstance) {
		this.setMaxRunTime(waveInstance.getMaxRunTime());
		this.setPreferredRunTime(waveInstance.getPreferredRunTime());
		this.setDispatchTime(waveInstance.getDispatchTime());
	}
	
	public int getPreferredRunTime() {
		return preferredRunTime;
	}

	public int getMaxRunTime() {
		return maxRunTime;
	}

	public RoutingTimeOfDay getDispatchTime() {
		return dispatchTime;
	}

	public int getDispatchSequence() {
		return dispatchSequence;
	}

	public void setPreferredRunTime(int preferredRunTime) {
		this.preferredRunTime = preferredRunTime;
	}

	public void setMaxRunTime(int maxRunTime) {
		this.maxRunTime = maxRunTime;
	}

	public void setDispatchTime(RoutingTimeOfDay dispatchTime) {
		this.dispatchTime = dispatchTime;
	}

	public void setDispatchSequence(int dispatchSequence) {
		this.dispatchSequence = dispatchSequence;
	}

	public List<String> getRoutingRouteId() {
		return routingRouteId;
	}

	public void setRoutingRouteId(List<String> routingRouteId) {
		this.routingRouteId = routingRouteId;
	}

	public IDrivingDirection getDrivingDirection() {
		return drivingDirection;
	}

	public void setDrivingDirection(IDrivingDirection drivingDirection) {
		this.drivingDirection = drivingDirection;
	}

	public String getRouteId() {
		return routeId;
	}

	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public TreeSet getStops() {
		return stops;
	}

	public void setStops(TreeSet stops) {
		this.stops = stops;
	}
	
	public Date getCompletionTime() {
		return completionTime;
	}

	public void setCompletionTime(Date completionTime) {
		this.completionTime = completionTime;
	}
	
	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}
	

	public double getTravelTime() {
		return travelTime;
	}

	public void setTravelTime(double travelTime) {
		this.travelTime = travelTime;
	}

	public double getServiceTime() {
		return serviceTime;
	}

	public void setServiceTime(double serviceTime) {
		this.serviceTime = serviceTime;
	}

	public void appendRoutingRoute(String routingRouteId) {
		if(this.getRoutingRouteId() == null) {
			this.setRoutingRouteId(new ArrayList<String>());
		} 
		this.getRoutingRouteId().add(routingRouteId);
	}
		
	public String getOriginId() {
		return originId;
	}

	public void setOriginId(String originId) {
		this.originId = originId;
	}

	public Date getFirstDeliveryTime() {
		return firstDeliveryTime;
	}

	public void setFirstDeliveryTime(Date firstDeliveryTime) {
		this.firstDeliveryTime = firstDeliveryTime;
	}
	
	public String getDepotParkingLocation() {
		return depotParkingLocation;
	}

	public void setDepotParkingLocation(String depotParkingLocation) {
		this.depotParkingLocation = depotParkingLocation;
	}

	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((routeId == null) ? 0 : routeId.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final RouteModel other = (RouteModel) obj;
		if (routeId == null) {
			if (other.routeId != null)
				return false;
		} else if (!routeId.equals(other.routeId))
			return false;
		return true;
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("############################### ").append(routeId).append(" ").append(this.getStartTime()).append(" -> ")
									.append(getStops() != null ? this.getStops().size() : 0).append(" Stops ###############################\n");
		/*if(getStops() != null) {
			Iterator itr = this.getStops().iterator();
			while(itr.hasNext()) {
				IRoutingStopModel stop = (IRoutingStopModel)itr.next();
				buf.append("\t").append(stop.toString()).append("\n");
			}
		}
		buf.append("####################################################################################################\n\n");*/
		return buf.toString();
	}

	public List<String> getRoadNetRouteIds() {
		return roadNetRouteIds;
	}

	public void setRoadNetRouteIds(List<String> roadNetRouteIds) {
		this.roadNetRouteIds = roadNetRouteIds;
	}

	@Override
	public String getRoadNetRouteId() {
		return roadNetRouteId;
	}

	@Override
	public void setRoadNetRouteId(String roadNetRouteId) {
		this.roadNetRouteId = roadNetRouteId;
	}

	@Override
	public void appendRoadNetRouteId(String roadNetRouteId) {
		if(this.getRoadNetRouteIds() == null) {
			this.setRoadNetRouteIds(new ArrayList<String>());
		} 
		this.getRoadNetRouteIds().add(roadNetRouteId);
	}

	public String getWaveId() {
		
		return waveId;
	}

	public void setWaveId(String waveId) {
		this.waveId = waveId;
	}

	public TreeSet getAllocatedStops() {
		return allocatedStops;
	}

	public void setAllocatedStops(TreeSet allocatedStops) {
		this.allocatedStops = allocatedStops;
	}
	
	public TreeSet getBreaks() {
		return this.breaks;
	}

	public void setBreaks(TreeSet breaks) {
		this.breaks = breaks;
	}
}
