package com.freshdirect.analytics.ejb;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

public interface OrderRateSB extends EJBObject{ 

	public void getOrderRate() throws RemoteException;

}
