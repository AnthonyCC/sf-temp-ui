package com.freshdirect.content.attributes.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface AttributesHome extends EJBHome {
	public AttributesSB create() throws CreateException, RemoteException;
}
