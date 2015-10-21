package com.freshdirect.webapp.ajax.expresscheckout.payment.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PaymentEditData {

	private String id;
	private String cardHolderName;
	private String cardBrand;
	private String cardNum;
	private String cardMonth;
	private String cardYear;
	
	@JsonProperty("bil_apartment")
	private String apartment;
	private String paymentMethodType;
	private String csv;
	private String abaRouteNumber;
	private String bankName;
	private String bankAccountType;

	@JsonProperty("bil_country")
	private String billingCountry;

	@JsonProperty("bil_address1")
	private String billingAddress1;

	@JsonProperty("bil_address2")
	private String billingAddress2;

	@JsonProperty("bil_city")
	private String billingCity;

	@JsonProperty("bil_state")
	private String billingState;

	@JsonProperty("bil_zipcode")
	private String billingZipcode;

	private String phone;
	private String eWalletID;

	public String getCardHolderName() {
		return cardHolderName;
	}

	public void setCardHolderName(String cardHolderName) {
		this.cardHolderName = cardHolderName;
	}

	public String getCardBrand() {
		return cardBrand;
	}

	public void setCardBrand(String cardBrand) {
		this.cardBrand = cardBrand;
	}

	public String getCardNum() {
		return cardNum;
	}

	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}

	public String getCardMonth() {
		return cardMonth;
	}

	public void setCardMonth(String cardMonth) {
		this.cardMonth = cardMonth;
	}

	public String getCardYear() {
		return cardYear;
	}

	public void setCardYear(String cardYear) {
		this.cardYear = cardYear;
	}

	public String getBillingCountry() {
		return billingCountry;
	}

	public void setBillingCountry(String billingCountry) {
		this.billingCountry = billingCountry;
	}

	public String getBillingAddress1() {
		return billingAddress1;
	}

	public void setBillingAddress1(String billingAddress1) {
		this.billingAddress1 = billingAddress1;
	}

	public String getApartment() {
		return apartment;
	}

	public void setApartment(String apartment) {
		this.apartment = apartment;
	}

	public String getBillingAddress2() {
		return billingAddress2;
	}

	public void setBillingAddress2(String billingAddress2) {
		this.billingAddress2 = billingAddress2;
	}

	public String getBillingCity() {
		return billingCity;
	}

	public void setBillingCity(String billingCity) {
		this.billingCity = billingCity;
	}

	public String getBillingState() {
		return billingState;
	}

	public void setBillingState(String billingState) {
		this.billingState = billingState;
	}

	public String getBillingZipcode() {
		return billingZipcode;
	}

	public void setBillingZipcode(String billingZipcode) {
		this.billingZipcode = billingZipcode;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPaymentMethodType() {
		return paymentMethodType;
	}

	public void setPaymentMethodType(String paymentMethodType) {
		this.paymentMethodType = paymentMethodType;
	}

	public String getCsv() {
		return csv;
	}

	public void setCsv(String csv) {
		this.csv = csv;
	}

	public String getAbaRouteNumber() {
		return abaRouteNumber;
	}

	public void setAbaRouteNumber(String abaRouteNumber) {
		this.abaRouteNumber = abaRouteNumber;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankAccountType() {
		return bankAccountType;
	}

	public void setBankAccountType(String bankAccountType) {
		this.bankAccountType = bankAccountType;
	}

	/**
	 * @return the eWalletID
	 */
	public String geteWalletID() {
		return eWalletID;
	}

	/**
	 * @param eWalletID the eWalletID to set
	 */
	public void seteWalletID(String eWalletID) {
		this.eWalletID = eWalletID;
	}

}
