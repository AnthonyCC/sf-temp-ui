/*
 * 
 * PaymentCommand.java
 * Date: Sep 23, 2002 Time: 6:53:05 PM
 */
package com.freshdirect.payment.command;

/**
 * 
 * @author knadeem
 */
import java.io.Serializable;

import com.freshdirect.payment.PaymentContext;

public interface PaymentCommandI extends Serializable {
	
	public void setContext(PaymentContext ctx);
	
	public void execute() throws Exception;

}
