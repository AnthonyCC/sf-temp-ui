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
	

}