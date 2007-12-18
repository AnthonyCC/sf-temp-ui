/*
 * $Workfile: SapGatewayHome.java$
 *
 * $Date: 9/25/2001 6:31:49 PM$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.sap.ejb;

import javax.ejb.*;
import java.rmi.RemoteException;

/**
 *
 *
 * @version $Revision: 1$
 * @author $Author: Viktor Szathmary$
 */
public interface SapGatewayHome extends EJBHome {
    
    public SapGatewaySB create() throws CreateException, RemoteException;

}

