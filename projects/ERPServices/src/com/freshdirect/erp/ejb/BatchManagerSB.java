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
public interface BatchManagerSB extends EJBObject {
    
    
    //public void updateStatus(int batchNumber, String user, EnumBatchStatus status, String notes) throws RemoteException, LoaderException;
    
    //public Collection getBatches(EnumBatchStatus status, java.util.Date since) throws RemoteException, LoaderException;
    
    public BatchModel getBatch(int batchNumber) throws RemoteException;
    
    public Collection getBatches() throws RemoteException;
    
    public Collection getRecentBatches() throws RemoteException;
    
    
}

