package com.freshdirect.mobileapi.controller.data.request;

import com.freshdirect.mobileapi.controller.data.Message;

public class PaymentMethodRequest extends Message {
	
/*	CARD_EXP_MONTH
	CARD_EXP_YEAR
	CARD_BRAND
	ACCOUNT_NUMBER
	ABA_ROUTE_NUMBER
	BANK_NAME	
	BYPASS_BAD_ACCOUNT_CHECK
	TERMS
*/
	private String cardExpMonth;
	private String cardExpYear;
	private String cardBrand;
	private String accountNumber;
	private String abaRouteNumber;
	private String bankName;
	private String terms;
	private String accountNumberVerify;
	private String bankAccountType;
	private String accountHolder;
	private String billAddress1;
	private String billAddress2;
	private String billApt;
	private String billCity;
	private String billState;
	private String billZipCode;
	private String paymentMethodType;
	private String paymentMethodId;
	private String billingRef;
	
	public String getCardExpMonth() {
		return cardExpMonth;
	}
	public void setCardExpMonth(String cardExpMonth) {
		this.cardExpMonth = cardExpMonth;
	}
	public String getCardExpYear() {
		return cardExpYear;
	}
	public void setCardExpYear(String cardExpYear) {
		this.cardExpYear = cardExpYear;
	}
	public String getCardBrand() {
		return cardBrand;
	}
	public void setCardBrand(String cardBrand) {
		this.cardBrand = cardBrand;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
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
	public String getTerms() {
		return terms;
	}
	public void setTerms(String terms) {
		this.terms = terms;
	}
	public String getAccountNumberVerify() {
		return accountNumberVerify;
	}
	public void setAccountNumberVerify(String accountNumberVerify) {
		this.accountNumberVerify = accountNumberVerify;
	}
	public String getBankAccountType() {
		return bankAccountType;
	}
	public void setBankAccountType(String bankAccountType) {
		this.bankAccountType = bankAccountType;
	}
	public String getAccountHolder() {
		return accountHolder;
	}
	public void setAccountHolder(String accountHolder) {
		this.accountHolder = accountHolder;
	}
	public String getBillAddress1() {
		return billAddress1;
	}
	public void setBillAddress1(String billAddress1) {
		this.billAddress1 = billAddress1;
	}
	public String getBillAddress2() {
		return billAddress2;
	}
	public void setBillAddress2(String billAddress2) {
		this.billAddress2 = billAddress2;
	}
	public String getBillApt() {
		return billApt;
	}
	public void setBillApt(String billApt) {
		this.billApt = billApt;
	}
	public String getBillCity() {
		return billCity;
	}
	public void setBillCity(String billCity) {
		this.billCity = billCity;
	}
	public String getBillState() {
		return billState;
	}
	public void setBillState(String billState) {
		this.billState = billState;
	}
	public String getBillZipCode() {
		return billZipCode;
	}
	public void setBillZipCode(String billZipCode) {
		this.billZipCode = billZipCode;
	}
	public String getPaymentMethodType() {
		return paymentMethodType;
	}
	public void setPaymentMethodType(String paymentMethodType) {
		this.paymentMethodType = paymentMethodType;
	}
	public String getPaymentMethodId() {
		return paymentMethodId;
	}
	public void setPaymentMethodId(String paymentMethodId) {
		this.paymentMethodId = paymentMethodId;
	}
	public String getBillingRef() {
		return billingRef;
	}
	public void setBillingRef(String billingRef) {
		this.billingRef = billingRef;
	}
	

}
