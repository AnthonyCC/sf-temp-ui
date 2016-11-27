/*
 * 
 * Capture.java
 * Date: Sep 23, 2002 Time: 7:01:07 PM
 */
package com.freshdirect.payment.command;

/**
 * 
 * @author knadeem
 */
import java.rmi.*;

import com.freshdirect.payment.PaymentContext;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.payment.PaymentManager;

public class Capture implements PaymentCommandI {
	
	private PaymentContext ctx;
	private String saleId;
	
	public Capture(String saleId){
		this.saleId = saleId;
	}
	
	public void setContext(PaymentContext ctx){
		this.ctx = ctx;
	}
	
	public void execute() throws ErpTransactionException, RemoteException {
		
		//PaymentHome paymentHome = ctx.getPaymentHome();
		//PaymentSB sb = paymentHome.create();
		System.out.println("Going to Capture saleId: "+saleId);
		//sb.captureAuthorization(this.saleId);
		PaymentManager paymentManager = new PaymentManager();
		paymentManager.captureAuthorization(this.saleId);
			
	}

}
