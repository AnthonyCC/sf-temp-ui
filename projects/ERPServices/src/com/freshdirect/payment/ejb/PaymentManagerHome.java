package com.freshdirect.payment.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface PaymentManagerHome extends EJBHome {
	
	public PaymentManagerSB create() throws CreateException, RemoteException;

}
