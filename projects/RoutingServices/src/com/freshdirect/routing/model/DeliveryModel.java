package com.freshdirect.routing.model;

import java.util.Date;

public class DeliveryModel extends BaseModel implements IDeliveryModel {

	private Date deliveryDate;
	private String deliveryModel;
	private IZoneModel deliveryZone;	
	private Date deliveryStartTime;
	private Date deliveryEndTime;
	private Date routingStartTime;
	private Date routingEndTime;
	private String serviceType;
	private Date deliveryCutoffTime;
	private IPackagingModel packagingDetail;
	
	private IServiceTime calculatedServiceTime;
	private double calculatedOrderSize;
	
	private ILocationModel deliveryLocation;
	
	private String reservationId;
	// confirmed orders for the building /window
	private int reservedOrdersAtBuilding;
	
	private Date deliveryETAStartTime;
	private Date deliveryETAEndTime;
	
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
				
	public IServiceTime getCalculatedServiceTime() {
		return calculatedServiceTime;
	}	
	public void setCalculatedServiceTime(IServiceTime calculatedServiceTime) {
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
	public int getReservedOrdersAtBuilding() {
		return reservedOrdersAtBuilding;
	}
	public void setReservedOrdersAtBuilding(int reservedOrdersAtBuilding) {
		this.reservedOrdersAtBuilding = reservedOrdersAtBuilding;
	}
	@Override
	public Date getDeliveryCutoffTime() {
		return deliveryCutoffTime;
	}
	@Override
	public void setDeliveryCutoffTime(Date deliveryCutoffTime) {
		this.deliveryCutoffTime = deliveryCutoffTime;
	}
	public Date getRoutingStartTime() {
		return routingStartTime;
	}
	public void setRoutingStartTime(Date routingStartTime) {
		this.routingStartTime = routingStartTime;
	}
	public Date getRoutingEndTime() {
		return routingEndTime;
	}
	public void setRoutingEndTime(Date routingEndTime) {
		this.routingEndTime = routingEndTime;
	}
	public Date getDeliveryETAStartTime() {
		return deliveryETAStartTime;
	}
	public void setDeliveryETAStartTime(Date deliveryETAStartTime) {
		this.deliveryETAStartTime = deliveryETAStartTime;
	}
	public Date getDeliveryETAEndTime() {
		return deliveryETAEndTime;
	}
	public void setDeliveryETAEndTime(Date deliveryETAEndTime) {
		this.deliveryETAEndTime = deliveryETAEndTime;
	}	
}
