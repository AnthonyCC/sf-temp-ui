/*
 * 
 * ErpResubmitPaymentModel.java
 * Date June 07/2002 10:45
 */
 
package com.freshdirect.customer;

/**
 * 
 * @author knadeem
 * @version
 */

public class ErpResubmitPaymentModel extends ErpInvoiceModel{
	
	private ErpPaymentMethodI paymentMethod;
	
	public ErpResubmitPaymentModel(){
		super(EnumTransactionType.RESUBMIT_PAYMENT);
	}
	
	public ErpPaymentMethodI getPaymentMethod(){ 
		return paymentMethod; 
	}

    public void setPaymentMethod(ErpPaymentMethodI paymentMethod){ 
    	this.paymentMethod = paymentMethod; 
    }
	
}
