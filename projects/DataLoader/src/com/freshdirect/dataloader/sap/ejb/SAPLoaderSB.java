/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.dataloader.sap.ejb;

import java.util.*;

import javax.ejb.*;
import java.rmi.RemoteException;

import com.freshdirect.dataloader.*;

/**
 * the remote interface for the SAPLoader session bean
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public interface SAPLoaderSB extends EJBObject {
    
    /** performs the actual load using data assembled by the parsers and the tree builder
     * in a single batch
     *
     * @param classes the collection of classes to be updated in this batch
     * @param activeMaterials the collection of materials to be created or updated in this batch
     * @param characteristicValuePrices the collection of characteristic value prices to create or update in this batch
     * @throws RemoteException any system level problems
     * @throws LoaderException any problems encountered creating or updating objects in the system
     */    
    public void loadData(HashMap classes, HashMap activeMaterials, HashMap characteristicValuePrices) throws RemoteException, LoaderException;

}

