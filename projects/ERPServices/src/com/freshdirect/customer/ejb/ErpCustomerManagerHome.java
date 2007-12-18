/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.customer.ejb;

import javax.ejb.*;
import java.rmi.RemoteException;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public interface ErpCustomerManagerHome extends EJBHome {
    
    public ErpCustomerManagerSB create() throws CreateException, RemoteException;

}

