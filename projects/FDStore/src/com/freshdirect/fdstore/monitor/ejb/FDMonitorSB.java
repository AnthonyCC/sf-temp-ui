/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.fdstore.monitor.ejb;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

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
public interface FDMonitorSB extends EJBObject {
    @Deprecated
    public void healthCheck() throws FDResourceException, RemoteException;
    
}
