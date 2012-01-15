package com.freshdirect.analytics.ejb;

import java.rmi.RemoteException;
import java.util.Date;

import javax.ejb.EJBObject;

public interface OrderRateSB extends EJBObject{ 

	public void getOrderRate(Date timeStamp) throws RemoteException;

}
