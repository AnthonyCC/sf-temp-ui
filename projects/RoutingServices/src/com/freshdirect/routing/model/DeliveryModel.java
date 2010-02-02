package com.freshdirect.routing.model;

import java.util.Date;

public class DeliveryModel extends BaseModel implements IDeliveryModel {

	private Date deliveryDate;
	private String deliveryModel;
	private IZoneModel deliveryZone;	
	private Date deliveryStartTime;
	private Date deliveryEndTime;
	private String serviceType;
	
	private IPackagingModel packagingInfo;
	private double serviceTime;
	private double orderSize;
	
	private ILocationModel deliveryLocation;
	
	private String reservationId;
	
	public double getOrderSize() {
		return orderSize;
	}
	public void setOrderSize(double orderSize) {
		this.orderSize = orderSize;
	}
	
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
		
	public void setServiceTime(double serviceTime) {
		this.serviceTime = serviceTime;
	}
	public IPackagingModel getPackagingInfo() {
		return packagingInfo;
	}
	public void setPackagingInfo(IPackagingModel packagingInfo) {
		this.packagingInfo = packagingInfo;
		this.setOrderSize(packagingInfo.getTotalSize1());		
	}
	public double getServiceTime() {
		return serviceTime;
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
		
			
	public String toString() {
		return deliveryDate+"|"+deliveryZone+"|"+deliveryStartTime+"|"+deliveryEndTime;
	}
	
}
