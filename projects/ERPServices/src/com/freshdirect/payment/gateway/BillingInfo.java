package com.freshdirect.payment.gateway;

import com.freshdirect.fdstore.EnumEStoreId;



public interface BillingInfo extends java.io.Serializable {
	
	
	public double getAmount();
	public void setAmount(double amount);
	public double getTax();
	public void setTax(double tax);
	
	public Currency getCurrency();
	public Merchant getMerchant();
	public PaymentMethod getPaymentMethod();
	
	public String getTransactionID();
	public void setTransactionID(String orderID);
	public String getTransactionRef();
	public void setTransactionRef(String transactionRef);
	public String getTransactionRefIndex();
	public void setTransactionRefIndex(String transactionRefIndex);
	
	public String getEwalletId();
	public void setEwalletId(String ewalletId);
	public String getEwalletTxId();
	public void setEwalletTxId(String ewalletId);
	public void setEStoreId(EnumEStoreId eStore);
	public EnumEStoreId getEStoreId();
	public String getVendorEwalletId();
	public void setVendorEwalletId(String vendorEwalletId);
	
	public String getEmailID();
	
	public void setEmailID(String emailID);
}
