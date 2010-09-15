package com.freshdirect.content.attributes;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import com.freshdirect.common.ERPServiceLocator;
import com.freshdirect.content.attributes.ejb.AttributesSB;
import com.freshdirect.fdstore.FDResourceException;

public class AttributesFacade {
	public static Map<ErpsAttributesKey, ErpsAttributes> loadAttributes(Date since) throws FDResourceException {
		try {
			AttributesSB sb = ERPServiceLocator.getInstance().getAttributesSessionBean();
			return sb.loadAttributes(since);
		} catch (RemoteException e) {
			throw new FDResourceException(e);
		}
	}

	public static ErpsAttributes loadAttributes(ErpsAttributesKey key) throws FDResourceException {
		try {
			AttributesSB sb = ERPServiceLocator.getInstance().getAttributesSessionBean();
			return sb.loadAttributes(key);
		} catch (RemoteException e) {
			throw new FDResourceException(e);
		}
	}

	public static void storeAttributes(Collection<ErpsAttributes> attributes) throws FDResourceException {
		try {
			AttributesSB sb = ERPServiceLocator.getInstance().getAttributesSessionBean();
			sb.storeAttributes(attributes);
		} catch (RemoteException e) {
			throw new FDResourceException(e);
		}
	}

	public static void storeAttributes(ErpsAttributes attributes) throws FDResourceException {
		try {
			AttributesSB sb = ERPServiceLocator.getInstance().getAttributesSessionBean();
			sb.storeAttributes(attributes);
		} catch (RemoteException e) {
			throw new FDResourceException(e);
		}
	}
}
