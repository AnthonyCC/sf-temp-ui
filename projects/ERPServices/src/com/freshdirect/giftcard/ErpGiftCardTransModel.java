package com.freshdirect.giftcard;

import com.freshdirect.customer.EnumTransactionType;
import com.freshdirect.customer.ErpTransactionModel;

public class ErpGiftCardTransModel extends ErpTransactionModel {
	
	private static final long	serialVersionUID	= 7900833703399393453L;
	
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
