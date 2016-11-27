/*
 * 
 * PaymentGaewaySB.java
 * Date: Sep 23, 2002 Time: 12:09:36 PM
 */
package com.freshdirect.payment.ejb;

/**
 * 
 * @author knadeem
 */
import javax.ejb.*;
import java.rmi.RemoteException;

import com.freshdirect.payment.command.PaymentCommandI;

public interface PaymentGatewaySB extends EJBObject {
	
	public void updateSaleDlvStatus(PaymentCommandI command) throws RemoteException;

}
