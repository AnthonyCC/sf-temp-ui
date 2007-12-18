/* 
 * PaymentHome.java
 * Date: 06/26/2002
 */

package com.freshdirect.payment.ejb;

/**
 * 
 * @author knadeem
 */
import javax.ejb.*;
import java.rmi.RemoteException;

public interface PaymentHome extends EJBHome {
	public PaymentSB create() throws CreateException, RemoteException;
}
