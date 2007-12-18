/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.dataloader.sap.ejb;

import javax.ejb.*;
import java.rmi.RemoteException;

/**
 * home interface for the VirtualProductLoaderSessionBean
 *
 * @version $Revision$
 * @author $Author$
 */
public interface VirtualProductLoaderHome extends EJBHome {
    
    /** creates a new VirtualProductLoader session bean
     * @throws CreateException any problems creating a bean for use
     * @throws EJBException any unexpected container-related problems
     * @throws RemoteException any unexpected system or communications related problems
     * @return a remote VirtualProduct session bean
     */    
    public VirtualProductLoaderSB create() throws CreateException, EJBException, RemoteException;

}

