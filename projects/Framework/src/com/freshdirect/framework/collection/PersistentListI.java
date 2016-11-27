package com.freshdirect.framework.collection;

import com.freshdirect.framework.core.ModelI;
import com.freshdirect.framework.core.PersistentBeanI;
import com.freshdirect.framework.core.PersistentI;

/**
 * lists that are persistent need to be able to save themselves to the
 * persistent store and also produce a list of model objects of its
 * members
 */ 
public interface PersistentListI<E extends PersistentBeanI> extends ListI<E>, PersistentI {
    
    /** 
     * gets a list of model objects of this lists members.
     *
     * @return a list of model objects
     */    
    public LocalObjectList<ModelI> getModelList();

}

