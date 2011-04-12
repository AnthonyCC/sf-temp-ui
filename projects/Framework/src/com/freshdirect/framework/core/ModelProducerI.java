package com.freshdirect.framework.core;

import java.rmi.RemoteException;

/**
 * this interface is implemented by objects that can externalize their state
 * into a serializable model object
 * @version $Revision: 2$
 * @author $Author: Viktor Szathmary$
 */ 
public interface ModelProducerI {
    
    /** gets a model representing the state of an object
     * @throws RemoteException any problems encountered while getting the model of
     * a remotely reachable object
     * @return the model representing the state of an object
     */    
    public ModelI getModel() throws RemoteException;

}

