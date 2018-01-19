package com.freshdirect.smartstore.ejb;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.EJBObject;

import com.freshdirect.smartstore.SessionImpressionLogEntry;

/**
 *@deprecated Please use the SessionImpressionLogController and SessionImpressionLogService in Storefront2.0 project.
 * SVN location :: https://appdevsvn.nj01/appdev/ecommerce
 *
 *
 */
public interface SessionImpressionLogSB extends EJBObject {
	@Deprecated
	void saveLogEntry(SessionImpressionLogEntry entry) throws RemoteException;
	@Deprecated
	void saveLogEntries(Collection<SessionImpressionLogEntry> entries) throws RemoteException;
}
