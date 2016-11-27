/*
 * 
 * PaymentGatewayHome.java
 * Date: Sep 23, 2002 Time: 12:09:51 PM
 */
package com.freshdirect.payment.ejb;

/**
 * 
 * @author knadeem
 */
import javax.ejb.*;
import java.rmi.RemoteException;

public interface PaymentGatewayHome extends EJBHome{
	
	public PaymentGatewaySB create() throws CreateException, RemoteException;

}
