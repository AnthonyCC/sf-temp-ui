package com.freshdirect.enums.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface EnumManagerHome extends EJBHome {
	
	public EnumManagerSB create() throws CreateException, RemoteException;

}
