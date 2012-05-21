package com.freshdirect.fdstore.standingorders.service;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.EJBObject;

import com.freshdirect.fdstore.standingorders.SOResult;

public interface StandingOrdersServiceSB extends EJBObject {
	public SOResult.ResultList placeStandingOrders(Collection<String> soList, boolean createIfSoiExistsForWeek)throws RemoteException;
}
