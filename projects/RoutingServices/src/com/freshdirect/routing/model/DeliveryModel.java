package com.freshdirect.routing.model;

import java.util.Date;

public class DeliveryModel extends BaseModel implements IDeliveryModel {

	private Date deliveryDate;
	private String deliveryModel;
	private IZoneModel deliveryZone;	
	private Date deliveryStartTime;
	private Date deliveryEndTime;
	private String serviceType;
	
	private IPackagingModel packagingDetail;
	
	private double calculatedServiceTime;
	private double calculatedOrderSize;
	
	private ILocationModel deliveryLocation;
	
	private String reservationId;
		
	public String getReservationId() {
		return reservationId;
	}
	public void setReservationId(String reservationId) {
		this.reservationId = reservationId;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	
	public String getDeliveryModel() {
		return deliveryModel;
	}
	public void setDeliveryModel(String deliveryModel) {
		this.deliveryModel = deliveryModel;
	}
		
	public ILocationModel getDeliveryLocation() {
		return deliveryLocation;
	}
	public void setDeliveryLocation(ILocationModel deliveryLocation) {
		this.deliveryLocation = deliveryLocation;
	}
		
	public IPackagingModel getPackagingDetail() {
		return packagingDetail;
	}
	public void setPackagingDetail(IPackagingModel packagingDetail) {
		this.packagingDetail = packagingDetail;
	}
	public Date getDeliveryDate() {
		return deliveryDate;
	}
	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
		
	public Date getDeliveryStartTime() {
		return deliveryStartTime;
	}
	public void setDeliveryStartTime(Date deliveryStartTime) {
		this.deliveryStartTime = deliveryStartTime;
	}
	public Date getDeliveryEndTime() {
		return deliveryEndTime;
	}
	public void setDeliveryEndTime(Date deliveryEndTime) {
		this.deliveryEndTime = deliveryEndTime;
	}
	public IZoneModel getDeliveryZone() {
		return deliveryZone;
	}
	public void setDeliveryZone(IZoneModel deliveryZone) {
		this.deliveryZone = deliveryZone;
	}
				
	public double getCalculatedServiceTime() {
		return calculatedServiceTime;
	}	
	public void setCalculatedServiceTime(double calculatedServiceTime) {
		this.calculatedServiceTime = calculatedServiceTime;
	}
	
	public double getCalculatedOrderSize() {
		return calculatedOrderSize;
	}
	public void setCalculatedOrderSize(double calculatedOrderSize) {
		this.calculatedOrderSize = calculatedOrderSize;
	}
	public String toString() {
		return deliveryDate+"|"+deliveryZone+"|"+deliveryStartTime+"|"+deliveryEndTime;
	}
	
}
