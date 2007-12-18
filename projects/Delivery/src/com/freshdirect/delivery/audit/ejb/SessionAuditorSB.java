package com.freshdirect.delivery.audit.ejb;

import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.EJBObject;

import com.freshdirect.framework.util.DateRange;

public interface SessionAuditorSB extends EJBObject {
	List getSessionLogEntries(DateRange range) throws RemoteException;
	void userLoggedIn(String sessionId, String username, String role) throws RemoteException;
	void userLogggedOut(String sessionId) throws RemoteException;
	void userInteraction(String sessionId) throws RemoteException;
}
