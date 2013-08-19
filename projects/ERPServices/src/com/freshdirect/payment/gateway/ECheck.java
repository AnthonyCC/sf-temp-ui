package com.freshdirect.payment.gateway;

public interface ECheck extends PaymentMethod {
	public String getRoutingNumber();
	public void setRoutingNumber(String routingNumber);
	public BankAccountType getBankAccountType();
	public void setBankAccountType(BankAccountType bankAccountType);
}
