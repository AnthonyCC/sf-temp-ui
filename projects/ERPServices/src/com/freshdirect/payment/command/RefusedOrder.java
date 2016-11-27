/*
 * 
 * RefusedOrder.java
 * Date: Sep 24, 2002 Time: 6:20:17 PM
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

public class RefusedOrder implements PaymentCommandI {
	
	private PaymentContext ctx;
	private String saleId;
	private boolean fullReturn;
	private boolean alcoholOnly;
	
	public RefusedOrder(String saleId, boolean fullReturn, boolean alcoholOnly){
		this.saleId = saleId;
		this.fullReturn = fullReturn;
		this.alcoholOnly = alcoholOnly;
	}
	
	public void setContext(PaymentContext ctx){
		this.ctx = ctx;
	}
	
	public void execute() throws ErpTransactionException, RemoteException, ErpSaleNotFoundException {
		
		try{
			ErpCustomerManagerHome managerHome = this.ctx.getCustomerManagerHome();
			ErpCustomerManagerSB sb = managerHome.create();
			sb.markAsReturn(this.saleId, fullReturn, alcoholOnly);
			
		}catch(CreateException ce){
			throw new RuntimeException("CreateException occured: "+ce.getMessage());	
		}	
	}

}
