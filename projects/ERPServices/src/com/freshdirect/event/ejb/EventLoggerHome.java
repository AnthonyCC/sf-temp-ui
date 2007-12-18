package com.freshdirect.event.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 * @author knadeem Date May 3, 2005
 */

public interface EventLoggerHome extends EJBHome {
	
	public EventLoggerSB create() throws CreateException, RemoteException;

}
