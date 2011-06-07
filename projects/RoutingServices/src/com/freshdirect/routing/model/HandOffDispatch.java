package com.freshdirect.routing.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class HandOffDispatch implements IHandOffDispatch  {
	
	private String dispatchId;
	private String planId;
	private String zone;
	private String region;
	private Date dispatchDate;
	private Date firstDeliveryTime;
	private Date startTime;
	private String isBullpen;	
	private String supervisorId;
	private Date maxTime;
	private Date checkInTime;
	private String route;
	private String truck;
	private Date cutoffTime;
	private Set batchDispatchResources = new HashSet(0);
	
	public HandOffDispatch() {
		super();
	}

	public HandOffDispatch(String dispatchId, String planId, String zone,
			String region, Date dispatchDate, Date firstDeliveryTime,
			Date startTime, String isBullpen, String supervisorId,
			Date maxTime, Date checkInTime, String route, String truck, Date cutoffTime,
			Set batchDispatchResources) {
		super();
		this.dispatchId = dispatchId;
		this.planId = planId;
		this.zone = zone;
		this.region = region;
		this.dispatchDate = dispatchDate;
		this.firstDeliveryTime = firstDeliveryTime;
		this.startTime = startTime;
		this.isBullpen = isBullpen;
		this.supervisorId = supervisorId;
		this.maxTime = maxTime;
		this.checkInTime = checkInTime;
		this.route = route;
		this.truck = truck;
		this.cutoffTime = cutoffTime;
		this.batchDispatchResources = batchDispatchResources;
	}

	public String getDispatchId() {
		return dispatchId;
	}

	public void setDispatchId(String dispatchId) {
		this.dispatchId = dispatchId;
	}

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public Date getDispatchDate() {
		return dispatchDate;
	}

	public void setDispatchDate(Date dispatchDate) {
		this.dispatchDate = dispatchDate;
	}

	public Date getFirstDeliveryTime() {
		return firstDeliveryTime;
	}

	public void setFirstDeliveryTime(Date firstDeliveryTime) {
		this.firstDeliveryTime = firstDeliveryTime;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public String getIsBullpen() {
		return isBullpen;
	}

	public void setIsBullpen(String isBullpen) {
		this.isBullpen = isBullpen;
	}

	public String getSupervisorId() {
		return supervisorId;
	}

	public void setSupervisorId(String supervisorId) {
		this.supervisorId = supervisorId;
	}

	public Date getMaxTime() {
		return maxTime;
	}

	public void setMaxTime(Date maxTime) {
		this.maxTime = maxTime;
	}

	public Date getCheckInTime() {
		return checkInTime;
	}

	public void setCheckInTime(Date checkInTime) {
		this.checkInTime = checkInTime;
	}

	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}

	public String getTruck() {
		return truck;
	}

	public void setTruck(String truck) {
		this.truck = truck;
	}

	public Date getCutoffTime() {
		return cutoffTime;
	}

	public void setCutoffTime(Date cutoffTime) {
		this.cutoffTime = cutoffTime;
	}

	public Set getBatchDispatchResources() {
		return batchDispatchResources;
	}

	public void setBatchDispatchResources(Set batchDispatchResources) {
		this.batchDispatchResources = batchDispatchResources;
	}
	
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((dispatchId == null) ? 0 : dispatchId.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final HandOffDispatch other = (HandOffDispatch) obj;
		if (dispatchId == null) {
			if (other.dispatchId != null)
				return false;
		} else if (!dispatchId.equals(other.dispatchId))
			return false;
		return true;
	}
	
}
