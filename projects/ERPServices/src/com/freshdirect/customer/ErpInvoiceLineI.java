package com.freshdirect.customer;

import java.io.Serializable;

public interface ErpInvoiceLineI extends Serializable {
	
	public double getQuantity();

	public double getPrice();

	public double getWeight();

	public double getTaxValue();

	public double getDepositValue();

	public double getCustomizationPrice();

	public String getOrderLineNumber();
	
	public double getActualDiscountAmount();
	
	public double getCouponDiscountAmount() ;
	
	public String getSubstitutedSkuCode(); // return the skucode of the substituted product from SAP
	
	public String getSubSkuStatus(); // return the status of the substituted skucode
	
	public String getMaterialNumber();
	
	public double getActualCost();
	
	public double getActualPrice();
	
	
}