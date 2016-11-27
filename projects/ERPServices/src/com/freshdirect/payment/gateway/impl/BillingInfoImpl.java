package com.freshdirect.payment.gateway.impl;

import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.payment.gateway.BillingInfo;
import com.freshdirect.payment.gateway.Currency;
import com.freshdirect.payment.gateway.Merchant;
import com.freshdirect.payment.gateway.PaymentMethod;

class BillingInfoImpl implements BillingInfo{

	private double amount;
	private double tax;
	private String transactionID;
	private String transactionRef;
	private String transactionRefIndex;
	private Currency currency=Currency.USD;
	private Merchant merchant;
	private PaymentMethod pymtMethod;
	private String ewalletId;
	private String ewalletTxId;	
	private EnumEStoreId eStore;
	private String vendorEwalletId;
	private String emailID;
	/**
	 * @return the ewalletTxId
	 */
	public String getEwalletTxId() {
		return ewalletTxId;
	}

	/**
	 * @param ewalletTxId the ewalletTxId to set
	 */
	public void setEwalletTxId(String ewalletTxId) {
		this.ewalletTxId = ewalletTxId;
	}

	public BillingInfoImpl(Merchant merchant,PaymentMethod pymtMethod) {
		this.merchant=merchant;
		this.pymtMethod=pymtMethod;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getTax() {
		return tax;
	}

	public void setTax(double tax) {
		this.tax = tax;
	}

	public String getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}

	public String getTransactionRef() {
		return transactionRef;
	}

	public void setTransactionRef(String transactionRef) {
		this.transactionRef = transactionRef;
	}
	public String getTransactionRefIndex() {
		return transactionRefIndex;
	}

	public void setTransactionRefIndex(String transactionRefIndex) {
		this.transactionRefIndex = transactionRefIndex;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public Merchant getMerchant() {
		return merchant;
	}

	

	public PaymentMethod getPaymentMethod() {
		return pymtMethod;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(amount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result
				+ ((currency == null) ? 0 : currency.hashCode());
		result = prime * result
				+ ((merchant == null) ? 0 : merchant.hashCode());
		result = prime * result
				+ ((pymtMethod == null) ? 0 : pymtMethod.hashCode());
		temp = Double.doubleToLongBits(tax);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result
				+ ((transactionID == null) ? 0 : transactionID.hashCode());
		result = prime * result
				+ ((transactionRef == null) ? 0 : transactionRef.hashCode());
		result = prime * result
		+ ((transactionRefIndex == null) ? 0 : transactionRefIndex.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof BillingInfoImpl))
			return false;
		BillingInfoImpl other = (BillingInfoImpl) obj;
		if (Double.doubleToLongBits(amount) != Double
				.doubleToLongBits(other.amount))
			return false;
		if (currency == null) {
			if (other.currency != null)
				return false;
		} else if (!currency.equals(other.currency))
			return false;
		if (merchant == null) {
			if (other.merchant != null)
				return false;
		} else if (!merchant.equals(other.merchant))
			return false;
		if (pymtMethod == null) {
			if (other.pymtMethod != null)
				return false;
		} else if (!pymtMethod.equals(other.pymtMethod))
			return false;
		if (Double.doubleToLongBits(tax) != Double.doubleToLongBits(other.tax))
			return false;
		if (transactionID == null) {
			if (other.transactionID != null)
				return false;
		} else if (!transactionID.equals(other.transactionID))
			return false;
		if (transactionRef == null) {
			if (other.transactionRef != null)
				return false;
		} else if (!transactionRef.equals(other.transactionRef))
			return false;
		if (transactionRefIndex == null) {
			if (other.transactionRefIndex != null)
				return false;
		} else if (!transactionRefIndex.equals(other.transactionRefIndex))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "BillingInfoImpl [amount=" + amount + ", currency=" + currency
				+ ", merchant=" + merchant + ", pymtMethod=" + pymtMethod
				+ ", tax=" + tax + ", transactionID=" + transactionID
				+ ", transactionRef=" + transactionRef 
				+ ", transactionRefIndex=" + transactionRefIndex +"]";
	}

	@Override
	public String getEwalletId() {
		return this.ewalletId;
	}

	@Override
	public void setEwalletId(String ewalletId) {
		this.ewalletId = ewalletId;
		
	}

	@Override
	public void setEStoreId(EnumEStoreId eStore) {
		this.eStore=eStore;
		
	}

	@Override
	public EnumEStoreId getEStoreId() {
		
		return eStore;
	}
	@Override
	public String getVendorEwalletId() {
		return vendorEwalletId;
	}

	@Override
	public void setVendorEwalletId(String vendorEwalletId) {
		this.vendorEwalletId=vendorEwalletId;
		
	}

	@Override
	public String getEmailID() {
		return emailID;
	}

	@Override
	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}

}
