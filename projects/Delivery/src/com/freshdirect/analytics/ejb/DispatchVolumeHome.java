package com.freshdirect.analytics.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface DispatchVolumeHome extends EJBHome{
	
	public DispatchVolumeSB create() throws CreateException, RemoteException;
	

}
