package com.freshdirect.payment.ewallet.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface EwalletActivityLogHome extends EJBHome{
	
	public EwalletActivityLogSB create() throws CreateException, RemoteException;
}
