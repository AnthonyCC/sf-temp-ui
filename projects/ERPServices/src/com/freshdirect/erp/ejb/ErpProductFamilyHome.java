package com.freshdirect.erp.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface ErpProductFamilyHome extends EJBHome{
	
	public ErpProductFamilySB create() throws CreateException, RemoteException;

}
