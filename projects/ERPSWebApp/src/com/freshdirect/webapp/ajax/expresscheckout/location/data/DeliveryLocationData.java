package com.freshdirect.webapp.ajax.expresscheckout.location.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DeliveryLocationData extends LocationData {

	@JsonProperty("company_name")
	private String companyName;

	@JsonProperty("nick_name")
	private String nickName;

	@JsonProperty("first_name")
	private String firstName;

	@JsonProperty("last_name")
	private String lastName;

	@JsonProperty("bd_auth")
	private String backupDeliveryAuthenticate;

	@JsonProperty("bd_address")
	private String backupDeliveryAddress;

	@JsonProperty("bd_apartment")
	private String backupDeliveryApartment;

	@JsonProperty("bd_first_name")
	private String backupDeliveryFirstName;

	@JsonProperty("bd_last_name")
	private String backupDeliveryLastName;

	@JsonProperty("bd_phone")
	private String backupDeliveryPhone;

	@JsonProperty("bd_phone_ext")
	private String backupDeliveryPhoneExtension;

	@JsonProperty("bd_instructions")
	private String backupDeliveryInstructions;

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getBackupDeliveryAuthenticate() {
		return backupDeliveryAuthenticate;
	}

	public void setBackupDeliveryAuthenticate(String backupDeliveryAuthenticate) {
		this.backupDeliveryAuthenticate = backupDeliveryAuthenticate;
	}

	public String getBackupDeliveryAddress() {
		return backupDeliveryAddress;
	}

	public void setBackupDeliveryAddress(String backupDeliveryAddress) {
		this.backupDeliveryAddress = backupDeliveryAddress;
	}

	public String getBackupDeliveryApartment() {
		return backupDeliveryApartment;
	}

	public void setBackupDeliveryApartment(String backupDeliveryApartment) {
		this.backupDeliveryApartment = backupDeliveryApartment;
	}

	public String getBackupDeliveryFirstName() {
		return backupDeliveryFirstName;
	}

	public void setBackupDeliveryFirstName(String backupDeliveryFirstName) {
		this.backupDeliveryFirstName = backupDeliveryFirstName;
	}

	public String getBackupDeliveryLastName() {
		return backupDeliveryLastName;
	}

	public void setBackupDeliveryLastName(String backupDeliveryLastName) {
		this.backupDeliveryLastName = backupDeliveryLastName;
	}

	public String getBackupDeliveryPhone() {
		return backupDeliveryPhone;
	}

	public void setBackupDeliveryPhone(String backupDeliveryPhone) {
		this.backupDeliveryPhone = backupDeliveryPhone;
	}

	public String getBackupDeliveryPhoneExtension() {
		return backupDeliveryPhoneExtension;
	}

	public void setBackupDeliveryPhoneExtension(String backupDeliveryPhoneExtension) {
		this.backupDeliveryPhoneExtension = backupDeliveryPhoneExtension;
	}

	public String getBackupDeliveryInstructions() {
		return backupDeliveryInstructions;
	}

	public void setBackupDeliveryInstructions(String backupDeliveryInstructions) {
		this.backupDeliveryInstructions = backupDeliveryInstructions;
	}

}
