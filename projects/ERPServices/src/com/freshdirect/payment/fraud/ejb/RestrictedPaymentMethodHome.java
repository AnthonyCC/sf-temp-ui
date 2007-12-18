package com.freshdirect.payment.fraud.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;


public interface RestrictedPaymentMethodHome extends EJBHome {

	   public RestrictedPaymentMethodSB create() throws CreateException, RemoteException;
	
}
