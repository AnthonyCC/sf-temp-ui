package com.freshdirect.payment.gateway;

public interface Request extends java.io.Serializable{

	public TransactionType getTransactionType();
	public BillingInfo getBillingInfo();
	public void setBillingInfo(BillingInfo info);
}
