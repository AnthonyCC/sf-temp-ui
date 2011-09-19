package com.freshdirect.dataloader.analytics.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface BounceCancelHome extends EJBHome{
	
	public BounceCancelSB create() throws CreateException, RemoteException;
	

}
