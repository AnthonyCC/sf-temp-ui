package com.freshdirect.routing.model;

import java.math.BigDecimal;
import java.util.Date;

public interface IDeliverySlot {
	
	IDeliverySlotCost getDeliveryCost();
	void setDeliveryCost(IDeliverySlotCost deliveryCost);
	
	boolean isManuallyClosed();
	void setManuallyClosed(boolean manuallyClosed);
	
	IRoutingSchedulerIdentity getSchedulerId();
	void setSchedulerId(IRoutingSchedulerIdentity schedulerId);
	
	Date getDisplayStartTime();
	void setDisplayStartTime(Date displayStartTime);
	
	Date getDisplayStopTime();
	void setDisplayStopTime(Date displayStopTime);
	
	Date getStartTime();
	void setStartTime(Date startTime);
	
	Date getStopTime();
	void setStopTime(Date stopTime);
	
	Date getRoutingStartTime();
	void setRoutingStartTime(Date routingStartTime);
	
	Date getRoutingStopTime();
	void setRoutingStopTime(Date routingStopTime);
	
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
	
	BigDecimal getEcoFriendly();
	void setEcoFriendly(BigDecimal ecoFriendly);
	
	BigDecimal getSteeringRadius();
	void setSteeringRadius(BigDecimal steeringRadius);

	int getReservedOrdersAtBuilding();
	void setReservedOrdersAtBuilding(int reservedOrdersAtBuilding);
}
