package com.freshdirect.routing.model;

import java.util.Date;

import com.freshdirect.routing.util.RoutingTimeOfDay;

public class WaveInstance  extends BaseModel implements IWaveInstance {
	
	private RoutingTimeOfDay dispatchTime;
	private RoutingTimeOfDay waveStartTime;
	private Date cutOffTime;
	private int preferredRunTime;
	private int maxRunTime;
	private int noOfResources;
	
	private int routingWaveInstanceId;
	
	private int priority;

	public int getPreferredRunTime() {
		return preferredRunTime;
	}

	public int getMaxRunTime() {
		return maxRunTime;
	}

	public int getNoOfResources() {
		return noOfResources;
	}

	public int getRoutingWaveInstanceId() {
		return routingWaveInstanceId;
	}
	
	public void setPreferredRunTime(int preferredRunTime) {
		this.preferredRunTime = preferredRunTime;
	}

	public void setMaxRunTime(int maxRunTime) {
		this.maxRunTime = maxRunTime;
	}

	public void setNoOfResources(int noOfResources) {
		this.noOfResources = noOfResources;
	}

	public void setRoutingWaveInstanceId(int routingWaveInstanceId) {
		this.routingWaveInstanceId = routingWaveInstanceId;
	}

	public Date getCutOffTime() {
		return cutOffTime;
	}

	public void setCutOffTime(Date cutOffTime) {
		this.cutOffTime = cutOffTime;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
	

	public RoutingTimeOfDay getDispatchTime() {
		return dispatchTime;
	}

	public RoutingTimeOfDay getWaveStartTime() {
		return waveStartTime;
	}

	public void setDispatchTime(RoutingTimeOfDay dispatchTime) {
		this.dispatchTime = dispatchTime;
	}

	public void setWaveStartTime(RoutingTimeOfDay waveStartTime) {
		this.waveStartTime = waveStartTime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((cutOffTime == null) ? 0 : cutOffTime.hashCode());
		result = prime * result
				+ ((dispatchTime == null) ? 0 : dispatchTime.hashCode());
		result = prime * result + maxRunTime;
		result = prime * result + preferredRunTime;
		result = prime * result
				+ ((waveStartTime == null) ? 0 : waveStartTime.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		WaveInstance other = (WaveInstance) obj;
		if (cutOffTime == null) {
			if (other.cutOffTime != null)
				return false;
		} else if (!cutOffTime.equals(other.cutOffTime))
			return false;
		if (dispatchTime == null) {
			if (other.dispatchTime != null)
				return false;
		} else if (!dispatchTime.equals(other.dispatchTime))
			return false;
		if (maxRunTime != other.maxRunTime)
			return false;
		if (preferredRunTime != other.preferredRunTime)
			return false;
		if (waveStartTime == null) {
			if (other.waveStartTime != null)
				return false;
		} else if (!waveStartTime.equals(other.waveStartTime))
			return false;
		return true;
	}
	
}
