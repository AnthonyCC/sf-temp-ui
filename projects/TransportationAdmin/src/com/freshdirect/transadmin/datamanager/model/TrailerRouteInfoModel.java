package com.freshdirect.transadmin.datamanager.model;

import java.util.Date;

public class TrailerRouteInfoModel implements ITrailerRouteInfoModel {
	
	private Date deliveryDate;
	private String trailerNo;
	
	private Date dispatchTime;
	private int dispatchSequence;
	
	private String totalDistance;
	private Date totalTravelTime;
	private Date totalServiceTime;
	private Date routeCompleteTime;
	
	private String routeId;
	private int noOfStops;
	private double noOfCartons;
	
	public Date getDeliveryDate() {
		return deliveryDate;
	}
	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	public String getTrailerNo() {
		return trailerNo;
	}
	public void setTrailerNo(String trailerNo) {
		this.trailerNo = trailerNo;
	}
	public Date getDispatchTime() {
		return dispatchTime;
	}
	public void setDispatchTime(Date dispatchTime) {
		this.dispatchTime = dispatchTime;
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
	public int getDispatchSequence() {
		return dispatchSequence;
	}
	public void setDispatchSequence(int dispatchSequence) {
		this.dispatchSequence = dispatchSequence;
	}
	public String getRouteId() {
		return routeId;
	}
	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}
	public int getNoOfStops() {
		return noOfStops;
	}
	public void setNoOfStops(int noOfStops) {
		this.noOfStops = noOfStops;
	}
	public double getNoOfCartons() {
		return noOfCartons;
	}
	public void setNoOfCartons(double noOfCartons) {
		this.noOfCartons = noOfCartons;
	}
}
