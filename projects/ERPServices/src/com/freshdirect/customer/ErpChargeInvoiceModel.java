/*
 * Created on Mar 30, 2005
 *
 */
package com.freshdirect.customer;

/**
 * @author jng
 */

public class ErpChargeInvoiceModel extends ErpAbstractInvoiceModel {
	
    public ErpChargeInvoiceModel() {
		super(EnumTransactionType.INVOICE_CHARGE);
    }
    
    public ErpChargeInvoiceModel(EnumTransactionType transType){
    	super(transType);
    }
    	
}
