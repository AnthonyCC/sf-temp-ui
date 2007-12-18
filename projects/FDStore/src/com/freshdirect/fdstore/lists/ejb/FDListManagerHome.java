/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.fdstore.lists.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public interface FDListManagerHome extends EJBHome {
    
    public final static String JNDI_HOME = "freshdirect.fdstore.ListManager";

	public FDListManagerSB create() throws CreateException, RemoteException;

}

