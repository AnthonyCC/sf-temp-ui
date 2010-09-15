package com.freshdirect.content.attributes.ejb;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import javax.ejb.EJBObject;

import com.freshdirect.content.attributes.ErpsAttributes;
import com.freshdirect.content.attributes.ErpsAttributesKey;

public interface AttributesSB extends EJBObject {
	public Map<ErpsAttributesKey, ErpsAttributes> loadAttributes(Date since) throws RemoteException;
	
	public ErpsAttributes loadAttributes(ErpsAttributesKey key) throws RemoteException;
	
	public void storeAttributes(ErpsAttributes attributes) throws RemoteException;
	
	public void storeAttributes(Collection<ErpsAttributes> attributes) throws RemoteException;
}
