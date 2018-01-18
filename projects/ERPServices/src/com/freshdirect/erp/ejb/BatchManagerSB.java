/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.erp.ejb;

import java.util.*;

import javax.ejb.*;
import java.rmi.RemoteException;

import com.freshdirect.erp.model.*;

/**
 * the remote interface for the BatchManager session bean
 *
 *
 * @version $Revision$
 * @author $Author$
 */
/**
 *@deprecated Please use the ErpBatchManagerController and ErpBatchManagerServiceI in Storefront2.0 project.
 * SVN location :: https://appdevsvn.nj01/appdev/ecommerce
 *
 *
 */
public interface BatchManagerSB extends EJBObject {
    
    
    //public void updateStatus(int batchNumber, String user, EnumBatchStatus status, String notes) throws RemoteException, LoaderException;
    
    //public Collection getBatches(EnumBatchStatus status, java.util.Date since) throws RemoteException, LoaderException;
    @Deprecated
    public BatchModel getBatch(int batchNumber) throws RemoteException;
    @Deprecated
    public Collection getBatches() throws RemoteException;
    @Deprecated
    public Collection getRecentBatches() throws RemoteException;
    
    
}

