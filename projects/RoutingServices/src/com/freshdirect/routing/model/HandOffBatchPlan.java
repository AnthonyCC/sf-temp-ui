package com.freshdirect.routing.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class HandOffBatchPlan implements IHandOffBatchPlan  {
	
	private String planId;
	private String zoneCode;
	private String region;
	private Date planDate;
	private Date firstDeliveryTime;
	private Date startTime;
	private int sequence;
	private String isBullpen;
	private Set batchPlanResources = new HashSet(0);
	private String supervisorId;
	private String userId;
	private Date maxTime;
	private String isOpen;
	private Boolean isTeamOverride;
	
	private Date lastDeliveryTime;
	private Date cutOffTime;
	
	public HandOffBatchPlan() {
		super();
	}

	public HandOffBatchPlan(String planId, String zoneCode, String region,
			Date planDate, Date firstDeliveryTime, Date startTime,
			int sequence, String isBullpen, Set batchPlanResources,
			String supervisorId,String userId, Date maxTime, String isOpen, Boolean isTeamOverride,
			Date lastDeliveryTime, Date cutOffTime) {
		super();
		this.planId = planId;
		this.zoneCode = zoneCode;
		this.region = region;
		this.planDate = planDate;
		this.firstDeliveryTime = firstDeliveryTime;
		this.startTime = startTime;
		this.sequence = sequence;
		this.isBullpen = isBullpen;
		this.batchPlanResources = batchPlanResources;	
		this.supervisorId = supervisorId;
		this.userId = userId;
		this.maxTime = maxTime;
		this.isOpen = isOpen;
		this.isTeamOverride = isTeamOverride;
		this.lastDeliveryTime = lastDeliveryTime;
		this.cutOffTime = cutOffTime;
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

	public int getSequence() {
		return sequence;
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

	public Date getLastDeliveryTime() {
		return lastDeliveryTime;
	}

	public void setLastDeliveryTime(Date lastDeliveryTime) {
		this.lastDeliveryTime = lastDeliveryTime;
	}

	public Date getCutOffTime() {
		return cutOffTime;
	}

	public void setCutOffTime(Date cutOffTime) {
		this.cutOffTime = cutOffTime;
	}	
		
}
