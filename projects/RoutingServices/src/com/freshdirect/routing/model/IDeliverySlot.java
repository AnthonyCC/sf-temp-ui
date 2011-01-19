package com.freshdirect.routing.model;

import java.util.Date;

public interface IDeliverySlot {
	
	IDeliverySlotCost getDeliveryCost();
	void setDeliveryCost(IDeliverySlotCost deliveryCost);
	
	boolean isManuallyClosed();
	void setManuallyClosed(boolean manuallyClosed);
	
	IRoutingSchedulerIdentity getSchedulerId();
	void setSchedulerId(IRoutingSchedulerIdentity schedulerId);
	
	Date getStartTime();
	void setStartTime(Date startTime);
	
	Date getStopTime();
	void setStopTime(Date stopTime);
	
	String getWaveCode();
	void setWaveCode(String waveCode);
	
	String getZoneCode();
	void setZoneCode(String zoneCode);
	
	boolean isDynamicActive();
	void setDynamicActive(boolean isDynamic);
	
	IDeliveryWindowMetrics getDeliveryMetrics();
	void setDeliveryMetrics(IDeliveryWindowMetrics deliveryMetrics);
	
	String getReferenceId();
	void setReferenceId(String referenceId);
	
}
