package com.freshdirect.giftcard;

import java.util.Date;
import java.util.List;

import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.customer.EnumTransactionType;
import com.freshdirect.customer.ErpTransactionModel;
import com.freshdirect.payment.EnumBankAccountType;
import com.freshdirect.payment.EnumGiftCardTransactionStatus;
import com.freshdirect.payment.EnumGiftCardTransactionType;
import com.freshdirect.payment.EnumPaymentMethodType;

/**
 * @stereotype fd-model
 */
public class ErpGiftCardTransModel extends ErpTransactionModel {
	
    private double amount;
    private double tax;
	
	    
    public ErpGiftCardTransModel(EnumTransactionType txType) {
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

	
	
}
