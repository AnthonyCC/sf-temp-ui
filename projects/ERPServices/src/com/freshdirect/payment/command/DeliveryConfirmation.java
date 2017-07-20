package com.freshdirect.payment.command;

import java.rmi.RemoteException;

import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.payment.PaymentContext;
import com.freshdirect.payment.PaymentManager;

public class DeliveryConfirmation implements PaymentCommandI {
	
	private PaymentContext ctx;
	private String saleId;
	
	public DeliveryConfirmation(String saleId){
		this.saleId = saleId;
	}
	
	public void setContext(PaymentContext ctx){
		this.ctx = ctx;
	}
	
	public void execute() throws ErpTransactionException, RemoteException {

		PaymentManager paymentManager = new PaymentManager();
		paymentManager.deliveryConfirm(this.saleId);
		
	}
//Introduced for Storefront2.0  to get the saleId
	/**
	 * @return the saleId
	 */
	public String getSaleId() {
		return saleId;
	}


}
