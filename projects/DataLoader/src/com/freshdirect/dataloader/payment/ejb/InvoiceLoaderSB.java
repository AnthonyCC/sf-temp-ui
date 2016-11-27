/*
 * InvoiceLoaderSB.java
 *
 * Created on December 27, 2001, 12:08 PM
 */

package com.freshdirect.dataloader.payment.ejb;

/**
 *
 * @author  knadeem
 * @version 
 */
import java.rmi.RemoteException;

import javax.ejb.EJBObject;

import com.freshdirect.customer.ErpInvoiceModel;
import com.freshdirect.customer.ErpShippingInfo;
import com.freshdirect.customer.ErpTransactionException;

public interface InvoiceLoaderSB extends EJBObject {
	
	public void addAndReconcileInvoice(String saleId, ErpInvoiceModel invoice, ErpShippingInfo shippingInfo) throws ErpTransactionException, RemoteException;

}

