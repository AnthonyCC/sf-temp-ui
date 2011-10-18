package com.freshdirect.fdstore.standingorders.service;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.EJBObject;

import com.freshdirect.fdstore.standingorders.StandingOrdersServiceResult;

public interface StandingOrdersServiceSB extends EJBObject {
	public StandingOrdersServiceResult.Counter placeStandingOrders() throws RemoteException;
	public StandingOrdersServiceResult.Counter placeStandingOrders(Collection<String> soList)throws RemoteException;
}
