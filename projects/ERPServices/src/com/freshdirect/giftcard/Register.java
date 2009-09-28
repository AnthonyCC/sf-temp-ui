/*
 * 
 * Capture.java
 * Date: Sep 23, 2002 Time: 7:01:07 PM
 */
package com.freshdirect.giftcard;

/**
 * 
 * @author knadeem
 */
import java.io.Serializable;
import java.rmi.*;

import com.freshdirect.payment.PaymentContext;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.payment.PaymentManager;

public class Register implements Serializable {
	
	private String saleId;
	private double saleAmount;
	
	public Register(String saleId, double amount){
		this.saleId = saleId;
		this.saleAmount = amount;
	}

	public String getSaleId() {
		return saleId;
	}

	public double getSaleAmount() {
		return saleAmount;
	}

}
