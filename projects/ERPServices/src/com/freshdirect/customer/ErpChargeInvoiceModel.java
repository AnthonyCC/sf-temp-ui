/*
 * Created on Mar 30, 2005
 *
 */
package com.freshdirect.customer;

/**
 * @author jng
 */

public class ErpChargeInvoiceModel extends ErpAbstractInvoiceModel {
	
	private double subTotal;
	
    public ErpChargeInvoiceModel() {
		super(EnumTransactionType.INVOICE_CHARGE);
    }
    
    public ErpChargeInvoiceModel(EnumTransactionType transType){
    	super(transType);
    }

	public double getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(double subTotal) {
		this.subTotal = subTotal;
	}
    	
}
