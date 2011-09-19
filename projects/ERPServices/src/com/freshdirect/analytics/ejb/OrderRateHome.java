package com.freshdirect.analytics.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface OrderRateHome extends EJBHome{
	
	public OrderRateSB create() throws CreateException, RemoteException;
	

}
