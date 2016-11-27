/*
 * 
 * PaymentGaewaySB.java
 * Date: Sep 23, 2002 Time: 12:09:36 PM
 */
package com.freshdirect.giftcard.ejb;

/**
 * 
 * @author skrishnasamy
 */
import javax.ejb.*;
import java.rmi.RemoteException;

import com.freshdirect.giftcard.Register;
import com.freshdirect.payment.command.PaymentCommandI;

public interface GCGatewaySB extends EJBObject {
	
	public void sendRegisterGiftCard(String saleId, double saleAmount) throws RemoteException;

}
