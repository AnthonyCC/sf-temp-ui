package com.freshdirect.framework.core;

import java.rmi.RemoteException;

/** the interface to be implemented by any object which is uniquely identified by a primary key
 *
 * @version $Revision: 2$
 * @author $Author: Viktor Szathmary$
 */ 
public interface IdentifiedI {

    /** gets the primary key that identifies an implementation
     * @throws RemoteException any problems getting the primary key of a remotely reachable object
     * @return the primary key that uniquely identifies an object
     */    
	public PrimaryKey getPK() throws RemoteException;

}
