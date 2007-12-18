package com.freshdirect.customer;

public class ErpCancelOrderModel extends ErpTransactionModel{
	
	double amount;
	
	public ErpCancelOrderModel(){
		super(EnumTransactionType.CANCEL_ORDER);
		this.amount = 0.0;
	}
	
	public double getAmount(){
		return this.amount;
	}
	
	public void setAmount(double amount){
		this.amount = amount;
	}

}
