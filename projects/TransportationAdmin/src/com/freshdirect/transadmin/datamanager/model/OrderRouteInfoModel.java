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
	private String tripId;
	private String stopNumber;
	private Date stopArrivalTime;
	private Date stopDepartureTime;
	private Date routeStartTime;
	private String plant = "1000";	
	private String deliveryModel = "003";
	private String deliveryArea;
	
	private String address;
	private String city;
	private String state;
	private String zipcode;
	private String latitude;
	private String longitude;
	
	private String totalDistance;
	private Date totalTravelTime;
	private Date totalServiceTime;
	private Date routeCompleteTime;
	
	private boolean isDepot;
	
	public boolean isDepot() {
		return isDepot;
	}
	public void setDepot(boolean isDepot) {
		this.isDepot = isDepot;
	}
	public String getTotalDistance() {
		return totalDistance;
	}
	public void setTotalDistance(String totalDistance) {
		this.totalDistance = totalDistance;
	}
	public Date getTotalTravelTime() {
		return totalTravelTime;
	}
	public void setTotalTravelTime(Date totalTravelTime) {
		this.totalTravelTime = totalTravelTime;
	}
	public Date getTotalServiceTime() {
		return totalServiceTime;
	}
	public void setTotalServiceTime(Date totalServiceTime) {
		this.totalServiceTime = totalServiceTime;
	}
	public Date getRouteCompleteTime() {
		return routeCompleteTime;
	}
	public void setRouteCompleteTime(Date routeCompleteTime) {
		this.routeCompleteTime = routeCompleteTime;
	}
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
		return orderNumber.toString()+"-"+timeWindowStart+"-"+timeWindowStop;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	public Date getStopDepartureTime() {
		return stopDepartureTime;
	}
	public void setStopDepartureTime(Date stopDepartureTime) {
		this.stopDepartureTime = stopDepartureTime;
	}
	public String getTripId() {
		return tripId;
	}
	public void setTripId(String tripId) {
		this.tripId = tripId;
	}

}
