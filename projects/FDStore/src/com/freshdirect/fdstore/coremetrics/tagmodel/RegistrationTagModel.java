package com.freshdirect.fdstore.coremetrics.tagmodel;


public class RegistrationTagModel extends AbstractTagModel  {
	private String registrationId; 
	private String registrantEmail; 
	private String registrantCity;
	private String registrantState;
	private String registrantPostalCode;
	private String registrantCountry;
	
	public String getRegistrationId() {
		return registrationId;
	}
	
	public void setRegistrationId(String registrationId) {
		this.registrationId = registrationId;
	}
	
	public String getRegistrantEmail() {
		return registrantEmail;
	}
	
	public void setRegistrantEmail(String registrantEmail) {
		this.registrantEmail = registrantEmail;
	}
	
	public String getRegistrantCity() {
		return registrantCity;
	}
	
	public void setRegistrantCity(String registrantCity) {
		this.registrantCity = registrantCity;
	}
	
	public String getRegistrantState() {
		return registrantState;
	}
	
	public void setRegistrantState(String registrantState) {
		this.registrantState = registrantState;
	}
	
	public String getRegistrantPostalCode() {
		return registrantPostalCode;
	}
	
	public void setRegistrantPostalCode(String registrantPostalCode) {
		this.registrantPostalCode = registrantPostalCode;
	}
	
	public String getRegistrantCountry() {
		return registrantCountry;
	}

	public void setRegistrantCountry(String registrantCountry) {
		this.registrantCountry = registrantCountry;
	}
}