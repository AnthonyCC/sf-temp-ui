package com.freshdirect.event.ejb;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

import com.freshdirect.framework.event.FDEvent;

/**
 * @author knadeem Date May 3, 2005
 */
public interface EventLoggerSB extends EJBObject {
	
	public void log(FDEvent evnet) throws RemoteException; 

}
