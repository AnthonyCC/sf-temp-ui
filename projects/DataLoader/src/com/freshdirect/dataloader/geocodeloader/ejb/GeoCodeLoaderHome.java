package com.freshdirect.dataloader.geocodeloader.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface GeoCodeLoaderHome extends EJBHome{
	
	public GeoCodeLoaderSB create() throws CreateException, RemoteException;	

}
