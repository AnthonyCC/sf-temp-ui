package com.freshdirect.crm.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface CrmManagerHome extends EJBHome {
	
	public CrmManagerSB create() throws CreateException, RemoteException;

}
