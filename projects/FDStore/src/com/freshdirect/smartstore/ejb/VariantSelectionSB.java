package com.freshdirect.smartstore.ejb;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBObject;

import com.freshdirect.fdstore.util.EnumSiteFeature;
/**
 *@deprecated Please use the VariantSelectionController and VariantSelectionServiceI in Storefront2.0 project.
 * SVN location :: https://appdevsvn.nj01/appdev/ecommerce
 *
 *
 */
public interface VariantSelectionSB extends EJBObject {
	@Deprecated
	public Map<String,String> getVariantMap(EnumSiteFeature feature) throws RemoteException;
	@Deprecated
	public Map<String,String> getVariantMap(EnumSiteFeature feature, Date date) throws RemoteException;
	@Deprecated
	public Map<String, Integer> getCohorts() throws RemoteException;
	@Deprecated
	public List<String> getCohortNames() throws RemoteException;
	@Deprecated
	public List<String> getVariants(EnumSiteFeature feature) throws RemoteException;
	@Deprecated
	public List<Date> getStartDates() throws RemoteException;
}
