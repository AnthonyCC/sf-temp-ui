package com.freshdirect.transadmin.model;


public class RouteMapping implements java.io.Serializable, TrnBaseEntityI {

	private RouteMappingId routeMappingId;
	
	private String routingSessionID;
	
	public RouteMapping() {
	}

	public RouteMapping(RouteMappingId id) {
		this.routeMappingId = id;		
	}
	

	public RouteMappingId getRouteMappingId() {
		return routeMappingId;
	}

	public void setRouteMappingId(RouteMappingId routeMappingId) {
		this.routeMappingId = routeMappingId;
	}

	public String getRoutingSessionID() {
		return routingSessionID;
	}

	public void setRoutingSessionID(String routingSessionID) {
		this.routingSessionID = routingSessionID;
	}

	public boolean isObsoleteEntity() {
		return false;
	}

	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((routeMappingId == null) ? 0 : routeMappingId.hashCode());
		result = PRIME * result + ((routingSessionID == null) ? 0 : routingSessionID.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final RouteMapping other = (RouteMapping) obj;
		if (routeMappingId == null) {
			if (other.routeMappingId != null)
				return false;
		} else if (!routeMappingId.equals(other.routeMappingId))
			return false;
		if (routingSessionID == null) {
			if (other.routingSessionID != null)
				return false;
		} else if (!routingSessionID.equals(other.routingSessionID))
			return false;
		return true;
	}
	

}