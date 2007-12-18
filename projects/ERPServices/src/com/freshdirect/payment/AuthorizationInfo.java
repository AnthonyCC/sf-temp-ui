package com.freshdirect.payment;

import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.customer.ErpPaymentMethodI;

public class AuthorizationInfo {

	private final String saleId;
	private final String customerId;
	private final ErpAffiliate affiliate;
	private final double amount;
	private final ErpPaymentMethodI paymentMethod;
	private final boolean additionalCharge;
	
	public AuthorizationInfo(String saleId, String customerId, ErpAffiliate affiliate, double amount, ErpPaymentMethodI paymentMethod, boolean additionalCharge) {
		this.saleId = saleId;
		this.customerId = customerId;
		this.affiliate = affiliate;
		this.amount = amount;
		this.paymentMethod = paymentMethod;
		this.additionalCharge = additionalCharge;
	}
	
	public String getSaleId() {
		return this.saleId;
	}
	
	public String getCustomerId() {
		return this.customerId;
	}
	
	public String getMerchantId() {
		return this.affiliate.getMerchant(this.paymentMethod.getCardType());
	}

	public double getAmount() {
		return this.amount;
	}
	
	public ErpPaymentMethodI getPaymentMethod() {
		return this.paymentMethod;
	}
	
	public boolean isAdditionalCharge() {
		return this.additionalCharge;
	}
	
	public ErpAffiliate getAffiliate() {
		return this.affiliate;
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer("AuthorizationInfo[saleId: ").append(this.saleId);
		buf.append(" customerId: ").append(this.customerId);
		buf.append(" affiliate: ").append(this.affiliate);
		buf.append(" amount: ").append(this.amount);
		buf.append(" paymentMethod: ");
		if(this.paymentMethod != null) {
			buf.append(this.paymentMethod.getCardType());
		}else{
			buf.append("null");
		}
		buf.append(" additionalCharge: ").append(this.additionalCharge).append("]");
		
		return buf.toString();
	}
}