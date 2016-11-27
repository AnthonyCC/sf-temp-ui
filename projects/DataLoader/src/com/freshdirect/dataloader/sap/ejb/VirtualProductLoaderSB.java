/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.dataloader.sap.ejb;

import java.rmi.RemoteException;
import java.util.Map;

import javax.ejb.EJBObject;

import com.freshdirect.dataloader.LoaderException;

/**
 * the remote interface for the VirtualProductLoader session bean
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public interface VirtualProductLoaderSB extends EJBObject {
    
    /** performs the actual load in a single batch
     * @param products the collection of virtual products to set up
     * @throws RemoteException any system level problems
     * @throws LoaderException any problems encountered creating or updating objects in the system
     */    
    public void loadData(Map<String, Map<String, Object>> products) throws RemoteException, LoaderException;

}

