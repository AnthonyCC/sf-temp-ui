package com.freshdirect.mobileapi.controller.data.response;

import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.delivery.EnumDeliveryStatus;
import com.freshdirect.mobileapi.controller.data.Message;

public class Visitor extends Message {
	
	private String zipCode;
	private EnumServiceType serviceType;
	private EnumDeliveryStatus deliveryStatus;
	private String address1;
	private String apartment;
	private String city;
	private String state;
	
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getApartment() {
		return apartment;
	}
	public void setApartment(String apartment) {
		this.apartment = apartment;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public EnumServiceType getServiceType() {
		return serviceType;
	}
	public void setServiceType(EnumServiceType serviceType) {
		this.serviceType = serviceType;
	}
	public EnumDeliveryStatus getDeliveryStatus() {
		return deliveryStatus;
	}
	public void setDeliveryStatus(EnumDeliveryStatus deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}

}
