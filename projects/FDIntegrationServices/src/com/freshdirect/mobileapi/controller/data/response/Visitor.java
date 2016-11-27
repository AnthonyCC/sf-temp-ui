package com.freshdirect.mobileapi.controller.data.response;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.mobileapi.controller.data.Message;

public class Visitor extends Message {
	
	private String zipCode;
	private EnumServiceType serviceType;
	private String deliveryStatus;
	private String address1;
	private String apartment;
	private String city;
	private String state;
	private Set availableServiceTypes;
	public Set getAvailableServiceTypes() {
		return availableServiceTypes;
	}
	public void setAvailableServiceTypes(Set availableServiceTypes) {
		this.availableServiceTypes = availableServiceTypes;
	}
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
	public String getDeliveryStatus() {
		return deliveryStatus;
	}
	public void setDeliveryStatus(String deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}

}
