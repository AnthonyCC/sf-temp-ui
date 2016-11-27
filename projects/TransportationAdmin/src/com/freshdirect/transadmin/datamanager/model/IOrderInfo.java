package com.freshdirect.transadmin.datamanager.model;

import java.io.Serializable;
import java.util.Date;

public interface IOrderInfo extends Serializable {
	
	String getAlternateReceiver();
	void setAlternateReceiver(String alternateReceiver);
	String getApartmentNumber();
	void setApartmentNumber(String apartmentNumber);
	String getCity();
	void setCity(String city);
	String getCustomerName();
	void setCustomerName(String customerName);
	String getCustomerNumber();
	void setCustomerNumber(String customerNumber);
	Date getDeliveryDate();
	void setDeliveryDate(Date deliveryDate);
	Date getDeliveryEndTime();
	void setDeliveryEndTime(Date deliveryEndTime);
	String getDeliveryModel();
	void setDeliveryModel(String deliveryModel);
	Date getDeliveryStartTime();
	void setDeliveryStartTime(Date deliveryStartTime);
	String getDeliveryZone();
	void setDeliveryZone(String deliveryZone);
	String getOrderNumber();
	void setOrderNumber(String orderNumber);
	String getPlant();
	void setPlant(String plant);
	String getState();
	void setState(String state);
	String getStreetAddress1();
	void setStreetAddress1(String streetAddress1);
	String getStreetAddress2();
	void setStreetAddress2(String streetAddress2);
	String getZipCode();
	void setZipCode(String zipCode);
}
