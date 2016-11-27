package com.freshdirect.erp.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface FDXOrderPickEligibleCronHome extends EJBHome{
	
	public FDXOrderPickEligibleCronSB create() throws CreateException, RemoteException;
	

}
