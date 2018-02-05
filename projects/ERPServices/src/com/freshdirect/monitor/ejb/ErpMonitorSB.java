/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.monitor.ejb;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;
import java.util.Properties;

import com.freshdirect.fdstore.FDResourceException;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
/**
 *@deprecated Please use the DbMonitorController and DbMonitorServiceI in Storefront2.0 project.
 * SVN location :: https://appdevsvn.nj01/appdev/ecommerce
 *
 *
 */
public interface ErpMonitorSB extends EJBObject {
    
	@Deprecated
    public void healthCheck() throws FDResourceException, RemoteException;

	public Properties monitorAndLoadProperties(String string,
			String clusterName, String nodeName, Properties defaults)throws FDResourceException, RemoteException;
    
}
