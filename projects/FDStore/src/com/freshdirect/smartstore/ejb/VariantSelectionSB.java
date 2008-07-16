package com.freshdirect.smartstore.ejb;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBObject;

import com.freshdirect.fdstore.util.EnumSiteFeature;

public interface VariantSelectionSB extends EJBObject {
	public Map getVariantMap(EnumSiteFeature feature) throws RemoteException;
	public Map getCohorts() throws RemoteException;
	public List getVariants(EnumSiteFeature feature) throws RemoteException;
}
