package com.freshdirect.dataloader.payment.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface SaleCronHome extends EJBHome{
	
	public SaleCronSB create() throws CreateException, RemoteException;
	

}
