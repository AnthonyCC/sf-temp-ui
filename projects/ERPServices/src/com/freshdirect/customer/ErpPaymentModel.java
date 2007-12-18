package com.freshdirect.customer;

import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.payment.EnumBankAccountType;
import com.freshdirect.payment.EnumPaymentMethodType;

/**
 * @stereotype fd-model
 */
public class ErpPaymentModel extends ErpTransactionModel {
	
    private double amount;
    private double tax;

	private String ccNumLast4;
	private EnumCardType cardType;
	// for eChecks
	private EnumPaymentMethodType paymentMethodType;
	private String abaRouteNumber;
	private EnumBankAccountType bankAccountType;
    private boolean isChargePayment;
    private ErpAffiliate affiliate;
    
    public ErpPaymentModel(EnumTransactionType txType) {
		super(txType);
    }

	public double getAmount(){
		return this.amount;
	}
	
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	public double getTax(){
		return this.tax;
	}
	public void setTax(double tax){
		this.tax = tax;
	}
	
	public double getPreTaxAmount(){
		return this.amount - this.tax;
	}

	public String getCcNumLast4(){
		return this.ccNumLast4;
	}
	public void setCcNumLast4(String ccNumLast4){
		this.ccNumLast4 = ccNumLast4;
	}
	
	public EnumCardType getCardType(){
		return this.cardType;
	}
	public void setCardType(EnumCardType cardType){
		this.cardType = cardType;
	}

	public EnumPaymentMethodType getPaymentMethodType(){
		return this.paymentMethodType;
	}
	public void setPaymentMethodType(EnumPaymentMethodType paymentMethodType){
		this.paymentMethodType = paymentMethodType;
	}

	public String getAbaRouteNumber(){
		return this.abaRouteNumber;
	}
	public void setAbaRouteNumber(String abaRouteNumber){
		this.abaRouteNumber = abaRouteNumber;
	}

	public EnumBankAccountType getBankAccountType(){
		return this.bankAccountType;
	}
	public void setBankAccountType(EnumBankAccountType bankAccountType){
		this.bankAccountType = bankAccountType;
	}
	
	public boolean getIsChargePayment(){
		return this.isChargePayment;
	}
	public void setIsChargePayment(boolean isChargePayment){
		this.isChargePayment = isChargePayment;
	}
	
	public ErpAffiliate getAffiliate() {
		return this.affiliate;
	}
	
	public void setAffiliate(ErpAffiliate affiliate) {
		this.affiliate = affiliate;
	}
	
}
