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
/**
 *@deprecated Please use the GiftCardController and GiftCardManagerServiceI in Storefront2.0 project.
 * SVN location :: https://appdevsvn.nj01/appdev/ecommerce
 *
 *
 */ 
public interface GCGatewaySB extends EJBObject {
	@Deprecated
	public void sendRegisterGiftCard(String saleId, double saleAmount) throws RemoteException;

}
