package com.freshdirect.smartstore.ejb;

import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.EJBObject;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.util.EnumSiteFeature;

public interface OfflineRecommenderSB extends EJBObject {
	public List<ProductModel> recommend(EnumSiteFeature siteFeature,
			String customerEmail, ContentNodeModel currentNode)
			throws RemoteException, FDResourceException;
}
