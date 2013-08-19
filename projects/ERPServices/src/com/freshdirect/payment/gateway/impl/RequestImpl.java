package com.freshdirect.payment.gateway.impl;

import com.freshdirect.payment.gateway.BillingInfo;
import com.freshdirect.payment.gateway.Request;
import com.freshdirect.payment.gateway.TransactionType;

class RequestImpl implements Request {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6703283523906995517L;
	private BillingInfo billingInfo;
	private TransactionType transactionType;
	
	public RequestImpl(TransactionType transactionType) {
		this.transactionType=transactionType;
	}
	public BillingInfo getBillingInfo() {
		
		return billingInfo;
	}

	
	public TransactionType getTransactionType() {
		return transactionType;
	}

	
	public void setBillingInfo(BillingInfo info) {
		billingInfo=info;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((billingInfo == null) ? 0 : billingInfo.hashCode());
		result = prime * result
				+ ((transactionType == null) ? 0 : transactionType.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof RequestImpl))
			return false;
		RequestImpl other = (RequestImpl) obj;
		if (billingInfo == null) {
			if (other.billingInfo != null)
				return false;
		} else if (!billingInfo.equals(other.billingInfo))
			return false;
		if (transactionType == null) {
			if (other.transactionType != null)
				return false;
		} else if (!transactionType.equals(other.transactionType))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "RequestImpl [billingInfo=" + billingInfo + ", transactionType="
				+ transactionType + "]";
	}

	
}
