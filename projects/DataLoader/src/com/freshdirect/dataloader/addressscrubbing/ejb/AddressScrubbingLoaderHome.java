package com.freshdirect.dataloader.addressscrubbing.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface AddressScrubbingLoaderHome extends EJBHome{
	
	public AddressScrubberLoaderSB create() throws CreateException, RemoteException;	

}
