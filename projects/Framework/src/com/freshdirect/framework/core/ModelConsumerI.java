/*
 * $Workfile: ModelConsumerI.java$
 *
 * $Date: 8/1/2001 11:20:20 AM$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.framework.core;

import java.rmi.RemoteException;

/**
 * defines the behavior of all objects whose properties
 * can be set by copying the properties of a model
 *
 * @version $Revision: 2$
 * @author $Author: Viktor Szathmary$
 */ 
public interface ModelConsumerI {
    
    /** sets the properties of an object by copying the properties of a model
     * @param model the model to copy
     * @throws RemoteException any problems during setting the properties of a remotely
     * reachable model consumer
     */    
    public void setFromModel(ModelI model) throws RemoteException;

}

