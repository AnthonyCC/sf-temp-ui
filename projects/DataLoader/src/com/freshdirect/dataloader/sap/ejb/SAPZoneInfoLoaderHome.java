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

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EJBHome;

/**
 * home interface for the SAPLoaderSessionBean
 *
 * @version $Revision$
 * @author $Author$
 */
public interface SAPZoneInfoLoaderHome extends EJBHome {
    
    /** creates a new SAPLoader session bean
     * @throws CreateException any problems creating a bean for use
     * @throws EJBException any unexpected container-related problems
     * @throws RemoteException any unexpected system or communications related problems
     * @return a remote SAPLoader session bean
     */    
    public SAPZoneInfoLoaderSB create() throws CreateException, EJBException, RemoteException;

}

