package com.freshdirect.fdstore.ewallet;

import com.freshdirect.fdstore.ewallet.PaymentData;
import com.freshdirect.fdstore.ewallet.impl.MasterpassData;
import com.freshdirect.payment.Result;
import com.mastercard.mcwallet.sdk.xml.allservices.MerchantTransactions;

public class MasterPassGatewayResponse extends Result  implements java.io.Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private MasterpassData masterpassData;
	
	private PaymentData paymentData;
	
	private MerchantTransactions merchantTransactions;

	private String responseTime;
	
	private String statusMessage;
	
	private String statusCode;

	private boolean isError;

	

	public String getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(String responseTime) {
		this.responseTime = responseTime;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public boolean isError() {
		return isError;
	}

	public void setError(boolean isError) {
		this.isError = isError;
	}

	public PaymentData getPaymentData() {
		return paymentData;
	}

	public void setPaymentData(PaymentData paymentData) {
		this.paymentData = paymentData;
	}

	public MasterpassData getMasterpassData() {
		return masterpassData;
	}

	public void setMasterpassData(MasterpassData masterpassData) {
		this.masterpassData = masterpassData;
	}

	public MerchantTransactions getMerchantTransactions() {
		return merchantTransactions;
	}

	public void setMerchantTransactions(MerchantTransactions merchantTransactions) {
		this.merchantTransactions = merchantTransactions;
	}
	

}
