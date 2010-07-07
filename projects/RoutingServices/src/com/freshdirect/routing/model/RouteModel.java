package com.freshdirect.routing.model;

import java.util.Date;
import java.util.TreeSet;

public class RouteModel extends BaseModel implements IRouteModel {
	
	private String routeId;
	
	private TreeSet stops;
	
	private Date routeStartTime;
	
	private Date completionTime;
	
	private IDrivingDirection drivingDirection;

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

	public Date getRouteStartTime() {
		return routeStartTime;
	}

	public void setRouteStartTime(Date routeStartTime) {
		this.routeStartTime = routeStartTime;
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
}
