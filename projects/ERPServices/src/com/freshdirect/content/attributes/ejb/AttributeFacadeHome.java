/*
 * $Workfile: AttributeFacadeHome.java$
 *
 * $Date: 8/20/2001 8:30:02 PM$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.content.attributes.ejb;

import javax.ejb.*;
import java.rmi.RemoteException;

/**
 *
 *
 * @version $Revision: 1$
 * @author $Author: Viktor Szathmary$
 */
public interface AttributeFacadeHome extends EJBHome {
    
    public AttributeFacadeSB create() throws CreateException, RemoteException;

}

