package com.freshdirect.payment.gateway.ewallet.impl;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

import com.freshdirect.payment.ejb.ReconciliationSB;

public interface PayPalReconciliationHome extends EJBHome {

	public PayPalReconciliationSB create() throws CreateException, RemoteException;
	
}
