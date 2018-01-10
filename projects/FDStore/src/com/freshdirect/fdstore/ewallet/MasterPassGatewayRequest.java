package com.freshdirect.fdstore.ewallet;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.freshdirect.fdstore.ewallet.PaymentData;
import com.freshdirect.fdstore.ewallet.impl.MasterpassData;
import com.mastercard.mcwallet.sdk.xml.allservices.MerchantTransactions;

public class MasterPassGatewayRequest implements java.io.Serializable {
	
	
private MasterpassData masterpassData;
private PaymentData paymentData;
private String shoppingCartRequestasXML;
private MerchantTransactions merchantTransactions;

public MasterpassData getMasterpassData() {
	return masterpassData;
}
public void setMasterpassData(MasterpassData masterpassData) {
	this.masterpassData = masterpassData;
}
public PaymentData getPaymentData() {
	return paymentData;
}
public void setPaymentData(PaymentData paymentData) {
	this.paymentData = paymentData;
}
public String getShoppingCartRequestasXML() {
	return shoppingCartRequestasXML;
}
public void setShoppingCartRequestasXML(String shoppingCartRequestasXML) {
	this.shoppingCartRequestasXML = shoppingCartRequestasXML;
}
public MerchantTransactions getMerchantTransactions() {
	return merchantTransactions;
}
public void setMerchantTransactions(MerchantTransactions merchantTransactions) {
	this.merchantTransactions = merchantTransactions;
}



}
