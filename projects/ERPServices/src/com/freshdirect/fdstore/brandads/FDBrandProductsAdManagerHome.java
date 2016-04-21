package com.freshdirect.fdstore.brandads;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface FDBrandProductsAdManagerHome extends EJBHome {

	public FDBrandProductsAdManagerSB create() throws CreateException, RemoteException;
}
