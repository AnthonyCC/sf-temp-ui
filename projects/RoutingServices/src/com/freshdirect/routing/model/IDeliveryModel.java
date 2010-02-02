package com.freshdirect.routing.model;

import java.util.Date;

import com.freshdirect.framework.util.TimeOfDay;

public interface IDeliveryModel  {
	
	Date getDeliveryDate();
	void setDeliveryDate(Date deliveryDate);
	
	Date getDeliveryEndTime();
	void setDeliveryEndTime(Date deliveryEndTime);
	
	String getDeliveryModel();
	void setDeliveryModel(String deliveryModel);
	
	Date getDeliveryStartTime();
	void setDeliveryStartTime(Date deliveryStartTime);
	
	IZoneModel getDeliveryZone();
	void setDeliveryZone(IZoneModel deliveryZone);	
	
	ILocationModel getDeliveryLocation();
	void setDeliveryLocation(ILocationModel location);
	
	IPackagingModel getPackagingInfo();
	void setPackagingInfo(IPackagingModel packageInfo);
	
	double getServiceTime();
	void setServiceTime(double serviceTime);
	
	double getOrderSize();
	void setOrderSize(double orderSize);
	
	String getReservationId();
	void setReservationId(String reservationId);
	
}
