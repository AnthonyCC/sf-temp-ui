package com.freshdirect.fdstore.standingorders.service;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;


public interface StandingOrdersServiceSB extends EJBObject {
	public StandingOrdersServiceResult.Counter placeStandingOrders() throws RemoteException;
}
