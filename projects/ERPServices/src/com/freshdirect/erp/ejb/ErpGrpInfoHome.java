package com.freshdirect.erp.ejb;

/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */


import javax.ejb.*;
import java.rmi.RemoteException;

/**
 * home interface for the ErpMaterialInfoSessionBean
 *
 * @version $Revision$
 * @author $Author$
 */
public interface ErpGrpInfoHome extends EJBHome {
    
    /** creates a new ErpInfo session bean
     * @throws CreateException any problems creating a bean for use
     * @throws EJBException any unexpected container-related problems
     * @throws RemoteException any unexpected system or communications related problems
     * @return a remote ErpMaterialInfo session bean
     */    
    public ErpGrpInfoSB create() throws CreateException, RemoteException;

}

