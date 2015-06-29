package com.freshdirect.webapp.ajax.expresscheckout.location.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LocationData {

	private String id;

	private boolean selected;

	@JsonProperty("service_type")
	private String serviceType;

	private String name;

	@JsonProperty("street_address")
	private String address1;
	
	@JsonProperty("street_address2")
	private String address2;

	private String apartment;

	private String city;

	private String state;

	private String zip;

	private String phone;

	@JsonProperty("phone_type")
	private String phoneType;

	@JsonProperty("phone_ext")
	private String phoneExtension;

	@JsonProperty("alt_phone")
	private String alternativePhone;

	@JsonProperty("alt_phone_type")
	private String alternativePhoneType;

	@JsonProperty("alt_phone_ext")
	private String alternativePhoneExtension;

	private String instructions;
	
	@JsonProperty("can_delete")
	private boolean canDelete = true;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
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

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPhoneType() {
		return phoneType;
	}

	public void setPhoneType(String phoneType) {
		this.phoneType = phoneType;
	}

	public String getPhoneExtension() {
		return phoneExtension;
	}

	public void setPhoneExtension(String phoneExtension) {
		this.phoneExtension = phoneExtension;
	}

	public String getAlternativePhone() {
		return alternativePhone;
	}

	public void setAlternativePhone(String alternativePhone) {
		this.alternativePhone = alternativePhone;
	}

	public String getAlternativePhoneType() {
		return alternativePhoneType;
	}

	public void setAlternativePhoneType(String alternativePhoneType) {
		this.alternativePhoneType = alternativePhoneType;
	}

	public String getAlternativePhoneExtension() {
		return alternativePhoneExtension;
	}

	public void setAlternativePhoneExtension(String alternativePhoneExtension) {
		this.alternativePhoneExtension = alternativePhoneExtension;
	}

	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	public boolean getCanDelete() {
		return canDelete;
	}

	public void setCanDelete(boolean canDelete) {
		this.canDelete = canDelete;
	}

}
