package com.freshdirect.analytics.ejb;

import java.rmi.RemoteException;
import java.sql.Timestamp;

import javax.ejb.EJBObject;

public interface OrderRateSB extends EJBObject{ 

	public void getOrderRate(Timestamp timeStamp) throws RemoteException;

}
