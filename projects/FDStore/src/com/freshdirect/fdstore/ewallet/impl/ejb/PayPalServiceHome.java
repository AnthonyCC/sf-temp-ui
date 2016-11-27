package com.freshdirect.fdstore.ewallet.impl.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface PayPalServiceHome extends EJBHome {
	public PayPalServiceSB create() throws CreateException, RemoteException;
}
