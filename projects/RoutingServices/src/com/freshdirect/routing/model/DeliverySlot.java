package com.freshdirect.routing.model;

import java.math.BigDecimal;
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
	private Date routingStartTime;
	private Date routingStopTime;
	
	private Date displayStartTime;
	private Date displayStopTime;
	
	private String waveCode;
	private String zoneCode;
	
	private String referenceId;
	
	private BigDecimal ecoFriendly;
	private BigDecimal steeringRadius;
	// confirmed orders for the building /window
	private int reservedOrdersAtBuilding;
	public BigDecimal getEcoFriendly() {
		return ecoFriendly;
	}
	public void setEcoFriendly(BigDecimal ecoFriendly) {
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
	public BigDecimal getSteeringRadius() {
		return steeringRadius;
	}
	public void setSteeringRadius(BigDecimal steeringRadius) {
		this.steeringRadius = steeringRadius;
	}
	public int getReservedOrdersAtBuilding() {
		return reservedOrdersAtBuilding;
	}
	public void setReservedOrdersAtBuilding(int reservedOrdersAtBuilding) {
		this.reservedOrdersAtBuilding = reservedOrdersAtBuilding;
	}
	@Override
	public Date getRoutingStartTime() {
		return routingStartTime;
	}
	@Override
	public void setRoutingStartTime(Date routingStartTime) {
		this.routingStartTime = routingStartTime;
	}
	@Override
	public Date getRoutingStopTime() {
		return routingStopTime;
	}
	@Override
	public void setRoutingStopTime(Date routingStopTime) {
		this.routingStopTime = routingStopTime;
	}
	@Override
	public Date getDisplayStartTime() {
		return displayStartTime ;
	}
	@Override
	public void setDisplayStartTime(Date displayStartTime) {
		this.displayStartTime = displayStartTime;
	}
	@Override
	public Date getDisplayStopTime() {
		return displayStopTime ;
	}
	@Override
	public void setDisplayStopTime(Date displayStopTime) {
		this.displayStopTime = displayStopTime;
	}
	
}
