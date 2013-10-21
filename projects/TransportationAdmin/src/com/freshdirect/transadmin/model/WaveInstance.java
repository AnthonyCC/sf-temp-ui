package com.freshdirect.transadmin.model;

import java.util.Date;

import com.freshdirect.routing.constants.EnumWaveInstanceStatus;

public class WaveInstance implements java.io.Serializable, TrnBaseEntityI  {

	private String waveInstanceId;
	private String area;
	private Date deliveryDate;
	private Date dispatchTime;
	private Date endTime;
	private Date maxTime;
	private Date cutOffTime;
	private int noOfResources;
	private EnumWaveInstanceStatus status;
	
	private Date modifiedTime;
	private String source;
	private String changedBy;
	private String referenceId;
	private String notificationMessage;
	private Boolean forceSynchronize;
	private TrnFacility originFacility;
	private TrnFacility destinationFacility;	
	private String equipmentType;
	private String todrestriction;

	
	public TrnFacility getOriginFacility() {
		return originFacility;
	}

	public void setOriginFacility(TrnFacility originFacility) {
		this.originFacility = originFacility;
	}

	public String getWaveInstanceId() {
		return waveInstanceId;
	}
	public String getArea() {
		return area;
	}
	public Date getDeliveryDate() {
		return deliveryDate;
	}
	public Date getEndTime() {
		return endTime;
	}
	public Date getCutOffTime() {
		return cutOffTime;
	}
	public int getNoOfResources() {
		return noOfResources;
	}
	public EnumWaveInstanceStatus getStatus() {
		return status;
	}
	public Date getModifiedTime() {
		return modifiedTime;
	}
	public String getSource() {
		return source;
	}
	public String getChangedBy() {
		return changedBy;
	}
	public String getReferenceId() {
		return referenceId;
	}
	public String getNotificationMessage() {
		return notificationMessage;
	}
	public Boolean getForceSynchronize() {
		return forceSynchronize;
	}
	
	public Date getDispatchTime() {
		return dispatchTime;
	}
	public void setDispatchTime(Date dispatchTime) {
		this.dispatchTime = dispatchTime;
	}
	public void setWaveInstanceId(String waveInstanceId) {
		this.waveInstanceId = waveInstanceId;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}	
	public Date getMaxTime() {
		return maxTime;
	}
	public void setMaxTime(Date maxTime) {
		this.maxTime = maxTime;
	}
	public void setCutOffTime(Date cutOffTime) {
		this.cutOffTime = cutOffTime;
	}
	public void setNoOfResources(int noOfResources) {
		this.noOfResources = noOfResources;
	}
	public void setStatus(EnumWaveInstanceStatus status) {
		this.status = status;
	}
	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public void setChangedBy(String changedBy) {
		this.changedBy = changedBy;
	}
	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}
	public void setNotificationMessage(String notificationMessage) {
		this.notificationMessage = notificationMessage;
	}
	public void setForceSynchronize(Boolean forceSynchronize) {
		this.forceSynchronize = forceSynchronize;
	}
	
	public boolean isObsoleteEntity() {
		// TODO Auto-generated method stub
		return false;
	}

	public TrnFacility getDestinationFacility() {
		return destinationFacility;
	}

	public void setDestinationFacility(TrnFacility destinationFacility) {
		this.destinationFacility = destinationFacility;
	}

	public String getEquipmentType() {
		return equipmentType;
	}

	public void setEquipmentType(String equipmentType) {
		this.equipmentType = equipmentType;
	}

	public String getTodrestriction() {
		return todrestriction;
	}

	public void setTodrestriction(String todrestriction) {
		this.todrestriction = todrestriction;
	}

}
