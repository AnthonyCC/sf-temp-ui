package com.freshdirect.payment.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface BINInfoManagerHome extends EJBHome{
	
	public BINInfoManagerSB create() throws CreateException, RemoteException;
	

}