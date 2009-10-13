package com.freshdirect.smartstore.ejb;

import java.rmi.RemoteException;
import java.util.Set;

import javax.ejb.EJBObject;

import com.freshdirect.fdstore.FDResourceException;

public interface OfflineRecommenderSB extends EJBObject {
	public Set<String> getRecentCustomers(int days) throws RemoteException,
			FDResourceException;

	public void checkSiteFeature(String siteFeature) throws RemoteException,
			FDResourceException;

	public int recommend(String siteFeature, String customerId,
			String currentNode) throws RemoteException, FDResourceException;
}
