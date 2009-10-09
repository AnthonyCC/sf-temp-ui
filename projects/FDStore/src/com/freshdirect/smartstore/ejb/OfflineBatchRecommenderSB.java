package com.freshdirect.smartstore.ejb;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.smartstore.offline.OfflineBatchRecommendationsResult;

public interface OfflineBatchRecommenderSB extends EJBObject {
	public OfflineBatchRecommendationsResult runBatch(String siteFeature)
			throws RemoteException, FDResourceException;
}
