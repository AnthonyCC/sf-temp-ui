package com.freshdirect.dataloader.notification.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface HandoffNotificationHome extends EJBHome{
	
	public HandoffNotificationSB create() throws CreateException, RemoteException;
	

}
