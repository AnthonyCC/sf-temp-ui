/*
 * 
 * PaymentContext.java
 * Date: Sep 23, 2002 Time: 6:56:40 PM
 */
package com.freshdirect.payment;

/**
 * 
 * @author knadeem
 */
import javax.naming.*;

import com.freshdirect.payment.ejb.*;
import com.freshdirect.customer.ejb.*;

public class PaymentContext {
	
	private Context ctx;
	private PaymentHome paymentHome = null;
	private ErpCustomerManagerHome custManagerHome = null;
	
	public PaymentContext(Context ctx) throws NamingException {
		this.ctx = ctx;
		this.paymentHome = (PaymentHome) ctx.lookup("freshdirect.payment.Payment");
		this.custManagerHome = (ErpCustomerManagerHome) ctx.lookup("freshdirect.erp.CustomerManager");
	}
	
	public PaymentHome getPaymentHome(){
		return this.paymentHome;
	}
	
	public ErpCustomerManagerHome getCustomerManagerHome(){
		return this.custManagerHome;
	}
}
