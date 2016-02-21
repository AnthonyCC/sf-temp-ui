package com.freshdirect.payment.ejb;


public class PaymentMethodData {


	public PaymentMethodData(String accountNumber, String paymentMethodType) {
		super();
		this.accountNumber = accountNumber;
		this.paymentMethodType = paymentMethodType;
	}

	private String accountNumber;
	private String paymentMethodType;

	public void setAccountNumber(String number) {
		this.accountNumber = number;
	}
	
	public String getAccountNumber() {
		return this.accountNumber;
	}

	public String getPaymentMethodType() {
		return paymentMethodType;
	}

	public void setPaymentMethodType(String paymentMethodType) {
		this.paymentMethodType = paymentMethodType;
	}
	
	

}
