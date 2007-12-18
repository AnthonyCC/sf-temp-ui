/*
 * 
 * Redelivery.java
 * Date: Sep 24, 2002 Time: 6:08:09 PM
 */
package com.freshdirect.payment.command;

/**
 * 
 * @author knadeem
 */
import javax.ejb.*;
import java.rmi.*;

import com.freshdirect.payment.PaymentContext;
import com.freshdirect.customer.*;
import com.freshdirect.customer.ejb.*;

public class Redelivery implements PaymentCommandI {
	
	private PaymentContext ctx;
	private String saleId;
	
	public Redelivery(String saleId){
		this.saleId = saleId;
	}
	
	public void setContext(PaymentContext ctx){
		this.ctx = ctx;
	}
	
	public void execute() throws ErpTransactionException, RemoteException, ErpSaleNotFoundException {
		
		try{
			ErpCustomerManagerHome managerHome = this.ctx.getCustomerManagerHome();
			ErpCustomerManagerSB sb = managerHome.create();
			sb.markAsRedelivery(this.saleId);
			
		}catch(CreateException ce){
			throw new RuntimeException("CreateException occured: "+ce.getMessage());	
		}	
	}

}
