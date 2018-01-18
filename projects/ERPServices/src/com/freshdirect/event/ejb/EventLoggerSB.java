package com.freshdirect.event.ejb;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

import com.freshdirect.framework.event.FDWebEvent;

/**
 * @author knadeem Date May 3, 2005
 */
/**
 *@deprecated Please use the EventLoggerController and EventLoggerServiceI in Storefront2.0 project.
 * SVN location :: https://appdevsvn.nj01/appdev/ecommerce
 *
 *
 */
public interface EventLoggerSB extends EJBObject {
	@Deprecated
	public void log(FDWebEvent evnet) throws RemoteException; 

}
