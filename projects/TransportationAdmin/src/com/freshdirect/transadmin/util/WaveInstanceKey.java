package com.freshdirect.transadmin.util;

import java.util.Date;

public class WaveInstanceKey implements java.io.Serializable {
	
	private Date deliveryDate;
	private Date dispatchTime;
	private Date firstDeliveryTime;
	private Date lastDeliveryTime;
	private Date cutOffTime;
	private String zone;
	public Date getDeliveryDate() {
		return deliveryDate;
	}
	public Date getDispatchTime() {
		return dispatchTime;
	}
	public Date getFirstDeliveryTime() {
		return firstDeliveryTime;
	}
	public Date getLastDeliveryTime() {
		return lastDeliveryTime;
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
	public void setFirstDeliveryTime(Date firstDeliveryTime) {
		this.firstDeliveryTime = firstDeliveryTime;
	}
	public void setLastDeliveryTime(Date lastDeliveryTime) {
		this.lastDeliveryTime = lastDeliveryTime;
	}
	public void setCutOffTime(Date cutOffTime) {
		this.cutOffTime = cutOffTime;
	}
	public void setZone(String zone) {
		this.zone = zone;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((cutOffTime == null) ? 0 : cutOffTime.hashCode());
		result = prime * result
				+ ((deliveryDate == null) ? 0 : deliveryDate.hashCode());
		result = prime * result
				+ ((dispatchTime == null) ? 0 : dispatchTime.hashCode());
		result = prime
				* result
				+ ((firstDeliveryTime == null) ? 0 : firstDeliveryTime
						.hashCode());
		result = prime
				* result
				+ ((lastDeliveryTime == null) ? 0 : lastDeliveryTime.hashCode());
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
		if (dispatchTime == null) {
			if (other.dispatchTime != null)
				return false;
		} else if (!dispatchTime.equals(other.dispatchTime))
			return false;
		if (firstDeliveryTime == null) {
			if (other.firstDeliveryTime != null)
				return false;
		} else if (!firstDeliveryTime.equals(other.firstDeliveryTime))
			return false;
		if (lastDeliveryTime == null) {
			if (other.lastDeliveryTime != null)
				return false;
		} else if (!lastDeliveryTime.equals(other.lastDeliveryTime))
			return false;
		if (zone == null) {
			if (other.zone != null)
				return false;
		} else if (!zone.equals(other.zone))
			return false;
		return true;
	}
	
	
}
