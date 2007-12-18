/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.fdstore.ejb;

import javax.ejb.*;
import java.rmi.RemoteException;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public interface FDFactoryHome extends EJBHome {
    
    public FDFactorySB create() throws CreateException, RemoteException;

}

