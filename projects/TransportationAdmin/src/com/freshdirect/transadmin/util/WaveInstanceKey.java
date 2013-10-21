package com.freshdirect.transadmin.util;

import java.util.Date;

import com.freshdirect.transadmin.model.TrnFacility;

@SuppressWarnings("serial")
public class WaveInstanceKey implements java.io.Serializable {
	
	private Date deliveryDate;
	private TrnFacility originFacility;	
	private TrnFacility destinationFacility;
	private Date dispatchTime;
	private Date endTime;
	private Date maxTime;
	private Date cutOffTime;
	private String zone;
	private String equipmentType;

	public TrnFacility getOriginFacility() {
		return originFacility;
	}
	public void setOriginFacility(TrnFacility originFacility) {
		this.originFacility = originFacility;
	}
	public Date getDeliveryDate() {
		return deliveryDate;
	}
	public Date getDispatchTime() {
		return dispatchTime;
	}	
	public Date getEndTime() {
		return endTime;
	}
	public Date getCutOffTime() {
		return cutOffTime;
	}
	public String getZone() {
		return zone;
	}
	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	public void setDispatchTime(Date dispatchTime) {
		this.dispatchTime = dispatchTime;
	}	
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public void setCutOffTime(Date cutOffTime) {
		this.cutOffTime = cutOffTime;
	}
	public void setZone(String zone) {
		this.zone = zone;
	}	
	public Date getMaxTime() {
		return maxTime;
	}
	public void setMaxTime(Date maxTime) {
		this.maxTime = maxTime;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((cutOffTime == null) ? 0 : cutOffTime.hashCode());
		result = prime * result
				+ ((deliveryDate == null) ? 0 : deliveryDate.hashCode());
		result = prime
				* result
				+ ((destinationFacility == null) ? 0 : destinationFacility
						.hashCode());
		result = prime * result
				+ ((dispatchTime == null) ? 0 : dispatchTime.hashCode());
		result = prime * result
				+ ((equipmentType == null) ? 0 : equipmentType.hashCode());
		result = prime * result
				+ ((endTime == null) ? 0 : endTime.hashCode());
		result = prime * result
				+ ((maxTime == null) ? 0 : maxTime.hashCode());
		result = prime * result
				+ ((originFacility == null) ? 0 : originFacility.hashCode());
		result = prime * result + ((zone == null) ? 0 : zone.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WaveInstanceKey other = (WaveInstanceKey) obj;
		if (cutOffTime == null) {
			if (other.cutOffTime != null)
				return false;
		} else if (!cutOffTime.equals(other.cutOffTime))
			return false;
		if (deliveryDate == null) {
			if (other.deliveryDate != null)
				return false;
		} else if (!deliveryDate.equals(other.deliveryDate))
			return false;
		if (destinationFacility == null) {
			if (other.destinationFacility != null)
				return false;
		} else if (!destinationFacility.equals(other.destinationFacility))
			return false;
		if (dispatchTime == null) {
			if (other.dispatchTime != null)
				return false;
		} else if (!dispatchTime.equals(other.dispatchTime))
			return false;
		if (equipmentType == null) {
			if (other.equipmentType != null)
				return false;
		} else if (!equipmentType.equals(other.equipmentType))
			return false;
		if (endTime == null) {
			if (other.endTime != null)
				return false;
		} else if (!endTime.equals(other.endTime))
			return false;
		if (maxTime == null) {
			if (other.maxTime != null)
				return false;
		} else if (!maxTime.equals(other.maxTime))
			return false;
		if (originFacility == null) {
			if (other.originFacility != null)
				return false;
		} else if (!originFacility.equals(other.originFacility))
			return false;
		if (zone == null) {
			if (other.zone != null)
				return false;
		} else if (!zone.equals(other.zone))
			return false;
		return true;
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
	
	
}
