/*
 * DlvManagerHome.java
 *
 * Created on August 27, 2001, 7:03 PM
 */

package com.freshdirect.delivery.ejb;

/**
 *
 * @author  gkalsanka
 * @version 
 */
import javax.ejb.*;
import java.rmi.RemoteException;

public interface DlvRestrictionManagerHome extends EJBHome{
    
    public DlvRestrictionManagerSB create() throws CreateException, RemoteException;

}

