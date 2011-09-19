package com.freshdirect.dataloader.analytics.ejb;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

public interface BounceCancelSB extends EJBObject{ 

	public void cancelBounce() throws RemoteException;

}
