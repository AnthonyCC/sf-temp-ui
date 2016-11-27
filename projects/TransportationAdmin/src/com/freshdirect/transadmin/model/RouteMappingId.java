package com.freshdirect.transadmin.model;

import java.util.Date;

public class RouteMappingId implements java.io.Serializable {

	private Date routeDate;

	private String cutOffId;
	
	private String groupCode;
	
	private String routeID;
	
	private String routingRouteID;

	public RouteMappingId() {
	}
	
	public RouteMappingId(Date routeDate, String cutOffId, String groupCode, String routeID, String routingRouteID) {
		super();
		this.routeDate = routeDate;
		this.cutOffId = cutOffId;
		this.groupCode = groupCode;
		this.routeID = routeID;
		this.routingRouteID = routingRouteID;
	}

	public String getCutOffId() {
		return cutOffId;
	}

	public void setCutOffId(String cutOffId) {
		this.cutOffId = cutOffId;
	}

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public Date getRouteDate() {
		return routeDate;
	}

	public void setRouteDate(Date routeDate) {
		this.routeDate = routeDate;
	}

	public String getRouteID() {
		return routeID;
	}

	public void setRouteID(String routeID) {
		this.routeID = routeID;
	}

	public String getRoutingRouteID() {
		return routingRouteID;
	}

	public void setRoutingRouteID(String routingRouteID) {
		this.routingRouteID = routingRouteID;
	}

	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((cutOffId == null) ? 0 : cutOffId.hashCode());
		result = PRIME * result + ((groupCode == null) ? 0 : groupCode.hashCode());
		result = PRIME * result + ((routeDate == null) ? 0 : routeDate.hashCode());
		result = PRIME * result + ((routeID == null) ? 0 : routeID.hashCode());
		result = PRIME * result + ((routingRouteID == null) ? 0 : routingRouteID.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final RouteMappingId other = (RouteMappingId) obj;
		if (cutOffId == null) {
			if (other.cutOffId != null)
				return false;
		} else if (!cutOffId.equals(other.cutOffId))
			return false;
		if (groupCode == null) {
			if (other.groupCode != null)
				return false;
		} else if (!groupCode.equals(other.groupCode))
			return false;
		if (routeDate == null) {
			if (other.routeDate != null)
				return false;
		} else if (!routeDate.equals(other.routeDate))
			return false;
		if (routeID == null) {
			if (other.routeID != null)
				return false;
		} else if (!routeID.equals(other.routeID))
			return false;
		if (routingRouteID == null) {
			if (other.routingRouteID != null)
				return false;
		} else if (!routingRouteID.equals(other.routingRouteID))
			return false;
		return true;
	}

	public String toString() {
		return routeDate.toString()+"->"+groupCode+"->"+cutOffId+"->"+routeID+"->"+routingRouteID;
	}

}