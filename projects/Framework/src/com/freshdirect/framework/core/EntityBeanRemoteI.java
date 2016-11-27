package com.freshdirect.framework.core;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;

/** the basic interface that all entity bean remote interfaces should implement
 */ 
public interface EntityBeanRemoteI extends IdentifiedI, ModelConsumerI, ModelProducerI, EJBObject {
    
    /**
     * a method that does nothing but throw an exception
     * its purpose is to force the container to discard
     * an instance from its cache and re-instantiate it
     * next time it is requested
     * @throws RemoteException unexpected system level exception
     * @throws EJBException this exception is intentionally thrown by this method to force the
     * container to discard an instance
     */
    public void invalidate() throws RemoteException;
    
}

