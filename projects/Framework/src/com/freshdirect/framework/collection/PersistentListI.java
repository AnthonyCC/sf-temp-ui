/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.framework.collection;

import com.freshdirect.framework.core.PersistentI;

/**
 * lists that are persistent need to be able to save themselves to the
 * persistent store and also produce a list of model objects of its
 * members
 *
 * @version $Revision$
 * @author $Author$
 */ 
public interface PersistentListI extends ListI, PersistentI {
    
    /** 
     * gets a list of model objects of this lists members.
     *
     * @return a list of model objects
     */    
    public LocalObjectList getModelList();

}

