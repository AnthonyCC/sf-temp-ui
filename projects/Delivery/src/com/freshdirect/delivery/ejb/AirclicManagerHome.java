/*
 * DlvManagerHome.java
 *
 * Created on August 27, 2001, 7:03 PM
 */

package com.freshdirect.delivery.ejb;

/**
 *
 * @author  knadeem
 * @version 
 */
import javax.ejb.*;
import java.rmi.RemoteException;

public interface AirclicManagerHome extends EJBHome{
    
    public AirclicManagerSB create() throws CreateException, RemoteException;

}

