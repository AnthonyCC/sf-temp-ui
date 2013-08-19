package com.freshdirect.dataloader.payment.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface ProfileCreatorHome extends EJBHome{
	
	public ProfileCreatorSB create() throws CreateException, RemoteException;
	

}