package com.freshdirect.giftcard.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface GiftCardManagerHome extends EJBHome {
	
	public GiftCardManagerSB create() throws CreateException, RemoteException;

}
