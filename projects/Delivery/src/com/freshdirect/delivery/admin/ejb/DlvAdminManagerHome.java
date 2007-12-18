package com.freshdirect.delivery.admin.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface DlvAdminManagerHome extends EJBHome {
	
	public DlvAdminManagerSB create() throws CreateException, RemoteException;

}
