package com.freshdirect.smartstore.ejb;

import java.rmi.RemoteException;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJBObject;
/**
 *@deprecated Please use the DynamicSiteFeatureController and DynamicSiteFeatureServiceI in Storefront2.0 project.
 * SVN location :: https://appdevsvn.nj01/appdev/ecommerce
 *
 *
 */
public interface DyfModelSB extends EJBObject {
	@Deprecated
	public Map getProductFrequencies(String customerID) throws RemoteException;
	@Deprecated
	public Set getProducts(String customerID) throws RemoteException;
	@Deprecated
	public Map getGlobalProductScores() throws RemoteException;
}
