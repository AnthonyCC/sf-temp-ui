package com.freshdirect.analytics;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author tbalumuri
 * 
 */

public class TimeslotEventDetailModel implements Serializable {

	private double ws_amount;
	private boolean alcohol_restriction;
	private boolean holiday_restriction;
	private boolean ecofriendlyslot;
	private boolean neighbourhoodslot;
	private int totalCapacity;
	private int ctCapacity;
	private boolean storefront_avl;
	private int ctAllocated;
	private int totalAllocated;
	private RoutingModel routingModel;
	private String zoneCode;
	private Date startTime;
	private Date stopTime;
	private Date deliveryDate;
	private Date cutOff;
	private boolean manuallyClosed;
	private boolean geoRestricted;
	
	public double getWs_amount() {
		return ws_amount;
	}

	public void setWs_amount(double ws_amount) {
		this.ws_amount = ws_amount;
	}

	public boolean isAlcohol_restriction() {
		return alcohol_restriction;
	}

	public void setAlcohol_restriction(boolean alcohol_restriction) {
		this.alcohol_restriction = alcohol_restriction;
	}

	public boolean isHoliday_restriction() {
		return holiday_restriction;
	}

	public void setHoliday_restriction(boolean holiday_restriction) {
		this.holiday_restriction = holiday_restriction;
	}

	public boolean isEcofriendlyslot() {
		return ecofriendlyslot;
	}

	public void setEcofriendlyslot(boolean ecofriendlyslot) {
		this.ecofriendlyslot = ecofriendlyslot;
	}

	public boolean isNeighbourhoodslot() {
		return neighbourhoodslot;
	}

	public void setNeighbourhoodslot(boolean neighbourhoodslot) {
		this.neighbourhoodslot = neighbourhoodslot;
	}

	public int getTotalCapacity() {
		return totalCapacity;
	}

	public void setTotalCapacity(int totalCapacity) {
		this.totalCapacity = totalCapacity;
	}

	public int getCtCapacity() {
		return ctCapacity;
	}

	public void setCtCapacity(int ctCapacity) {
		this.ctCapacity = ctCapacity;
	}

	public boolean isStorefront_avl() {
		return storefront_avl;
	}

	public void setStorefront_avl(boolean storefront_avl) {
		this.storefront_avl = storefront_avl;
	}

	public int getCtAllocated() {
		return ctAllocated;
	}

	public void setCtAllocated(int ctAllocated) {
		this.ctAllocated = ctAllocated;
	}

	public int getTotalAllocated() {
		return totalAllocated;
	}

	public void setTotalAllocated(int totalAllocated) {
		this.totalAllocated = totalAllocated;
	}

	public RoutingModel getRoutingModel() {
		return routingModel;
	}

	public void setRoutingModel(RoutingModel routingModel) {
		this.routingModel = routingModel;
	}

	public String getZoneCode() {
		return zoneCode;
	}

	public void setZoneCode(String zoneCode) {
		this.zoneCode = zoneCode;
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

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public boolean isManuallyClosed() {
		return manuallyClosed;
	}

	public void setManuallyClosed(boolean manuallyClosed) {
		this.manuallyClosed = manuallyClosed;
	}

	public Date getCutOff() {
		return cutOff;
	}

	public void setCutOff(Date cutOff) {
		this.cutOff = cutOff;
	}

	public boolean isGeoRestricted() {
		return geoRestricted;
	}

	public void setGeoRestricted(boolean geoRestricted) {
		this.geoRestricted = geoRestricted;
	}

}
