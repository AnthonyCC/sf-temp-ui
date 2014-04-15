package com.freshdirect.routing.model;

import java.util.Date;

public interface IDeliveryModel  {
	
	Date getDeliveryDate();
	void setDeliveryDate(Date deliveryDate);
	
	Date getDeliveryEndTime();
	void setDeliveryEndTime(Date deliveryEndTime);
	
	String getDeliveryModel();
	void setDeliveryModel(String deliveryModel);
	
	Date getDeliveryStartTime();
	void setDeliveryStartTime(Date deliveryStartTime);
	
	Date getRoutingEndTime();
	void setRoutingEndTime(Date routingEndTime);
	
	Date getRoutingStartTime();
	void setRoutingStartTime(Date routingStartTime);
	
	IZoneModel getDeliveryZone();
	void setDeliveryZone(IZoneModel deliveryZone);	
	
	ILocationModel getDeliveryLocation();
	void setDeliveryLocation(ILocationModel location);
	
	IPackagingModel getPackagingDetail();
	void setPackagingDetail(IPackagingModel packagingDetail);
	
	IServiceTime getCalculatedServiceTime();
	double getCalculatedOrderSize();
	
	void setCalculatedServiceTime(IServiceTime calculatedServiceTime);
	void setCalculatedOrderSize(double calculatedOrderSize);
	
	String getReservationId();
	void setReservationId(String reservationId);
	
	String getServiceType();
	void setServiceType(String serviceType);
	
	int getReservedOrdersAtBuilding();
	void setReservedOrdersAtBuilding(int reservedOrdersAtBuilding);
	
	Date getDeliveryCutoffTime();
	void setDeliveryCutoffTime(Date deliveryCutoffTime);
	
	Date getDeliveryETAStartTime();
	void setDeliveryETAStartTime(Date deliveryETAStartTime);
	
	Date getDeliveryETAEndTime();
	void setDeliveryETAEndTime(Date deliveryETAEndTime);
}
