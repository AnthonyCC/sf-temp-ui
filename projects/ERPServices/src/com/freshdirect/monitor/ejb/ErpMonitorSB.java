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
public interface ErpMonitorSB extends EJBObject {
    
    public void healthCheck() throws FDResourceException, RemoteException;

	public Properties monitorAndLoadProperties(String string,
			String clusterName, String nodeName, Properties defaults)throws FDResourceException, RemoteException;
    
}
