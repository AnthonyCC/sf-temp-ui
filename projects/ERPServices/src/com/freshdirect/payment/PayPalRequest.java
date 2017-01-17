package com.freshdirect.payment;



public class PayPalRequest {


	
	private String orderNumber;
	private String authorizationAmount;
	private String tax;
	private String merchantId;
	private String merchentEmailId;
	private String token; 
	private String customerId;

	private String taxId;
	private String customerFirstName;
	private String customerLastName;
	
	private String paymentMethodNonce;
	private String deviceId;
	private String profileId;

	public String getProfileId() {
		return profileId;
	}
	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}
	public String getPaymentMethodNonce() {
		return paymentMethodNonce;
	}
	public void setPaymentMethodNonce(String paymentMethodNonce) {
		this.paymentMethodNonce = paymentMethodNonce;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}


	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public String getAuthorizationAmount() {
		return authorizationAmount;
	}
	public void setAuthorizationAmount(String authorizationAmount) {
		this.authorizationAmount = authorizationAmount;
	}
	public String getTax() {
		return tax;
	}
	public void setTax(String tax) {
		this.tax = tax;
	}
	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	public String getMerchentEmailId() {
		return merchentEmailId;
	}
	public void setMerchentEmailId(String merchentEmailId) {
		this.merchentEmailId = merchentEmailId;
	}


	public String getTaxId() {
		return taxId;
	}
	public void setTaxId(String taxId) {
		this.taxId = taxId;
	}
	public String getCustomerFirstName() {
		return customerFirstName;
	}
	public void setCustomerFirstName(String customerFirstName) {
		this.customerFirstName = customerFirstName;
	}
	public String getCustomerLastName() {
		return customerLastName;
	}
	public void setCustomerLastName(String customerLastName) {
		this.customerLastName = customerLastName;
	}




}
