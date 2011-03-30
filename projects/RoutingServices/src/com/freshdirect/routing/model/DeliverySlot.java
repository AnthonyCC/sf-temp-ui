package com.freshdirect.routing.model;

import java.util.Date;

import com.freshdirect.routing.util.RoutingDateUtil;

public class DeliverySlot extends BaseModel implements IDeliverySlot  {
	
	private IDeliverySlotCost deliveryCost;
	private IDeliveryWindowMetrics deliveryMetrics;
	private boolean manuallyClosed;
	private boolean dynamicActive;
	
	private IRoutingSchedulerIdentity schedulerId;
	private Date startTime;
	private Date stopTime;
	private String waveCode;
	private String zoneCode;
	
	private String referenceId;
	
	private int ecoFriendly;
	
	public int getEcoFriendly() {
		return ecoFriendly;
	}
	public void setEcoFriendly(int ecoFriendly) {
		this.ecoFriendly = ecoFriendly;
	}
	public String getReferenceId() {
		return referenceId;
	}
	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}
	public String getZoneCode() {
		return zoneCode;
	}
	public void setZoneCode(String zoneCode) {
		this.zoneCode = zoneCode;
	}
	public IDeliverySlotCost getDeliveryCost() {
		return deliveryCost;
	}
	public void setDeliveryCost(IDeliverySlotCost deliveryCost) {
		this.deliveryCost = deliveryCost;
	}
	public boolean isManuallyClosed() {
		return manuallyClosed;
	}
	public void setManuallyClosed(boolean manuallyClosed) {
		this.manuallyClosed = manuallyClosed;
	}
	public IRoutingSchedulerIdentity getSchedulerId() {
		return schedulerId;
	}
	public void setSchedulerId(IRoutingSchedulerIdentity schedulerId) {
		this.schedulerId = schedulerId;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getStopTime() {
		return stopTime;
	}
	public void setStopTime(Date stopTime) {
		this.stopTime = stopTime;
	}
	
	public boolean isDynamicActive() {
		return dynamicActive;
	}
	public void setDynamicActive(boolean dynamicActive) {
		this.dynamicActive = dynamicActive;
	}
	
	public IDeliveryWindowMetrics getDeliveryMetrics() {
		return deliveryMetrics;
	}
	public void setDeliveryMetrics(IDeliveryWindowMetrics deliveryMetrics) {
		this.deliveryMetrics = deliveryMetrics;
	}
	
	public String formatTime(Date date) {
		try {
			return RoutingDateUtil.formatDateTime(date);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return "ERROR";
		}
	}
	public String toString() {
		return schedulerId + "["+formatTime(startTime)+"->"+formatTime(stopTime) +"->"+waveCode+"]";
	}
	public String getWaveCode() {
		return waveCode;
	}
	public void setWaveCode(String waveCode) {
		this.waveCode = waveCode;
	}
}
