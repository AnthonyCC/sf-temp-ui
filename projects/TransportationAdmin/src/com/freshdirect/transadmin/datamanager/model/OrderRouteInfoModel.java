package com.freshdirect.transadmin.datamanager.model;

import java.util.Date;

public class OrderRouteInfoModel  implements IOrderRouteInfo {
	
	private String locationId;
	private String orderNumber;
	private String totalSize1;
	private String totalSize2;
	private String fixedServiceTime;
	private Date timeWindowStart;
	private Date timeWindowStop;
	private Date deliveryDate;
	private String deliveryZone;
	private Date orderBeginDate;
	private Date orderEndDate;
	private String routeId;
	private String stopNumber;
	private Date stopArrivalTime;
	private Date routeStartTime;
	private String plant = "1000";	
	private String deliveryModel = "003";
	private String deliveryArea;
		
	public String getDeliveryModel() {
		return deliveryModel;
	}
	public void setDeliveryModel(String deliveryModel) {
		this.deliveryModel = deliveryModel;
	}
	public String getFixedServiceTime() {
		return fixedServiceTime;
	}
	public void setFixedServiceTime(String fixedServiceTime) {
		this.fixedServiceTime = fixedServiceTime;
	}
	public String getLocationId() {
		return locationId;
	}
	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}
	
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public String getPlant() {
		return plant;
	}
	public void setPlant(String plant) {
		this.plant = plant;
	}
	public String getRouteId() {
		return routeId;
	}
	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}
	
	public String getStopNumber() {
		return stopNumber;
	}
	public void setStopNumber(String stopNumber) {
		this.stopNumber = stopNumber;
	}
	
	public String getTotalSize1() {
		return totalSize1;
	}
	public void setTotalSize1(String totalSize1) {
		this.totalSize1 = totalSize1;
	}
	public String getTotalSize2() {
		return totalSize2;
	}
	public void setTotalSize2(String totalSize2) {
		this.totalSize2 = totalSize2;
	}
	public Date getDeliveryDate() {
		return deliveryDate;
	}
	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	public Date getOrderBeginDate() {
		return orderBeginDate;
	}
	public void setOrderBeginDate(Date orderBeginDate) {
		this.orderBeginDate = orderBeginDate;
	}
	public Date getOrderEndDate() {
		return orderEndDate;
	}
	public void setOrderEndDate(Date orderEndDate) {
		this.orderEndDate = orderEndDate;
	}
	public Date getRouteStartTime() {
		return routeStartTime;
	}
	public void setRouteStartTime(Date routeStartTime) {
		this.routeStartTime = routeStartTime;
	}
	public Date getStopArrivalTime() {
		return stopArrivalTime;
	}
	public void setStopArrivalTime(Date stopArrivalTime) {
		this.stopArrivalTime = stopArrivalTime;
	}
	public Date getTimeWindowStart() {
		return timeWindowStart;
	}
	public void setTimeWindowStart(Date timeWindowStart) {
		this.timeWindowStart = timeWindowStart;
	}
	public Date getTimeWindowStop() {
		return timeWindowStop;
	}
	public void setTimeWindowStop(Date timeWindowStop) {
		this.timeWindowStop = timeWindowStop;
	}
	public String getDeliveryZone() {
		return deliveryZone;
	}
	public void setDeliveryZone(String deliveryZone) {
		this.deliveryZone = deliveryZone;
	}
	public String getDeliveryArea() {
		return deliveryArea;
	}
	public void setDeliveryArea(String deliveryArea) {
		this.deliveryArea = deliveryArea;
	}
	public String toString() {
		return orderNumber.toString();
	}


}
