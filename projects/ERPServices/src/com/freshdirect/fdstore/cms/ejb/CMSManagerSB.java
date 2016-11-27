package com.freshdirect.fdstore.cms.ejb;

import java.rmi.RemoteException;
import javax.ejb.EJBObject;

public interface CMSManagerSB extends EJBObject {
	public void createFeed(String feedId, String storeId, String feedData) throws RemoteException;
	public String getLatestFeed(String storeId) throws RemoteException;
}
