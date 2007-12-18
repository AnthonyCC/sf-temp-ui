package com.freshdirect.delivery.map.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface DlvMapperHome extends EJBHome{
	
	public DlvMapperSB create() throws CreateException, RemoteException;

}
