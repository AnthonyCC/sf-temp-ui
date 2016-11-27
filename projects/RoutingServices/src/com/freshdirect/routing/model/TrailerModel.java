package com.freshdirect.routing.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import com.freshdirect.routing.util.RoutingTimeOfDay;

public class TrailerModel extends BaseModel implements ITrailerModel {
	
	private String trailerId;	
	private TreeSet routes;		
	private Date startTime;	
	private Date completionTime;	
	private IDrivingDirection drivingDirection;	
	private List<String> routingTrailerId;	
	private double distance;
	private double travelTime;
	private double serviceTime;	
	private String originId;	
	private int preferredRunTime;
	private int maxRunTime;		
	private RoutingTimeOfDay dispatchTime;	
	
	
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

	public void setPreferredRunTime(int preferredRunTime) {
		this.preferredRunTime = preferredRunTime;
	}

	public void setMaxRunTime(int maxRunTime) {
		this.maxRunTime = maxRunTime;
	}

	public void setDispatchTime(RoutingTimeOfDay dispatchTime) {
		this.dispatchTime = dispatchTime;
	}

	public List<String> getRoutingTrailerId() {
		return routingTrailerId;
	}

	public void setRoutingTrailerId(List<String> routingTrailerId) {
		this.routingTrailerId = routingTrailerId;
	}

	public IDrivingDirection getDrivingDirection() {
		return drivingDirection;
	}

	public void setDrivingDirection(IDrivingDirection drivingDirection) {
		this.drivingDirection = drivingDirection;
	}

	public String getTrailerId() {
		return trailerId;
	}

	public void setTrailerId(String trailerId) {
		this.trailerId = trailerId;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public TreeSet getRoutes() {
		return routes;
	}

	public void setRoutes(TreeSet routes) {
		this.routes = routes;
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

	public void appendRoutingTrailer(String routingTrailerId) {
		if(this.getRoutingTrailerId() == null) {
			this.setRoutingTrailerId(new ArrayList<String>());
		} 
		this.getRoutingTrailerId().add(routingTrailerId);
	}
		
	public String getOriginId() {
		return originId;
	}

	public void setOriginId(String originId) {
		this.originId = originId;
	}
	
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((trailerId == null) ? 0 : trailerId.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final TrailerModel other = (TrailerModel) obj;
		if (trailerId == null) {
			if (other.trailerId != null)
				return false;
		} else if (!trailerId.equals(other.trailerId))
			return false;
		return true;
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("############################### ").append(trailerId).append(" ").append(this.getStartTime()).append(" -> ")
									.append(getRoutes() != null ? this.getRoutes().size() : 0).append(" Routes ###############################\n");
		if(getRoutes() != null) {
			Iterator itr = this.getRoutes().iterator();
			while(itr.hasNext()) {
				IRouteModel route = (IRouteModel)itr.next();
				buf.append("\t").append(route.toString()).append("\n");
			}
		}
		buf.append("####################################################################################################\n\n");
		return buf.toString();
	}

	
}
