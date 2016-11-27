package com.freshdirect.routing.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class HandOffBatchPlan implements IHandOffBatchPlan  {
	
	private String planId;
	private String zoneCode;
	private String region;
	private Date planDate;

	private Date dispatchTime;
	private Date endTime;
	private Date maxTime;

	private Date cutOffTime;
	
	private int sequence;
	private String isBullpen;
	private Set batchPlanResources = new HashSet(0);
	private String supervisorId;
	private String userId;
	private String isOpen;
	private Boolean isTeamOverride;
	
	private String originFacility;
	private String destinationFacility;
	private Date dispatchGroup;
	private int runnerMax;
	
	public HandOffBatchPlan() {
		super();
	}

	public HandOffBatchPlan(String planId, String zoneCode, String region,
			Date planDate, Date endTime, Date dispatchTime,
			int sequence, String isBullpen, Set batchPlanResources,
			String supervisorId,String userId, Date maxTime, String isOpen, Boolean isTeamOverride, String originFacility,
			String destinationFacility,
			Date cutOffTime, Date dispatchGroup) {
		super();
		this.planId = planId;
		this.zoneCode = zoneCode;
		this.region = region;
		this.planDate = planDate;
		this.endTime = endTime;
		this.dispatchTime = dispatchTime;
		this.sequence = sequence;
		this.isBullpen = isBullpen;
		this.batchPlanResources = batchPlanResources;	
		this.supervisorId = supervisorId;
		this.userId = userId;
		this.maxTime = maxTime;
		this.isOpen = isOpen;
		this.isTeamOverride = isTeamOverride;
		this.originFacility = originFacility;
		this.destinationFacility = destinationFacility;
		this.cutOffTime = cutOffTime;
		this.dispatchGroup = dispatchGroup;
	}

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public String getZoneCode() {
		return zoneCode;
	}

	public void setZoneCode(String zoneCode) {
		this.zoneCode = zoneCode;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public Date getPlanDate() {
		return planDate;
	}

	public void setPlanDate(Date planDate) {
		this.planDate = planDate;
	}

	public Date getDispatchTime() {
		return dispatchTime;
	}

	public void setDispatchTime(Date dispatchTime) {
		this.dispatchTime = dispatchTime;
	}

	public Date getDispatchGroup() {
		return dispatchGroup;
	}

	public void setDispatchGroup(Date dispatchGroup) {
		this.dispatchGroup = dispatchGroup;
	}

	public int getSequence() {
		return sequence;
	}
	
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public String getIsBullpen() {
		return isBullpen;
	}

	public void setIsBullpen(String isBullpen) {
		this.isBullpen = isBullpen;
	}

	public Set getBatchPlanResources() {
		return batchPlanResources;
	}

	public void setBatchPlanResources(Set batchPlanResources) {
		this.batchPlanResources = batchPlanResources;
	}

	public String getSupervisorId() {
		return supervisorId;
	}

	public void setSupervisorId(String supervisorId) {
		this.supervisorId = supervisorId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getMaxTime() {
		return maxTime;
	}

	public void setMaxTime(Date maxTime) {
		this.maxTime = maxTime;
	}

	public String getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(String isOpen) {
		this.isOpen = isOpen;
	}

	public Boolean getIsTeamOverride() {
		return isTeamOverride;
	}

	public void setIsTeamOverride(Boolean isTeamOverride) {
		this.isTeamOverride = isTeamOverride;
	}
	
	public Date getCutOffTime() {
		return cutOffTime;
	}

	public void setCutOffTime(Date cutOffTime) {
		this.cutOffTime = cutOffTime;
	}	
		
	public String getOriginFacility() {
		return originFacility;
}

	public void setOriginFacility(String originFacility) {
		this.originFacility = originFacility;
	}

	public String getDestinationFacility() {
		return destinationFacility;
	}

	public void setDestinationFacility(String destinationFacility) {
		this.destinationFacility = destinationFacility;
	}

	public int getRunnerMax() {
		return runnerMax;
	}

	public void setRunnerMax(int runnerMax) {
		this.runnerMax = runnerMax;
	}

}
