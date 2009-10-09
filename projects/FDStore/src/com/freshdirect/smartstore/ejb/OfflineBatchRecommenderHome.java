package com.freshdirect.smartstore.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface OfflineBatchRecommenderHome extends EJBHome {
	public static final String JNDI_HOME = "freshdirect.smartstore.OfflineBatchRecommender";

	public OfflineBatchRecommenderSB create() throws CreateException, RemoteException;
}
