
package com.freshdirect.delivery.ejb;

/**
 *
 * @author  tbalumuri
 * @version 
 */
import javax.ejb.*;
import java.rmi.RemoteException;

public interface CapacitySnapshotHome extends EJBHome{
    
    public CapacitySnapshotSB create() throws CreateException, RemoteException;

}

