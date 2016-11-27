package com.freshdirect.deliverypass.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface DlvPassManagerHome extends EJBHome {
	
	public DlvPassManagerSB create() throws CreateException, RemoteException;

}
