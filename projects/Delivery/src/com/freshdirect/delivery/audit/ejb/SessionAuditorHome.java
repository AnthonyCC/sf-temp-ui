package com.freshdirect.delivery.audit.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface SessionAuditorHome extends EJBHome {
	public SessionAuditorSB create() throws CreateException, RemoteException;
}
